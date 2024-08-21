package com.github.kaspiandev.fishybusiness.data;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PointsTable extends Table {

    private static final String CREATE_POINTS_TABLE = """
            CREATE TABLE IF NOT EXISTS points (
                player_uuid CHAR(36),
                points INT,
                PRIMARY KEY(player_uuid)
            );
            """;
    private static final String SET_INITIAL_POINTS = """
            INSERT INTO points (player_uuid, points)
            VALUES (?, ?)
            """;
    private static final String SET_POINTS = """
            UPDATE points
            SET points = ?
            WHERE player_uuid = ?
            """;
    private static final String GET_POINTS = """
            SELECT points FROM points
            WHERE player_uuid = ?
            """;
    private static final String GET_TOP_POINTS = """
            SELECT player_uuid, points FROM points
            ORDER BY points DESC
            LIMIT 10
            """;

    public PointsTable(Database database) {
        super(database, CREATE_POINTS_TABLE);
    }

    public CompletableFuture<Void> addPoints(Player player, int amount) {
        return getPoints(player).thenAccept((points) -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(SET_POINTS)) {
                    statement.setInt(1, points + amount);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occured.", ex);
            }
        });
    }

    public CompletableFuture<Integer> getPoints(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(GET_POINTS)) {
                    statement.setString(1, player.getUniqueId().toString());
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        int points = resultSet.getInt(1);
                        resultSet.close();
                        return points;
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occurred.", ex);
            }
            return null;
        }).thenCompose((points) -> {
            if (points != null) {
                return CompletableFuture.completedFuture(points);
            } else {
                return initializePoints(player).thenApply((v) -> 0);
            }
        });
    }

    public CompletableFuture<Map<UUID, Integer>> getTopPoints() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(GET_TOP_POINTS)) {
                    ResultSet resultSet = statement.executeQuery();
                    Map<UUID, Integer> topPoints = new LinkedHashMap<>();
                    while (resultSet.next()) {
                        topPoints.put(UUID.fromString(resultSet.getString(1)), resultSet.getInt(2));
                    }
                    resultSet.close();
                    return topPoints;
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occurred.", ex);
            }
        });
    }

    public CompletableFuture<Void> initializePoints(Player player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(SET_INITIAL_POINTS)) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.executeUpdate();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occured.", ex);
            }
        });
    }

}

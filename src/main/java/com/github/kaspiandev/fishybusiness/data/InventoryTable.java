package com.github.kaspiandev.fishybusiness.data;

import com.github.kaspiandev.fishybusiness.util.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class InventoryTable extends Table {

    private static final String CREATE_INVENTORY_TABLE = """
            CREATE TABLE IF NOT EXISTS inventory_backup (
                player_uuid CHAR(36),
                inventory BLOB,
                PRIMARY KEY(player_uuid)
            );
            """;
    private static final String SAVE_INVENTORY = """
            INSERT OR REPLACE INTO inventory_backup (player_uuid, inventory)
            VALUES (?, ?)
            """;
    private static final String GET_INVENTORY = """
            SELECT inventory FROM inventory_backup
            WHERE player_uuid = ?
            """;

    public InventoryTable(Database database) {
        super(database, CREATE_INVENTORY_TABLE);
    }

    public CompletableFuture<Void> saveInventory(Player player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(SAVE_INVENTORY)) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setBytes(2, InventoryUtil.encodeInventory(player.getInventory()));
                    statement.executeUpdate();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occured.", ex);
            }
        });
    }

    public CompletableFuture<Optional<ItemStack[]>> loadInventory(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(GET_INVENTORY)) {
                    statement.setString(1, player.getUniqueId().toString());
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        return Optional.of(InventoryUtil.decodeInventory(resultSet.getBytes(1)));
                    }
                    return Optional.empty();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occured.", ex);
            }
        });
    }

}

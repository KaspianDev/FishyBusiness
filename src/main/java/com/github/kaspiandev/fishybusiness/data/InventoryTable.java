package com.github.kaspiandev.fishybusiness.data;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class InventoryTable extends Table {

    private static final String CREATE_INVENTORY_TABLE = """
            CREATE TABLE IF NOT EXISTS inventory_backup (
                player_uuid CHAR(36),
                inventory TEXT
            );
            """;
    private static final String SAVE_INVENTORY = """
            INSERT INTO whitelist (player_uuid, player_name)
            VALUES (?, ?)
            """;

    public InventoryTable(Database database) {
        super(database, CREATE_INVENTORY_TABLE);
    }

    public CompletableFuture<Void> saveInventory(Player player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getSQLConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(WHITELIST_UUID)) {
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                    connection.close();
                    return true;
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occured.", ex);
            }
        });
    }

}

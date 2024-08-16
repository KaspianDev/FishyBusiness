package com.github.kaspiandev.fishybusiness.data;

import com.github.kaspiandev.fishybusiness.util.InventoryUtil;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class InventoryTable extends Table {

    private static final String CREATE_INVENTORY_TABLE = """
            CREATE TABLE IF NOT EXISTS inventory_backup (
                player_uuid CHAR(36),
                inventory BLOB
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
                try (PreparedStatement statement = connection.prepareStatement(SAVE_INVENTORY)) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setBytes(2, InventoryUtil.encodeInventory(player.getInventory()));
                    statement.executeUpdate();
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("An SQL exception occured.", ex);
            }
        });
    }

}

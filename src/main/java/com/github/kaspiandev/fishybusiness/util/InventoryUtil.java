package com.github.kaspiandev.fishybusiness.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class InventoryUtil {

    private InventoryUtil() {}

    public static byte[] encodeInventory(Inventory inventory) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            ItemStack[] items = inventory.getContents();
            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                byte[] encodedItem = encodeItem(item);
                dataOutput.writeObject(encodedItem);
            }

            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static ItemStack[] decodeInventory(byte[] bytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int size = dataInput.readInt();
            ItemStack[] items = new ItemStack[size];

            for (int i = 0; i < size; i++) {
                byte[] encodedItem = (byte[]) dataInput.readObject();
                ItemStack item = decodeItem(encodedItem);
                items[i] = item;
            }

            return items;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static byte[] encodeItem(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static ItemStack decodeItem(byte[] bytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            Object object = dataInput.readObject();
            return (object == null) ? null : (ItemStack) object;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}

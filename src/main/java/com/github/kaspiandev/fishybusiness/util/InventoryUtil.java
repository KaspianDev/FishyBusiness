package com.github.kaspiandev.fishybusiness.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

public class InventoryUtil {

    private InventoryUtil() {}

    // Encodes the Inventory into a byte array
    public static byte[] encodeInventory(Inventory inventory) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            ItemStack[] items = inventory.getContents();
            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                if (item == null) {
                    dataOutput.writeBoolean(false);
                } else {
                    dataOutput.writeBoolean(true);
                    byte[] encodedItem = encodeItem(item);
                    dataOutput.writeObject(encodedItem);
                }
            }

            return outputStream.toByteArray();
        } catch (Exception exception) {
            System.err.println("Error during encoding inventory: " + exception.getMessage());
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    // Decodes the byte array back into an array of ItemStacks
    public static ItemStack[] decodeInventory(byte[] bytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int size = dataInput.readInt();
            ItemStack[] items = new ItemStack[size];

            for (int i = 0; i < size; i++) {
                boolean isNotNull = dataInput.readBoolean();
                if (isNotNull) {
                    byte[] encodedItem = (byte[]) dataInput.readObject();
                    ItemStack item = decodeItem(encodedItem);
                    items[i] = item;
                } else {
                    items[i] = null;
                }
            }

            return items;
        } catch (EOFException e) {
            System.err.println("EOFException encountered while decoding inventory. Data might be incomplete.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception exception) {
            System.err.println("Error during decoding inventory: " + exception.getMessage());
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    // Encodes an ItemStack into a byte array
    public static byte[] encodeItem(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            System.err.println("Error during encoding item: " + exception.getMessage());
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    // Decodes a byte array back into an ItemStack
    public static ItemStack decodeItem(byte[] bytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            return (ItemStack) dataInput.readObject();
        } catch (Exception exception) {
            System.err.println("Error during decoding item: " + exception.getMessage());
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

}

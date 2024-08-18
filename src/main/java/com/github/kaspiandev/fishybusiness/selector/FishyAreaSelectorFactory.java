package com.github.kaspiandev.fishybusiness.selector;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.pdc.LocationPersistentDataType;
import com.github.kaspiandev.fishybusiness.util.ColorUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FishyAreaSelectorFactory {

    private final NamespacedKey selectorKey;
    private final NamespacedKey corner1Key;
    private final NamespacedKey corner2Key;

    public FishyAreaSelectorFactory(FishyBusiness plugin) {
        this.selectorKey = new NamespacedKey(plugin, "fishyareaselector");
        this.corner1Key = new NamespacedKey(plugin, "corner-1");
        this.corner2Key = new NamespacedKey(plugin, "corner-2");
        new SelectorListener(plugin);
    }

    // TODO: configurable selector item
    public ItemStack getSelector() {
        ItemStack selectorItem = new ItemStack(Material.STICK);
        ItemMeta selectorMeta = selectorItem.getItemMeta();
        assert selectorMeta != null;

        PersistentDataContainer pdc = selectorMeta.getPersistentDataContainer();
        pdc.set(selectorKey, PersistentDataType.BOOLEAN, true);
        pdc.set(corner1Key, LocationPersistentDataType.INSTANCE, LocationPersistentDataType.INVALID_LOCATION);
        pdc.set(corner2Key, LocationPersistentDataType.INSTANCE, LocationPersistentDataType.INVALID_LOCATION);

        selectorMeta.setDisplayName(ColorUtil.string("&e&lArea Selector"));

        selectorItem.setItemMeta(selectorMeta);

        return selectorItem;
    }

    public boolean isSelector(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(selectorKey, PersistentDataType.BOOLEAN);
    }

    public Location getCorner1(ItemStack selector) {
        ItemMeta selectorMeta = selector.getItemMeta();
        assert selectorMeta != null;

        PersistentDataContainer pdc = selectorMeta.getPersistentDataContainer();
        return pdc.get(corner1Key, LocationPersistentDataType.INSTANCE);
    }

    public void setCorner1(ItemStack selector, Location location) {
        ItemMeta selectorMeta = selector.getItemMeta();
        assert selectorMeta != null;

        PersistentDataContainer pdc = selectorMeta.getPersistentDataContainer();
        pdc.set(corner1Key, LocationPersistentDataType.INSTANCE, location);

        selector.setItemMeta(selectorMeta);
    }

    public Location getCorner2(ItemStack selector) {
        ItemMeta selectorMeta = selector.getItemMeta();
        assert selectorMeta != null;

        PersistentDataContainer pdc = selectorMeta.getPersistentDataContainer();
        return pdc.get(corner2Key, LocationPersistentDataType.INSTANCE);
    }

    public void setCorner2(ItemStack selector, Location location) {
        ItemMeta selectorMeta = selector.getItemMeta();
        assert selectorMeta != null;

        PersistentDataContainer pdc = selectorMeta.getPersistentDataContainer();
        pdc.set(corner2Key, LocationPersistentDataType.INSTANCE, location);

        selector.setItemMeta(selectorMeta);
    }

}

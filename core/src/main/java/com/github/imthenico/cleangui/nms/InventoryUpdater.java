package com.github.imthenico.cleangui.nms;

import com.github.imthenico.cleangui.util.Version;
import org.bukkit.entity.Player;

public interface InventoryUpdater {

    void updateInventory(Player player, String title);

    @SuppressWarnings("unchecked")
    static InventoryUpdater newUpdater() {
        Class<? extends InventoryUpdater> clazz = null;
        try {
            clazz = (Class<? extends InventoryUpdater>) Class.forName(String.format("com.github.imthenico.cleangui.%s.InventoryUpdater", Version.revision));

            return clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
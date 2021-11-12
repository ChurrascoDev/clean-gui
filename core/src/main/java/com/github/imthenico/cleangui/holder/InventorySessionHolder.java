package com.github.imthenico.cleangui.holder;

import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventorySessionHolder implements InventoryHolder {

    private final InventorySession session;

    public InventorySessionHolder(InventorySession session) {
        this.session = Validate.notNull(session);
    }

    @Override
    public Inventory getInventory() {
        return session.handle();
    }

    public InventorySession getSession() {
        return session;
    }
}
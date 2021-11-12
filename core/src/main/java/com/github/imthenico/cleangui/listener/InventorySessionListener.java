package com.github.imthenico.cleangui.listener;

import com.github.imthenico.cleangui.holder.InventorySessionHolder;
import com.github.imthenico.cleangui.manager.item.GUIItem;
import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.model.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class InventorySessionListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventorySession session = getSession(event.getClickedInventory());

        if (session == null)
            return;

        GUI model = session.model();

        int clickedSlot = event.getSlot();

        GUIItem item = session.getDisplayedItem(clickedSlot);

        if (item == null)
            return;

        event.setCancelled(
                item.getEventAction().test(event, session) || session.model().cancelClick()
        );

        if (clickedSlot == model.getPreviousPageItem().getKey()) {
            session.previousPage().open();
        } else if (clickedSlot == model.getNextPageItem().getKey()) {
            session.nextPage().open();
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        InventorySession session = getSession(event.getInventory());

        if (session == null)
            return;

        event.setCancelled(session.model().onOpen().test(event));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventorySession session = getSession(event.getInventory());

        if (session == null)
            return;

        session.model().onClose().accept(event);
        session.handleInventoryClose();
    }

    private InventorySession getSession(Inventory clickedInventory) {
        if (clickedInventory != null && clickedInventory.getHolder() instanceof InventorySessionHolder) {
            return ((InventorySessionHolder) clickedInventory.getHolder()).getSession();
        }

        return null;
    }
}
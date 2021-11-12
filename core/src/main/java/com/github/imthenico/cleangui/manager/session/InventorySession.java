package com.github.imthenico.cleangui.manager.session;

import com.github.imthenico.cleangui.manager.item.GUIItem;
import com.github.imthenico.cleangui.model.GUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Timer;

public interface InventorySession extends Runnable {

    Timer SESSION_UPDATER_TIMER = new Timer("Session Updater Thread");

    Player getOpener();

    String displayedTitle();

    GUIItem getItem(int position);

    GUIItem getPermanentItem(int slot);

    GUIItem getDisplayedItem(int slot);

    int getCurrentPage();

    int getTotalPages();

    boolean isLastPage();

    boolean isFirstPage();

    InventorySession nextPage();

    InventorySession previousPage();

    Inventory handle();

    GUI model();

    boolean isOpened();

    void open();

    void handleInventoryClose();
}
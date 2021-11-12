package com.github.imthenico.cleangui.model;

import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GUI {

    SlotModel getSlotData(int position);

    Map<Integer, SlotModel> permanentItemData();

    Map<Integer, SlotModel> getSlotData();

    Collection<Integer> getAllowedSlots();

    BiFunction<Integer, Integer, String> getTitleProvider();

    int maxItemsPerPage();

    int size();

    boolean cancelClick();

    Pair<Integer, SlotModel> getNextPageItem();

    Pair<Integer, SlotModel> getPreviousPageItem();

    Predicate<InventoryOpenEvent> onOpen();

    Consumer<InventoryCloseEvent> onClose();

    Map<UUID, InventorySession> getCachedSessions();

    InventorySession getSession(Player player);
}
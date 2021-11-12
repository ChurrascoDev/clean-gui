package com.github.imthenico.cleangui.model;

import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.manager.session.SimpleInventorySession;
import com.github.imthenico.cleangui.util.Pair;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleGUI implements GUI {

    private final int maxItemsPerPage;
    private final int size;
    private final BiFunction<Integer, Integer, String> title;
    private final boolean cancelClick;
    private final Pair<Integer, SlotModel> nextPageButton;
    private final Pair<Integer, SlotModel> previousPageButton;
    private final Map<Integer, SlotModel> slotData = new HashMap<>();
    private final Predicate<InventoryOpenEvent> onOpen;
    private final Consumer<InventoryCloseEvent> onClose;
    private final Map<UUID, InventorySession> sessions = new HashMap<>();
    private final Map<Integer, SlotModel> permanentItemData = new HashMap<>();
    private final Collection<Integer> allowedSlots;

    public SimpleGUI(
            int maxItemsPerPage,
            int size,
            BiFunction<Integer, Integer, String> title,
            boolean cancelClick,
            Pair<Integer, SlotModel> nextPageButton,
            Pair<Integer, SlotModel> previousPageButton,
            Predicate<InventoryOpenEvent> onOpen,
            Consumer<InventoryCloseEvent> onClose,
            Map<Integer, SlotModel> slotData,
            Map<Integer, SlotModel> permanentItemData,
            Collection<Integer> allowedSlots
    ) {
        this.maxItemsPerPage = maxItemsPerPage;
        this.size = size;
        this.title = Validate.notNull(title);
        this.cancelClick = cancelClick;
        this.nextPageButton = nextPageButton;
        this.previousPageButton = previousPageButton;
        this.onOpen = Validate.notNull(onOpen);
        this.onClose = Validate.notNull(onClose);
        this.slotData.putAll(slotData);
        this.permanentItemData.putAll(permanentItemData);
        this.allowedSlots = allowedSlots;
    }

    @Override
    public SlotModel getSlotData(int position) {
        return slotData.get(position);
    }

    @Override
    public Map<Integer, SlotModel> permanentItemData() {
        return permanentItemData;
    }

    @Override
    public Map<Integer, SlotModel> getSlotData() {
        return slotData;
    }

    @Override
    public Collection<Integer> getAllowedSlots() {
        return allowedSlots;
    }

    @Override
    public BiFunction<Integer, Integer, String> getTitleProvider() {
        return title;
    }

    @Override
    public int maxItemsPerPage() {
        return maxItemsPerPage;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean cancelClick() {
        return cancelClick;
    }

    @Override
    public Pair<Integer, SlotModel> getNextPageItem() {
        return nextPageButton;
    }

    @Override
    public Pair<Integer, SlotModel> getPreviousPageItem() {
        return previousPageButton;
    }

    @Override
    public Predicate<InventoryOpenEvent> onOpen() {
        return onOpen;
    }

    @Override
    public Consumer<InventoryCloseEvent> onClose() {
        return onClose;
    }

    @Override
    public Map<UUID, InventorySession> getCachedSessions() {
        return sessions;
    }

    @Override
    public InventorySession getSession(Player player) {
        InventorySession inventorySession = sessions.get(player.getUniqueId());

        if (inventorySession == null)
            inventorySession = new SimpleInventorySession(player.getUniqueId(), this);

        return inventorySession;
    }
}
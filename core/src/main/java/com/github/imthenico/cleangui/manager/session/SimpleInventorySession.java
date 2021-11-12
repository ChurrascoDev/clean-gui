package com.github.imthenico.cleangui.manager.session;

import com.github.imthenico.cleangui.holder.InventorySessionHolder;
import com.github.imthenico.cleangui.manager.item.GUIItem;
import com.github.imthenico.cleangui.model.GUI;
import com.github.imthenico.cleangui.model.SlotModel;
import com.github.imthenico.cleangui.nms.InventoryUpdater;
import com.github.imthenico.cleangui.util.DynamicObject;
import com.github.imthenico.cleangui.util.PageUtils;
import com.github.imthenico.cleangui.util.Pair;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.*;

public class SimpleInventorySession implements InventorySession {

    private final UUID openerUUID;
    private final GUI model;
    private String title;
    private final Map<Integer, GUIItem> cachedItems;
    private final GUIItem[] permanentItems;
    private final GUIItem[] displayedItems;
    private int currentPage = 1;
    private final int totalPages;
    private final Inventory handle;
    private TimerTask task;
    private final Pair<GUIItem, GUIItem> switches;
    private final static InventoryUpdater INVENTORY_UPDATER = InventoryUpdater.newUpdater();

    public SimpleInventorySession(UUID openerUUID, GUI model) {
        this.openerUUID = Validate.notNull(openerUUID);
        this.model = Validate.notNull(model);

        this.cachedItems = new HashMap<>();
        this.permanentItems = new GUIItem[model().size()];

        Iterator<Integer> allowedSlots = model.getAllowedSlots() != null ? model.getAllowedSlots().iterator() : null;
        for (Map.Entry<Integer, SlotModel> entry : model.getSlotData().entrySet()) {
            int index = entry.getKey();
            SlotModel data = entry.getValue();

            int slot;

            if (allowedSlots != null) {
                if (!allowedSlots.hasNext()) {
                    allowedSlots = model.getAllowedSlots().iterator();
                }

                slot = allowedSlots.next();
            } else {
                slot = normalize(index);
            }

            GUIItem item = newItem(slot, index, data);
            this.cachedItems.put(index, item);
        }

        model.permanentItemData().forEach((slot, data) -> {
            if (slot >= model.size())
                return;

            GUIItem item = newItem(slot, slot, data);

            this.permanentItems[slot] = item;
        });

        this.totalPages = PageUtils.getTotalPages(cachedItems.size(), model.maxItemsPerPage());
        this.displayedItems = new GUIItem[model.size()];
        this.switches = new Pair<>(
                newItem(model.getPreviousPageItem().getKey(), -1, model.getPreviousPageItem().getValue()),
                newItem(model.getNextPageItem().getKey(), -1, model.getNextPageItem().getValue())
        );

        this.title = model.getTitleProvider().apply(currentPage, totalPages);

        handle = Bukkit.createInventory(new InventorySessionHolder(this), model.size(), title);
    }

    @Override
    public Player getOpener() {
        return Bukkit.getPlayer(openerUUID);
    }

    @Override
    public String displayedTitle() {
        return title;
    }

    @Override
    public GUIItem getItem(int position) {
        return cachedItems.get(position);
    }

    @Override
    public GUIItem getPermanentItem(int slot) {
        if (slot >= model.size())
            return null;

        return permanentItems[slot];
    }

    @Override
    public GUIItem getDisplayedItem(int slot) {
        if (slot >= model.size() || displayedItems == null)
            return null;

        return displayedItems[slot];
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public InventorySession nextPage() {
        if (!isLastPage()) {
            currentPage++;
        }

        return this;
    }

    @Override
    public InventorySession previousPage() {
        if (!isFirstPage()) {
            currentPage--;
        }

        return this;
    }

    @Override
    public Inventory handle() {
        return handle;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public boolean isLastPage() {
        return currentPage == totalPages;
    }

    @Override
    public boolean isFirstPage() {
        return currentPage == 1;
    }

    @Override
    public GUI model() {
        return model;
    }

    @Override
    public boolean isOpened() {
        Player player = getOpener();

        if (player == null || !player.isOnline())
            return false;

        InventoryView view = player.getOpenInventory();

        return Objects.equals(view.getTopInventory(), handle);
    }

    @Override
    public void open() {
        boolean opened = isOpened();
        Player player = getOpener();

        if (opened) {
            clear();
        }

        displayItems();

        if (opened) {
            INVENTORY_UPDATER.updateInventory(player, (this.title = model.getTitleProvider().apply(currentPage, totalPages)));
        } else {
            player.openInventory(handle);
        }

        startTask();
    }

    @Override
    public void handleInventoryClose() {
        if (displayedItems == null)
            return;

        clear();
    }

    private void clear() {
        for (int i = 0; i < displayedItems.length; i++) {
            GUIItem displayedItem = displayedItems[i];

            if (displayedItem != null) {
                displayedItem.restartFrameIndex();
            }

            displayedItems[i] = null;
        }

        handle.clear();
    }

    @Override
    public void run() {
        if (!isOpened()) {
            stopTask();
            return;
        }

        for (GUIItem displayedItem : displayedItems) {
            if (displayedItem != null && displayedItem.update()) {
                handle.setItem(displayedItem.getInventorySlot(), displayedItem.getFrameToDisplay());
            }
        }
    }

    private void displayItems() {
        for (GUIItem pageItem : PageUtils.getPageItems(currentPage, model.maxItemsPerPage(), cachedItems.values().toArray(new GUIItem[0]))) {
            if (pageItem != null) {
                displayedItems[pageItem.getInventorySlot()] = pageItem;
            }
        }

        for (GUIItem permanentItem : permanentItems) {
            if (permanentItem != null) {
                displayedItems[permanentItem.getInventorySlot()] = permanentItem;
            }
        }

        displaySwitches();

        for (GUIItem displayedItem : displayedItems) {
            if (displayedItem != null) {
                handle.setItem(displayedItem.getInventorySlot(), displayedItem.getFrameToDisplay());
            }
        }
    }

    private void startTask() {
        if (task == null) {
            this.task = new TimerTask() {
                @Override
                public void run() {
                    SimpleInventorySession.this.run();
                }
            };

            SESSION_UPDATER_TIMER.scheduleAtFixedRate(task, 0, 50);
        }
    }

    private void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private int normalize(int index) {
        if (index >= model.maxItemsPerPage()) {
            int previousPage = index / model.maxItemsPerPage();
            int quantityOfSlots = (previousPage) * model.maxItemsPerPage();

            return index - quantityOfSlots;
        }

        return index;
    }

    private GUIItem newItem(int slot, int position, SlotModel model) {
        if (model == null)
            return null;

        return new GUIItem(new DynamicObject<>(model.getItemsToDisplay()), slot, position < 0 ? slot : position, model.getEventAction());
    }

    private void displaySwitches() {
        GUIItem previousPage = switches.getKey();
        GUIItem nextPage = switches.getValue();

        if (previousPage != null && !isFirstPage()) {
            displayedItems[previousPage.getInventorySlot()] = previousPage;
        }

        if (nextPage != null && !isLastPage()) {
            displayedItems[nextPage.getInventorySlot()] = nextPage;
        }
    }
}
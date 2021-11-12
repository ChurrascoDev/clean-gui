package com.github.imthenico.cleangui.manager.item;

import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.util.Counter;
import com.github.imthenico.cleangui.util.DynamicObject;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BiPredicate;

public class GUIItem {

    private final DynamicObject<ItemStack> itemStacks;
    private final int inventorySlot;
    private final int index;
    private final BiPredicate<InventoryClickEvent, InventorySession> eventAction;

    public GUIItem(DynamicObject<ItemStack> itemStacks, int inventorySlot, int index, BiPredicate<InventoryClickEvent, InventorySession> eventAction) {
        this.itemStacks = Validate.notNull(itemStacks);
        this.inventorySlot = Validate.isTrue(inventorySlot >= 0, "position < 0", inventorySlot);
        this.index = index;
        this.eventAction = eventAction;
    }

    public GUIItem(int totalUpdates, int inventorySlot, int index, BiPredicate<InventoryClickEvent, InventorySession> eventAction, ItemStack... itemStacks) {
        this(new DynamicObject<>(totalUpdates, itemStacks), inventorySlot, index, eventAction);
    }

    public GUIItem applyMeta(ItemMeta meta) {
        this.itemStacks.forEach(itemStack -> itemStack.getObject().setItemMeta(meta));

        return this;
    }

    public boolean update() {
        return this.itemStacks.updateInternalIndex();
    }

    public ItemStack getFrameToDisplay() {
        return this.itemStacks.getCurrent();
    }

    public void restartFrameIndex() {
        Counter counter = this.itemStacks.getCounter();

        if (counter != null)
            counter.restart();

        this.itemStacks.setIndex(0);
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public int getIndex() {
        return index;
    }

    public BiPredicate<InventoryClickEvent, InventorySession> getEventAction() {
        return eventAction;
    }
}
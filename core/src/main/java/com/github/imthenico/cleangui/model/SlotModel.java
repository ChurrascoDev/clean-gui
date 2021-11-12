package com.github.imthenico.cleangui.model;

import com.github.imthenico.cleangui.builder.ItemMetaBuilder;
import com.github.imthenico.cleangui.builder.SimpleSlotModelBuilder;
import com.github.imthenico.cleangui.builder.SlotModelBuilder;
import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class SlotModel {

    private final Map<ItemStack, Integer> itemsToDisplay;
    private final ItemMetaBuilder itemMetaProvider;
    private final BiPredicate<InventoryClickEvent, InventorySession> eventAction;

    public SlotModel(
            int totalUpdates,
            ItemMetaBuilder itemMetaProvider,
            BiPredicate<InventoryClickEvent, InventorySession> eventAction,
            ItemStack... itemsToDisplay) {
        this(itemMetaProvider, eventAction, ((Supplier<Map<ItemStack, Integer>>) () -> {
            Map<ItemStack, Integer> items = new HashMap<>();

            for (ItemStack itemStack : itemsToDisplay) {
                items.put(itemStack, totalUpdates);
            }
            return items;
        }).get());
    }

    public SlotModel(
            ItemMetaBuilder itemMetaProvider,
            BiPredicate<InventoryClickEvent, InventorySession> eventAction,
            Map<ItemStack, Integer> itemsToDisplay
    ) {
        this.itemsToDisplay = Collections.unmodifiableMap(itemsToDisplay);
        this.itemMetaProvider = itemMetaProvider;
        this.eventAction = Validate.defIfNull(eventAction, (event, session) -> false);

        if (itemMetaProvider != null) {
            for (ItemStack itemStack : itemsToDisplay.keySet()) {
                ItemMeta meta = itemMetaProvider.build(itemStack.getType());

                if (meta != null)
                    itemStack.setItemMeta(meta);
            }
        }
    }

    public SlotModel(
            int totalUpdates,
            BiPredicate<InventoryClickEvent, InventorySession> eventAction, ItemStack... itemsToDisplay
    ) {
        this(totalUpdates, null, eventAction, itemsToDisplay);
    }

    public SlotModel(
            ItemStack itemStack,
            BiPredicate<InventoryClickEvent, InventorySession> eventAction) {
        this(-1, null, eventAction, itemStack);
    }

    public Map<ItemStack, Integer> getItemsToDisplay() {
        return itemsToDisplay;
    }

    public ItemMetaBuilder getItemMetaProvider() {
        return itemMetaProvider;
    }

    public BiPredicate<InventoryClickEvent, InventorySession> getEventAction() {
        return eventAction;
    }

    public static SlotModelBuilder builder() {
        return new SimpleSlotModelBuilder();
    }
}
package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.model.GUI;
import com.github.imthenico.cleangui.model.SimpleGUI;
import com.github.imthenico.cleangui.model.SlotModel;
import com.github.imthenico.cleangui.util.Pair;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleGUIBuilder implements GUIBuilder {

    private BiFunction<Integer, Integer, String> titleProvider;
    private int maxItemsPerPage;
    private final int size;
    private boolean cancelClick;
    private Pair<Integer, SlotModel> nextPageButton = new Pair<>(-1, null);
    private Pair<Integer, SlotModel> previousPageButton = new Pair<>(-1, null);
    private final Map<Integer, SlotModel> slotData = new HashMap<>();
    private final Map<Integer, SlotModel> permanentItemData = new HashMap<>();
    private Predicate<InventoryOpenEvent> onOpen = (event) -> false;
    private Consumer<InventoryCloseEvent> onClose = (event) -> {};
    private Collection<Integer> allowedSlots;

    public SimpleGUIBuilder(int size) {
        this.size = Validate.isTrue(size > 0, "size <= 0", size);
        Validate.isTrue(size % 9 == 0, "size must be divisible by 9");

        this.maxItemsPerPage = size;
    }

    @Override
    public GUIBuilder title(BiFunction<Integer, Integer, String> titleProvider) {
        this.titleProvider = Validate.notNull(titleProvider);
        return this;
    }

    @Override
    public GUIBuilder title(String title) {
        return title((i1, i2) -> title);
    }

    @Override
    public GUIBuilder maxItemsPerPage(int itemsPerPage) {
        this.maxItemsPerPage = itemsPerPage;

        return this;
    }

    @Override
    public GUIBuilder cancelClick(boolean cancel) {
        this.cancelClick = cancel;

        return this;
    }

    @Override
    public GUIBuilder nextPageButtonData(int slot, SlotModel model) {
        this.nextPageButton = new Pair<>(
                Validate.isTrue(slot >= 0, "slot < 0", slot),
                Validate.notNull(model)
        );

        return this;
    }

    @Override
    public GUIBuilder previousPageButtonData(int slot, SlotModel model) {
        this.previousPageButton = new Pair<>(
                Validate.isTrue(slot >= 0, "slot < 0", slot),
                Validate.notNull(model)
        );

        return this;
    }

    @Override
    public GUIBuilder slotData(int position, SlotModel model, boolean permanent) {
        if (permanent) {
            this.permanentItemData.put(
                    Validate.isTrue(position >= 0 && position < size, String.format("%s < 0 || %s >= %s", position, position, size), position),
                    Validate.notNull(model)
            );
        } else {
            this.slotData.put(
                    Validate.isTrue(position >= 0, "position < 0", position),
                    Validate.notNull(model)
            );
        }

        return this;
    }

    @Override
    public GUIBuilder specifyAllowedSlots(List<Integer> slots) {
        this.allowedSlots = Collections.unmodifiableCollection(slots);

        maxItemsPerPage(slots.size());
        return this;
    }

    @Override
    public GUIBuilder openAction(Predicate<InventoryOpenEvent> openAction) {
        this.onOpen = Validate.notNull(openAction);

        return this;
    }

    @Override
    public GUIBuilder closeAction(Consumer<InventoryCloseEvent> closeAction) {
        this.onClose = Validate.notNull(closeAction);

        return this;
    }

    @Override
    public GUI build() {
        if (titleProvider == null)
            title(InventoryType.CHEST.getDefaultTitle());

        return new SimpleGUI(
                maxItemsPerPage,
                size,
                titleProvider,
                cancelClick,
                nextPageButton,
                previousPageButton,
                onOpen,
                onClose,
                slotData,
                permanentItemData,
                allowedSlots
        );
    }
}
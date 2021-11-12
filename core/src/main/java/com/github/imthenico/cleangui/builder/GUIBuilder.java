package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.fill.FillOperation;
import com.github.imthenico.cleangui.model.GUI;
import com.github.imthenico.cleangui.model.SlotModel;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GUIBuilder {

    GUIBuilder title(BiFunction<Integer, Integer, String> titleProvider);

    GUIBuilder title(String title);

    GUIBuilder maxItemsPerPage(int itemsPerPage);

    GUIBuilder cancelClick(boolean cancel);

    GUIBuilder nextPageButtonData(int slot, SlotModel model);

    GUIBuilder previousPageButtonData(int slot, SlotModel model);

    GUIBuilder slotData(int position, SlotModel model, boolean permanent);

    default GUIBuilder slotData(int position, SlotModelBuilder builder, boolean permanent) {
        return slotData(position, builder.build(), permanent);
    }

    default GUIBuilder applyFill(SlotModel slotModel, boolean permanent, FillOperation... operations) {
        for (FillOperation operation : operations) {
            for (Integer integer : operation.complete()) {
                slotData(integer, slotModel, permanent);
            }
        }

        return this;
    }

    default GUIBuilder applyFill(SlotModelBuilder slotModel, boolean permanent, FillOperation... operations) {
        return applyFill(slotModel.build(), permanent, operations);
    }

    GUIBuilder specifyAllowedSlots(List<Integer> slots);

    default GUIBuilder specifyAllowedSlots(FillOperation fillOperation) {
        return specifyAllowedSlots(fillOperation.complete());
    }

    GUIBuilder openAction(Predicate<InventoryOpenEvent> openAction);

    GUIBuilder closeAction(Consumer<InventoryCloseEvent> closeAction);

    GUI build();

}
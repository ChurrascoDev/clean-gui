package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.model.SlotModel;
import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class SimpleSlotModelBuilder implements SlotModelBuilder {

    private final Map<ItemStack, Integer> itemStacks = new HashMap<>();
    private ItemMetaBuilder metaBuilder = ItemMetaBuilder.newBuilder();
    private BiPredicate<InventoryClickEvent, InventorySession> action;
    private boolean cancelClick;

    @Override
    public SlotModelBuilder item(int updateTime, ItemStack... itemStacks) {
        for (ItemStack itemStack : Validate.notNull(itemStacks)) {
            this.itemStacks.put(Validate.notNull(itemStack), updateTime);
        }
        return this;
    }

    @Override
    public SlotModelBuilder item(ItemStack itemStack, int updateTime) {
        this.itemStacks.put(Validate.notNull(itemStack), updateTime);

        return this;
    }

    @Override
    public SlotModelBuilder material(int updateTime, Material material, int damage) {
        this.itemStacks.put(new ItemStack(Validate.notNull(material), 1, (short) damage), updateTime);
        return this;
    }

    @Override
    public SlotModelBuilder material(int updateTime, String name, int damage) {
        return material(updateTime, Material.valueOf(name), (short) damage);
    }

    @Override
    public SlotModelBuilder name(String name) {
        this.metaBuilder.name(name);
        return this;
    }

    @Override
    public SlotModelBuilder lore(String... lore) {
        this.metaBuilder.lore(lore);
        return this;
    }

    @Override
    public SlotModelBuilder enchant(Enchantment enchantment, int level) {
        this.metaBuilder.enchant(enchantment, level);
        return this;
    }

    @Override
    public SlotModelBuilder flag(ItemFlag flag) {
        this.metaBuilder.flag(flag);
        return this;
    }

    @Override
    public SlotModelBuilder action(BiPredicate<InventoryClickEvent, InventorySession> action) {
        this.action = action;
        return this;
    }

    @Override
    public SlotModelBuilder applyMeta(ItemMetaBuilder builder) {
        this.metaBuilder = Validate.notNull(builder);

        return this;
    }

    @Override
    public SlotModelBuilder cancelClick(boolean cancel) {
        this.cancelClick = cancel;
        return null;
    }

    @Override
    public SlotModel build() {
        if (cancelClick)
            action = (event, session) -> {
                if (action != null)
                    action.test(event, session);

                return true;
            };

        Map<ItemStack, Integer> clonedItems = new HashMap<>();

        this.itemStacks.forEach((k, v) -> clonedItems.put(k.clone(), v));

        return new SlotModel(metaBuilder, action, clonedItems);
    }
}
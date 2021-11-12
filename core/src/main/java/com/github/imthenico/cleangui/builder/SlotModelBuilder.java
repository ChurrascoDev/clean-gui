package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.manager.session.InventorySession;
import com.github.imthenico.cleangui.model.SlotModel;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiPredicate;

public interface SlotModelBuilder {

    SlotModelBuilder item(int updateTime, ItemStack... itemStacks);

    SlotModelBuilder item(ItemStack itemStack, int updateTime);

    SlotModelBuilder material(int updateTime, Material material, int damage);

    SlotModelBuilder material(int updateTime, String name, int damage);

    SlotModelBuilder name(String name);

    SlotModelBuilder lore(String... lore);

    SlotModelBuilder enchant(Enchantment enchantment, int level);

    SlotModelBuilder flag(ItemFlag flag);

    SlotModelBuilder action(BiPredicate<InventoryClickEvent, InventorySession> action);

    SlotModelBuilder applyMeta(ItemMetaBuilder builder);

    SlotModelBuilder cancelClick(boolean cancel);

    SlotModel build();
}
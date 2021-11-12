package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.util.Validate;
import com.github.imthenico.cleangui.util.Version;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemMetaBuilder {

    ItemMetaBuilder name(String displayName);

    ItemMetaBuilder lore(String... lore);

    ItemMetaBuilder enchant(Enchantment enchantment, int level);

    ItemMetaBuilder flag(ItemFlag flag);

    default ItemMetaBuilder modifier(Object attribute, Object modifier) {
        throw new UnsupportedOperationException("outdated version");
    }

    default ItemMetaBuilder modifiers(Multimap<Object, Object> modifiers) {
        throw new UnsupportedOperationException("outdated version");
    }

    default ItemMetaBuilder localizedName(String localizedName) {
        throw new UnsupportedOperationException("outdated version");
    }

    default ItemMetaBuilder modelData(int modelData) {
        throw new UnsupportedOperationException("outdated version");
    }

    ItemMeta build(Material material);

    default ItemStack apply(ItemStack itemStack) {
        ItemMeta meta = build(Validate.notNull(itemStack, "itemStack").getType());

        if (meta != null) {
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    static ItemMetaBuilder newBuilder() {
        if (Version.RUNNING_VERSION == Version.v1_8)
            return new BaseItemMetaBuilder();

        return new SimpleItemMetaBuilder();
    }
}
package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.github.imthenico.cleangui.util.TextColorizer.color;

public class BaseItemMetaBuilder implements ItemMetaBuilder {

    private String displayName;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantments;
    private final List<ItemFlag> flags;

    public BaseItemMetaBuilder() {
        this.lore = new ArrayList<>();
        this.enchantments = new HashMap<>();
        this.flags = new ArrayList<>();
    }

    public BaseItemMetaBuilder name(String displayName) {
        this.displayName = color(Validate.notNull(displayName));

        return this;
    }

    public BaseItemMetaBuilder lore(String... lines) {
        this.lore.addAll(Arrays.asList(color(lines)));

        return this;
    }

    public BaseItemMetaBuilder enchant(Enchantment enchantment, int level) {
        this.enchantments.put(Validate.notNull(enchantment), level);

        return this;
    }

    public BaseItemMetaBuilder flag(ItemFlag flag) {
        this.flags.add(Validate.notNull(flag));

        return this;
    }

    public ItemMeta build(Material material) {
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        if (meta != null) {
            if (displayName != null)
                meta.setDisplayName(displayName);

            meta.setLore(lore);
            enchantments.forEach((enchantment, integer) -> meta.addEnchant(enchantment, integer, true));
            flags.forEach(meta::addItemFlags);
        }

        return meta;
    }
}
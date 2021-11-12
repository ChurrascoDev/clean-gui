package com.github.imthenico.cleangui.builder;

import com.github.imthenico.cleangui.util.Validate;
import com.github.imthenico.cleangui.util.Version;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;

public class SimpleItemMetaBuilder extends BaseItemMetaBuilder {
    private final Multimap<Object, Object> modifiers;
    private String localizedName;
    private int modelData;
    private boolean hasCustomModelData;

    public SimpleItemMetaBuilder() {
        this.modifiers = HashMultimap.create();
    }

    @Override
    public ItemMetaBuilder modifier(Object attribute, Object modifier) {
        if (!Version.RUNNING_VERSION.isMajorThan(Version.v1_13)) {
            return super.modifier(attribute, modifier);
        }

        if (attribute instanceof Attribute && modifier instanceof AttributeModifier) {
            modifiers.put(attribute, modifier);
        }

        return this;
    }

    @Override
    public ItemMetaBuilder modifiers(Multimap<Object, Object> modifiers) {
        modifiers.forEach(this::modifier);

        return this;
    }

    @Override
    public ItemMetaBuilder localizedName(String localizedName) {
        if (!Version.RUNNING_VERSION.isMajorThan(Version.v1_9)) {
            return super.localizedName(localizedName);
        }

        this.localizedName = Validate.notNull(localizedName);

        return this;
    }

    @Override
    public ItemMetaBuilder modelData(int modelData) {
        if (!Version.RUNNING_VERSION.isMajorThan(Version.v1_14)) {
            return super.localizedName(localizedName);
        }

        this.modelData = modelData;
        this.hasCustomModelData = true;

        return this;
    }

    @Override
    public ItemMeta build(Material material) {
        ItemMeta meta = super.build(material);

        if (localizedName != null)
            meta.setLocalizedName(localizedName);

        if (hasCustomModelData)
            meta.setCustomModelData(modelData);

        modifiers.forEach((attribute, modifier) -> {
            meta.addAttributeModifier((Attribute) attribute, (AttributeModifier) modifier);
        });

        return meta;
    }
}
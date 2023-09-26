package me.hapyl.mmu3.feature.trim;

import org.bukkit.Color;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import javax.annotation.Nonnull;

public class TrimData {

    private final TrimType type;
    private final ArmorStand stand;

    private boolean current;

    private Color color;
    private EnumTrimPattern pattern;
    private EnumTrimMaterial material;

    public TrimData(TrimType type, ArmorStand stand) {
        this.type = type;
        this.stand = stand;
        this.color = null;
        this.pattern = EnumTrimPattern.SENTRY;
        this.material = EnumTrimMaterial.QUARTZ;

        type.setItem(stand, createItem(new ItemStack(TrimArmor.IRON.getMaterial(type))));
    }

    public void update() {
        setItem(createItem(type.getItem(stand)));
    }

    public void setItem(ItemStack item) {
        type.setItem(stand, item);
    }

    @Nonnull
    public ItemStack getItem() {
        return type.getItem(stand);
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public ArmorStand getStand() {
        return stand;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public EnumTrimPattern getPattern() {
        return pattern;
    }

    public void setPattern(EnumTrimPattern pattern) {
        this.pattern = pattern;
    }

    public EnumTrimMaterial getMaterial() {
        return material;
    }

    public void setMaterial(EnumTrimMaterial material) {
        this.material = material;
    }

    public final ItemStack createItem(ItemStack stack) {
        final ItemStack item = new ItemStack(stack);

        if (!(item.getItemMeta() instanceof ArmorMeta meta)) {
            return item;
        }

        meta.setTrim(new ArmorTrim(material.bukkit, pattern.bukkit));
        item.setItemMeta(meta);

        // Apply color
        if (isLeatherArmor() && color != null) {
            final LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;

            leatherMeta.setColor(color);
            item.setItemMeta(leatherMeta);
        }

        return item;
    }

    public boolean isLeatherArmor() {
        final ItemStack item = type.getItem(stand);

        return switch (item.getType()) {
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS -> true;
            default -> false;
        };
    }
}

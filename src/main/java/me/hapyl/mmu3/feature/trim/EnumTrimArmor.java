package me.hapyl.mmu3.feature.trim;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

public enum EnumTrimArmor implements EnumTrim {

    LEATHER(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS),
    CHAINMAIL(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS),
    IRON(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS),
    GOLD(Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS),
    DIAMOND(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS),
    NETHERITE(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS),
    TURTLE(Material.TURTLE_HELMET, Material.TURTLE_HELMET, Material.TURTLE_HELMET, Material.TURTLE_HELMET);

    private final Material[] materials;

    EnumTrimArmor(Material helmet, Material chest, Material legs, Material feet) {
        this.materials = new Material[]{helmet, chest, legs, feet};
    }

    public ItemStack createItem(EquipmentSlot slot, TrimData data) {
        final ItemStack item = new ItemStack(getMaterial(slot));

        if (!(item.getItemMeta() instanceof ArmorMeta meta)) {
            throw new IllegalArgumentException("invalid item, somehow");
        }

        final Color color = data.getArmorColor(slot);
        final EnumTrimPattern pattern = data.getPattern();
        final EnumTrimMaterial material = data.getMaterial();

        meta.setTrim(new ArmorTrim(material.bukkit, pattern.bukkit));
        item.setItemMeta(meta);

        // Apply color
        if (this == LEATHER) {
            final LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;

            leatherMeta.setColor(color);
            item.setItemMeta(leatherMeta);
        }

        return item;
    }

    public Material getMaterial(EquipmentSlot slot) {
        return materials[slot == EquipmentSlot.HEAD ? 0 : slot == EquipmentSlot.CHEST ? 1 : slot == EquipmentSlot.LEGS ? 2 : 3];
    }

    public Material[] getMaterials() {
        return materials;
    }

    @Override
    public String getName() {
        return Chat.format(name());
    }

    @Override
    public Material getMaterial() {
        return materials[0];
    }
}

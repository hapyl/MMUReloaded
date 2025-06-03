package me.hapyl.mmu3.feature.standeditor;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum TunePart {
    HEAD(Material.LEATHER_HELMET, "Head"),
    BODY(Material.LEATHER_CHESTPLATE, "Body"),
    LEFT_ARM(Material.IRON_SWORD, "Left Arm"),
    RIGHT_ARM(Material.DIAMOND_SWORD, "Right Arm"),
    LEFT_LEG(Material.IRON_LEGGINGS, "Left Leg"),
    RIGHT_LEG(Material.DIAMOND_LEGGINGS, "Right Leg");

    private final Material material;
    private final String name;

    TunePart(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ChatColor.DARK_GREEN + name + " Position";
    }
}

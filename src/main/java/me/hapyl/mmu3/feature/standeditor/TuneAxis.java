package me.hapyl.mmu3.feature.standeditor;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum TuneAxis {
    X(Material.RED_TERRACOTTA, ChatColor.RED),
    Y(Material.GREEN_TERRACOTTA, ChatColor.GREEN),
    Z(Material.BLUE_TERRACOTTA, ChatColor.BLUE);

    private final Material material;
    private final ChatColor color;

    TuneAxis(Material material, ChatColor color) {
        this.material = material;
        this.color = color;
    }

    public Material getMaterial() {
        return this.material;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public double[] scaleIfAxis(double amount) {
        return new double[] { this == X ? amount : 0.0, this == Y ? amount : 0.0, this == Z ? amount : 0.0 };
    }

}

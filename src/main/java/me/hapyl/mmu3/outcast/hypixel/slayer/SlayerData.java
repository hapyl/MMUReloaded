package me.hapyl.mmu3.outcast.hypixel.slayer;

import org.bukkit.Material;

public class SlayerData {

    private final String name;
    private final String lore;
    private final Material icon;

    public SlayerData(String name, String lore, Material icon) {
        this.name = name;
        this.lore = lore;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public Material getIcon() {
        return icon;
    }
}

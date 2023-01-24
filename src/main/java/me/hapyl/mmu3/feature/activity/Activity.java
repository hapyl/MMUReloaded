package me.hapyl.mmu3.feature.activity;

import org.bukkit.Material;

public enum Activity {

    WATER_FLOWING(Material.WATER_BUCKET, "Water Flowing", "Water will not flow.", "Water flows as normal."),
    LAVA_FLOWING(Material.LAVA_BUCKET, "Lava Flowing", "Lava will not flow.", "Lava flows as normal."),
    BLOCK_UPDATE(
            Material.OAK_SAPLING,
            "Block Updates",
            "Blocks won't update; Powders won't fall, support required blocks won't break, etc.",
            "Blocks work as normal."
    ),
    BLOCK_FALLING(Material.SAND, "Block Falling", "Powders will not fall.", "Powders will fall.");

    private final Material material;
    private final String name;
    private final String enableMessage;
    private final String disableMessage;

    Activity(Material material, String name, String enableMessage, String disableMessage) {
        this.material = material;
        this.name = name;
        this.enableMessage = enableMessage;
        this.disableMessage = disableMessage;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getEnableMessage() {
        return enableMessage;
    }

    public String getDisableMessage() {
        return disableMessage;
    }
}

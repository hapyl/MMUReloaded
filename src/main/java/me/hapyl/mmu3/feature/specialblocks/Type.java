package me.hapyl.mmu3.feature.specialblocks;

import me.hapyl.mmu3.util.menu.Size;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public enum Type {

    NATURE(10, Material.SUNFLOWER, "Nature", "Grass and flowers.", Size.SIZE_5),
    DOOR(12, Material.OAK_DOOR, "Doors", "All kinds of doors.", Size.SIZE_5),
    SNOW(14, Material.SNOW, "Snow", "All the layers of snow.", Size.SIZE_4),
    FLUID_WATER(30, Material.WATER_BUCKET, "Water", "All the levels of water.", Size.SIZE_4),
    FLUID_LAVA(32, Material.LAVA_BUCKET, "Lava", "All the levels of lava.", Size.SIZE_4),
    CORAL(16, Material.TUBE_CORAL_FAN, "Corals", "Dead and alive corals.", Size.SIZE_5);

    private final int slot;
    private final Material material;
    private final String name;
    private final String description;
    private final Size size;

    Type(int slot, Material material, String name, String description, Size size) {
        this.slot = slot;
        this.material = material;
        this.name = name;
        this.description = description;
        this.size = size;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public Size getSize() {
        return size;
    }
}

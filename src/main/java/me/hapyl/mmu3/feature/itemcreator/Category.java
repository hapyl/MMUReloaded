package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Lists;
import org.bukkit.Material;

import java.util.List;

public enum Category {

    BLOCKS(Material.BRICKS, "Building blocks, including unobtainable."),
    DECORATION(Material.PEONY, "Decorative blocks, such as flowers."),
    FOOD(Material.APPLE, "Consumable items and brewing ingredients."),
    REDSTONE_AND_TRANSPORTATION(Material.REDSTONE, "Redstone and transportation components."),
    MISCELLANEOUS(Material.LAVA_BUCKET, "Miscellaneous items and uncategorized items."),
    TOOLS_AND_WEAPONS(Material.IRON_AXE, "All items that have durability.");

    private final Material material;
    private final String string;
    private final List<Material> items;

    Category(Material material, String string) {
        this.material = material;
        this.string = string;
        this.items = Lists.newArrayList();
    }

    public Material getMaterial() {
        return material;
    }

    public String getString() {
        return string;
    }

    public List<Material> getItems() {
        return items;
    }

    public void addMaterial(Material material) {
        items.add(material);
    }
}

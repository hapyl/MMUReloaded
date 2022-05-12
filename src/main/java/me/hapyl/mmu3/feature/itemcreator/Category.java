package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Lists;
import org.bukkit.Material;

import java.util.List;

public enum Category {

    BLOCKS(Material.BRICKS, "All blocks, including unobtainable."),
    FOOD(Material.APPLE, "All consumable items."),
    MISC(Material.LAVA_BUCKET, "All items from creative miscellaneous tab."),
    TOOLS(Material.IRON_AXE, "All items that have durability."),
    OTHER(Material.DIAMOND, "Everything else that not included in others categories.");

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

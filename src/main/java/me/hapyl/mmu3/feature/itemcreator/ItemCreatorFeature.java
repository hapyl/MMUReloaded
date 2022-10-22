package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CreativeCategory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ItemCreatorFeature extends Feature {

    // this stores players creators in case they closed it
    private final Map<UUID, ItemCreator> creators;

    public ItemCreatorFeature(Main mmu3plugin) {
        super(mmu3plugin);
        creators = Maps.newHashMap();
        fillCategories();
    }

    private void fillCategories() {
        for (Material material : Material.values()) {
            if (!material.isItem() || material.isAir()) {
                continue;
            }

            final CreativeCategory category = material.getCreativeCategory();
            if (category == null) {
                Category.MISCELLANEOUS.addMaterial(material);
                continue;
            }

            switch (category) {
                case BUILDING_BLOCKS -> Category.BLOCKS.addMaterial(material);
                case DECORATIONS -> Category.DECORATION.addMaterial(material);
                case FOOD, BREWING -> Category.FOOD.addMaterial(material);
                case MISC -> Category.MISCELLANEOUS.addMaterial(material);
                case TOOLS, COMBAT -> Category.TOOLS_AND_WEAPONS.addMaterial(material);
                case REDSTONE, TRANSPORTATION -> Category.REDSTONE_AND_TRANSPORTATION.addMaterial(material);
            }
        }
    }

    @Nullable
    public ItemCreator getCreator(Player player) {
        return creators.get(player.getUniqueId());
    }

    public ItemCreator getCreatorOrCreate(Player player) {
        if (!hasCreator(player)) {
            setCreator(player, new ItemCreator(player));
        }
        return getCreator(player);
    }

    public boolean hasCreator(Player player) {
        return creators.containsKey(player.getUniqueId());
    }

    public void setCreator(Player player, ItemCreator creator) {
        creators.put(player.getUniqueId(), creator);
    }

}

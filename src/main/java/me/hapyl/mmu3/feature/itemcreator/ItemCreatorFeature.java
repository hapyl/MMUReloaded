package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

            if (material.isBlock()) {
                Category.BLOCKS.addMaterial(material);
            }
            else if (material.isEdible()) {
                Category.FOOD.addMaterial(material);
            }
            else if (isTool(material)) {
                Category.TOOLS.addMaterial(material);
            }
            else if (isMisc(material)) {
                Category.MISC.addMaterial(material);
            }
            else {
                Category.OTHER.addMaterial(material);
            }
        }
    }

    private boolean isTool(Material material) {
        return material.getMaxDurability() >= 1;
    }

    private boolean isMisc(Material material) {
        if (hasName(material, "dye", "spawn_egg", "disc", "pattern")) {
            return true;
        }
        else {
            return switch (material) {
                case BEACON, TURTLE_EGG, CONDUIT, SCUTE, COAL, CHARCOAL, DIAMOND, IRON_INGOT, GOLD_INGOT, NETHERITE_INGOT, NETHERITE_SCRAP, STICK, BOWL, STRING, FEATHER, GUNPOWDER, WHEAT_SEEDS, WHEAT, FLINT, BUCKET, WATER_BUCKET, LAVA_BUCKET, SNOWBALL, LEATHER, MILK_BUCKET, PUFFERFISH_BUCKET, SALMON_BUCKET, COD_BUCKET, TROPICAL_FISH_BUCKET, BRICK, CLAY_BALL, PAPER, BOOK, SLIME_BALL, EGG, GLOWSTONE_DUST, INK_SAC, BONE_MEAL, BONE, SUGAR, PUMPKIN_SEEDS, MELON_SEEDS, ENDER_PEARL, BLAZE_ROD, GOLD_NUGGET, NETHER_WART, ENDER_EYE, EXPERIENCE_BOTTLE, FIRE_CHARGE, WRITABLE_BOOK, EMERALD, MAP, NETHER_STAR, FIREWORK_ROCKET, FIREWORK_STAR, NETHER_BRICK, QUARTZ, PRISMARINE_SHARD, PRISMARINE_CRYSTALS, RABBIT_HIDE, IRON_HORSE_ARMOR, GOLDEN_HORSE_ARMOR, DIAMOND_HORSE_ARMOR, LEATHER_HORSE_ARMOR, CHORUS_FRUIT, POPPED_CHORUS_FRUIT, BEETROOT_SEEDS, SHULKER_SHELL, IRON_NUGGET, NAUTILUS_SHELL, HEART_OF_THE_SEA, HONEYCOMB ->
                        true;
                default -> false;
            };
        }
    }

    private boolean hasName(Material material, String... strings) {
        final String lowerCase = material.name().toLowerCase();
        for (String string : strings) {
            if (string.contains(lowerCase)) {
                return true;
            }
        }
        return false;
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

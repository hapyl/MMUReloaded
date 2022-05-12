package me.hapyl.mmu3.feature.specialblocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Snow;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

public class SpecialBlocks extends Feature {

    private final Map<String, SpecialBlock> global; // contains all special blocks no matter the type
    private final Map<Type, Set<SpecialBlock>> perType;

    private final Map<Material, Material> doorToTrapdoorMap;

    public SpecialBlocks(Main mmu3plugin) {
        super(mmu3plugin);
        global = Maps.newHashMap();
        perType = Maps.newHashMap();
        doorToTrapdoorMap = Maps.newHashMap();
        fillMaps();
        createItems();
    }

    public Set<SpecialBlock> getByType(Type type) {
        return perType.getOrDefault(type, new HashSet<>());
    }

    private void addByType(Type type, SpecialBlock block) {
        final Set<SpecialBlock> set = getByType(type);
        set.add(block);
        perType.put(type, set);
    }

    private void fillMaps() {
        doorToTrapdoorMap.put(Material.IRON_DOOR, Material.IRON_TRAPDOOR);
        doorToTrapdoorMap.put(Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR);
        doorToTrapdoorMap.put(Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR);
        doorToTrapdoorMap.put(Material.BIRCH_DOOR, Material.BIRCH_TRAPDOOR);
        doorToTrapdoorMap.put(Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR);
        doorToTrapdoorMap.put(Material.OAK_DOOR, Material.OAK_TRAPDOOR);
        doorToTrapdoorMap.put(Material.SPRUCE_DOOR, Material.SPRUCE_TRAPDOOR);
        doorToTrapdoorMap.put(Material.CRIMSON_DOOR, Material.CRIMSON_TRAPDOOR);
        doorToTrapdoorMap.put(Material.WARPED_DOOR, Material.WARPED_TRAPDOOR);
    }

    private void createItems() {
        if (!perType.isEmpty()) {
            throw new IllegalStateException("special items already created");
        }

        createBlock(10, Material.SUNFLOWER, Material.GREEN_CONCRETE, "Sunflower Flower", true);
        createBlock(19, Material.SUNFLOWER, Material.GREEN_CONCRETE, "Sunflower Stem", false);

        createBlock(11, Material.ROSE_BUSH, Material.MAGENTA_CONCRETE, "Rose Bush", true);
        createBlock(20, Material.ROSE_BUSH, Material.MAGENTA_CONCRETE, "Rose Stem", false);

        createBlock(12, Material.LILAC, Material.PINK_CONCRETE, "Lilac Flower", true);
        createBlock(21, Material.LILAC, Material.PINK_CONCRETE, "Lilac Stem", false);

        createBlock(13, Material.PEONY, Material.PINK_GLAZED_TERRACOTTA, "Peony Flower", true);
        createBlock(22, Material.PEONY, Material.PINK_GLAZED_TERRACOTTA, "Peony Stem", false);

        createBlock(14, Material.TALL_GRASS, Material.GREEN_WOOL, "Tall Grass Top", true);
        createBlock(23, Material.TALL_GRASS, Material.GREEN_WOOL, "Tall Grass Bottom", false);

        createBlock(15, Material.LARGE_FERN, Material.GREEN_GLAZED_TERRACOTTA, "Large Fern Top", true);
        createBlock(24, Material.LARGE_FERN, Material.GREEN_GLAZED_TERRACOTTA, "Large Fern Bottom", false);

        createBlock(
                16,
                Material.CACTUS,
                Material.GREEN_CONCRETE,
                "Cactus",
                (self, event) -> self.setBlock(event.getBlockReplacedState(), Material.CACTUS)
        );


        createBlock(25, Material.DEAD_BUSH, Material.SOUL_SAND, "Dead Bush");

        createBlock(2, Material.RED_MUSHROOM, Material.RED_MUSHROOM_BLOCK, "Red Mushroom");
        createBlock(6, Material.BROWN_MUSHROOM, Material.BROWN_MUSHROOM_BLOCK, "Brown Mushroom");
        createBlock(3, Material.CRIMSON_FUNGUS, Material.NETHER_WART_BLOCK, "Crimson Fungus");
        createBlock(5, Material.WARPED_FUNGUS, Material.WARPED_WART_BLOCK, "Warped Fungus");

        // Create Doors
        int slotDoors = 9;
        for (Material door : doorToTrapdoorMap.keySet()) {
            final Material trapdoor = doorToTrapdoorMap.get(door);
            create(
                    Type.DOOR,
                    slotDoors,
                    door,
                    trapdoor,
                    Chat.capitalize(door.name()) + " Top",
                    1,
                    (self, event) -> self.setDoor(event, door, Bisected.Half.TOP),
                    true
            );
            create(
                    Type.DOOR,
                    slotDoors + 9,
                    door,
                    trapdoor,
                    Chat.capitalize(door.name()) + " Bottom",
                    1,
                    (self, event) -> self.setDoor(event, door, Bisected.Half.BOTTOM),
                    false
            );
            ++slotDoors;
        }

        // Create Snow Layers
        int slotSnow = 11;
        final Snow snowData = (Snow) Bukkit.createBlockData(Material.SNOW);
        for (int i = snowData.getMinimumLayers(); i <= snowData.getMaximumLayers(); i++) {
            final int pos = i;
            create(
                    Type.SNOW,
                    slotSnow,
                    Material.SNOW,
                    Material.SNOW_BLOCK,
                    "Snow Layer " + pos,
                    pos,
                    (self, event) -> self.setLevelled(event, Material.SNOW, pos),
                    false
            );

            slotSnow += (slotSnow % 9 == 6) ? 6 : 1;
        }

        // Create fluids
        int slotFluids = 10;
        for (int i = 0; i < 15; i++) {
            final int plusOne = i + 1;
            create(
                    Type.FLUID_WATER,
                    slotFluids,
                    Material.WATER_BUCKET,
                    Material.BLUE_CONCRETE,
                    "Levelled Water " + plusOne,
                    plusOne,
                    (self, event) -> self.setLevelled(event, Material.WATER, plusOne - 1),
                    false
            );
            create(
                    Type.FLUID_LAVA,
                    slotFluids,
                    Material.LAVA_BUCKET,
                    Material.ORANGE_CONCRETE,
                    "Levelled Lava " + plusOne,
                    plusOne,
                    (self, event) -> self.setLevelled(event, Material.LAVA, plusOne - 1),
                    false
            );

            if (slotFluids == 16) {
                slotFluids += 4;
            }
            else if (slotFluids == 24) {
                slotFluids += 6;
            }
            else {
                slotFluids++;
            }
        }
    }

    @Nullable
    public SpecialBlock byId(String id) {
        return global.get(id);
    }

    private void create(Type type, int slot, Material icon, Material block, String name, String id, int amount, BiConsumer<SpecialBlock, BlockPlaceEvent> event, boolean glow) {
        final SpecialBlock specialBlock = new SpecialBlock(slot, icon, block, name, id, amount, glow) {
            @Override
            public void acceptEvent(BlockPlaceEvent ev) {
                if (event == null) {
                    super.acceptEvent(ev);
                }
                else {
                    event.accept(this, ev);
                }
            }
        };
        global.put(specialBlock.getId(), specialBlock);
        addByType(type, specialBlock);
    }

    private void create(Type type, int slot, Material icon, Material block, String name, int amount, BiConsumer<SpecialBlock, BlockPlaceEvent> event, boolean glow) {
        final String id = name.toLowerCase(Locale.ROOT).replace(" ", "_");
        create(type, slot, icon, block, name, id, amount, event, glow);
    }

    private void createBlock(int slot, Material icon, Material block, String name, int amount, BiConsumer<SpecialBlock, BlockPlaceEvent> event, boolean glow) {
        create(Type.COMMON, slot, icon, block, name, amount, event, glow);
    }

    private void createBlock(int slot, Material icon, Material block, String name) {
        createBlock(slot, icon, block, name, 1, null, false);
    }

    private void createBlock(int slot, Material icon, Material block, String name, boolean glow) {
        createBlock(slot, icon, block, name, 1, null, glow);
    }

    private void createBlock(int slot, Material icon, Material block, String name, BiConsumer<SpecialBlock, BlockPlaceEvent> consumer) {
        createBlock(slot, icon, block, name, 1, consumer, false);
    }

    public List<String> getIdsNoSb() {
        final List<String> strings = Lists.newArrayList();
        for (String value : global.keySet()) {
            strings.add(value.replace("sb.", ""));
        }
        return strings;
    }
}

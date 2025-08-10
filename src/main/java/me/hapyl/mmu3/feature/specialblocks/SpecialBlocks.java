package me.hapyl.mmu3.feature.specialblocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.util.Tuple;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

public class SpecialBlocks extends Feature implements Listener {

    private final Map<String, SpecialBlock> global; // contains all special blocks no matter the type
    private final Map<Type, Set<SpecialBlock>> perType;

    private final Map<Material, Material> doorToTrapdoorMap;
    private final List<Tuple<Material, Material>> coralMapped;

    public SpecialBlocks(Main mmu3plugin) {
        super(mmu3plugin);
        global = Maps.newHashMap();
        perType = Maps.newHashMap();
        doorToTrapdoorMap = Maps.newLinkedHashMap();
        coralMapped = Lists.newArrayList();

        fillMaps();
        createItems();
    }

    @EventHandler()
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack handItem = inventory.getItemInMainHand();

        if (!SpecialBlock.isSpecialBlock(handItem)) {
            return;
        }

        // Fx
        final Location location = block.getLocation();
        final BlockData blockData = block.getBlockData();
        final World world = block.getWorld();

        world.playSound(location, blockData.getSoundGroup().getBreakSound(), 1, 1);
        world.spawnParticle(Particle.BLOCK, location, 1, 0, 0, 0, 1, blockData);

        // If holding a special block, cancel the event and replace the block instead.
        // This is done because most of the special blocks will break upon update.
        ev.setCancelled(true);
        block.setType(Material.AIR, false);
    }

    public Set<SpecialBlock> getByType(Type type) {
        return perType.getOrDefault(type, new HashSet<>());
    }

    @Nullable
    public SpecialBlock byId(String id) {
        return global.get(id);
    }

    public List<String> getIdsNoSb() {
        final List<String> strings = Lists.newArrayList();
        for (String value : global.keySet()) {
            strings.add(value.replace("sb.", ""));
        }
        return strings;
    }

    private void addByType(Type type, SpecialBlock block) {
        final Set<SpecialBlock> set = getByType(type);
        set.add(block);
        perType.put(type, set);
    }

    private void fillMaps() {
        doorToTrapdoorMap.put(Material.OAK_DOOR, Material.OAK_TRAPDOOR);
        doorToTrapdoorMap.put(Material.SPRUCE_DOOR, Material.SPRUCE_TRAPDOOR);
        doorToTrapdoorMap.put(Material.BIRCH_DOOR, Material.BIRCH_TRAPDOOR);
        doorToTrapdoorMap.put(Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR);
        doorToTrapdoorMap.put(Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR);
        doorToTrapdoorMap.put(Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR);
        doorToTrapdoorMap.put(Material.MANGROVE_DOOR, Material.MANGROVE_TRAPDOOR);
        doorToTrapdoorMap.put(Material.CHERRY_DOOR, Material.CHERRY_TRAPDOOR);
        doorToTrapdoorMap.put(Material.BAMBOO_DOOR, Material.BAMBOO_TRAPDOOR);
        doorToTrapdoorMap.put(Material.CRIMSON_DOOR, Material.CRIMSON_TRAPDOOR);
        doorToTrapdoorMap.put(Material.WARPED_DOOR, Material.WARPED_TRAPDOOR);
        doorToTrapdoorMap.put(Material.IRON_DOOR, Material.IRON_TRAPDOOR);

        coralMapped.add(Tuple.of(Material.TUBE_CORAL_BLOCK, Material.DEAD_TUBE_CORAL_BLOCK));
        coralMapped.add(Tuple.of(Material.BUBBLE_CORAL_BLOCK, Material.DEAD_BUBBLE_CORAL_BLOCK));
        coralMapped.add(Tuple.of(Material.BRAIN_CORAL_BLOCK, Material.DEAD_BRAIN_CORAL_BLOCK));
        coralMapped.add(Tuple.of(Material.FIRE_CORAL_BLOCK, Material.DEAD_FIRE_CORAL_BLOCK));
        coralMapped.add(Tuple.of(Material.HORN_CORAL_BLOCK, Material.DEAD_HORN_CORAL_BLOCK));
    }

    @SuppressWarnings("all")
    private void createItems() {
        if (!perType.isEmpty()) {
            throw new IllegalStateException("special items already created");
        }

        // Nature
        create(Type.NATURE, 10, Material.SUNFLOWER, Material.GREEN_CONCRETE, "Sunflower Flower", true);
        create(Type.NATURE, 19, Material.SUNFLOWER, Material.GREEN_CONCRETE, "Sunflower Stem", false);

        create(Type.NATURE, 11, Material.ROSE_BUSH, Material.MAGENTA_CONCRETE, "Rose Bush", true);
        create(Type.NATURE, 20, Material.ROSE_BUSH, Material.MAGENTA_CONCRETE, "Rose Stem", false);

        create(Type.NATURE, 12, Material.LILAC, Material.PINK_CONCRETE, "Lilac Flower", true);
        create(Type.NATURE, 21, Material.LILAC, Material.PINK_CONCRETE, "Lilac Stem", false);

        create(Type.NATURE, 13, Material.PEONY, Material.PINK_GLAZED_TERRACOTTA, "Peony Flower", true);
        create(Type.NATURE, 22, Material.PEONY, Material.PINK_GLAZED_TERRACOTTA, "Peony Stem", false);

        create(Type.NATURE, 14, Material.TALL_GRASS, Material.GREEN_WOOL, "Tall Grass Top", true);
        create(Type.NATURE, 23, Material.TALL_GRASS, Material.GREEN_WOOL, "Tall Grass Bottom", false);

        create(Type.NATURE, 15, Material.LARGE_FERN, Material.GREEN_GLAZED_TERRACOTTA, "Large Fern Top", true);
        create(Type.NATURE, 24, Material.LARGE_FERN, Material.GREEN_GLAZED_TERRACOTTA, "Large Fern Bottom", false);

        create(Type.NATURE, 16, Material.PITCHER_PLANT, Material.SNIFFER_EGG, "Pitcher Plant Top", true);
        create(Type.NATURE, 25, Material.PITCHER_PLANT, Material.SNIFFER_EGG, "Pitcher Plant Bottom", false);

        create(
                Type.NATURE,
                39,
                Material.CACTUS,
                Material.GREEN_CONCRETE,
                "Cactus",
                (self, event) -> self.setBlock(event.getBlockReplacedState(), Material.CACTUS)
        );

        create(Type.NATURE, 41, Material.DEAD_BUSH, Material.SOUL_SAND, "Dead Bush");

        create(Type.NATURE, 36, Material.BROWN_MUSHROOM, Material.BROWN_MUSHROOM_BLOCK, "Brown Mushroom");
        create(Type.NATURE, 37, Material.RED_MUSHROOM, Material.RED_MUSHROOM_BLOCK, "Red Mushroom");
        create(Type.NATURE, 43, Material.CRIMSON_FUNGUS, Material.NETHER_WART_BLOCK, "Crimson Fungus");
        create(Type.NATURE, 44, Material.WARPED_FUNGUS, Material.WARPED_WART_BLOCK, "Warped Fungus");

        // Create Doors
        doors:
        {
            int slot = 10;

            for (Material door : doorToTrapdoorMap.keySet()) {
                final Material trapdoor = doorToTrapdoorMap.get(door);
                create(
                        Type.DOOR,
                        slot,
                        door,
                        trapdoor,
                        Chat.capitalize(door.name()) + " Top",
                        1,
                        (self, event) -> self.setDoor(event, door, Bisected.Half.TOP),
                        true
                );

                create(
                        Type.DOOR,
                        slot + 9,
                        door,
                        trapdoor,
                        Chat.capitalize(door.name()) + " Bottom",
                        1,
                        (self, event) -> self.setDoor(event, door, Bisected.Half.BOTTOM),
                        false
                );

                ++slot;

                if (slot == 17) {
                    slot = 29;
                }
            }
        }

        // Create Snow Layers
        snow_layers:
        {
            int slot = 11;
            final Snow snowData = (Snow) Bukkit.createBlockData(Material.SNOW);
            for (int i = snowData.getMinimumLayers(); i <= snowData.getMaximumLayers(); i++) {
                final int pos = i;
                create(
                        Type.SNOW,
                        slot,
                        Material.SNOW,
                        Material.SNOW_BLOCK,
                        "Snow Layer " + pos,
                        pos,
                        (self, event) -> self.setLevelled(event, Material.SNOW, pos),
                        false
                );

                slot += (slot % 9 == 6) ? 6 : 1;
            }
        }

        fluids:
        {
            int slot = 11;
            for (int i = 0; i < 8; i++) {
                final int plusOne = i + 1;
                create(
                        Type.FLUID_WATER,
                        slot,
                        Material.WATER_BUCKET,
                        Material.BLUE_CONCRETE,
                        "Levelled Water " + plusOne,
                        plusOne,
                        (self, event) -> self.setLevelled(event, Material.WATER, plusOne - 1),
                        false
                );
                create(
                        Type.FLUID_LAVA,
                        slot,
                        Material.LAVA_BUCKET,
                        Material.ORANGE_CONCRETE,
                        "Levelled Lava " + plusOne,
                        plusOne,
                        (self, event) -> self.setLevelled(event, Material.LAVA, plusOne - 1),
                        false
                );
                slot += (slot == 15 ? 6 : 1);
            }
        }

        // Create corals
        corals:
        {
            int row = 0;
            for (Tuple<Material, Material> coral : coralMapped) {
                int slot = 2;

                final Material coralBlock = coral.a();
                final Material coralBlockDead = coral.b();

                create(
                        Type.CORAL,
                        slot + (9 * row),
                        coralBlock,
                        slot > 3 ? coralBlockDead : coralBlock,
                        Chat.capitalize(coralBlock),
                        1,
                        (self, event) -> {
                            final BlockData data = coralBlock.createBlockData();

                            if (data instanceof Waterlogged waterlogged) {
                                waterlogged.setWaterlogged(false);
                            }
                            self.setBlock(event.getBlockReplacedState(), data);
                        },
                        false
                );

                slot += slot == 3 ? 2 : 1;
                row++;
            }
        }
    }

    private void create(Type type, int slot, Material icon, Material block, String name) {
        create(type, slot, icon, block, name, false);
    }

    private void create(Type type, int slot, Material icon, Material block, String name, boolean glow) {
        create(type, slot, icon, block, name, 1, null, glow);
    }

    private void create(Type type, int slot, Material icon, Material block, String name, BiConsumer<SpecialBlock, BlockPlaceEvent> event) {
        create(type, slot, icon, block, name, 1, event, false);
    }

    private void create(Type type, int slot, Material icon, Material block, String name, String id, int amount, BiConsumer<SpecialBlock, BlockPlaceEvent> event, boolean glow) {
        final SpecialBlock specialBlock = new SpecialBlock(type, slot, icon, block, name, id, amount, glow) {
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

}

package me.hapyl.mmu3.feature.statechanger;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.utils.Enums;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StateChangerGUI extends PlayerGUI {

    private final Data data;

    public StateChangerGUI(Player player, String name, Data data) {
        super(player, name, 6);
        this.data = data;
        setBottomPanel();
        updateMenu();
    }

    private void setBottomPanel() {
        fillItem(45, getSize() - 1, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&0").build());

        setItem(49, new ItemBuilder(Material.BARRIER).setName("&aClose Menu").build(), Player::closeInventory);

        // Restore
        setItem(
                52,
                new ItemBuilder(Material.COMMAND_BLOCK)
                        .setName("&aRestore Block")
                        .setSmartLore("Restores blocks to it's original form, before opening the menu.")
                        .build(),
                player -> {
                    data.restoreOriginalBlockData();
                    player.closeInventory();
                    Message.success(player, "Restored.");
                }
        );

        this.setItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&aNo Modifier").build(), 10, 19, 28, 16, 25, 34);
    }

    public void updateMenu() {
        final Block block = data.getBlock();
        final Material type = block.getType();
        final String blockName = Chat.capitalize(type);
        final BlockData blockRawData = data.getBlockData();

        // Waterlogged
        if (blockRawData instanceof Waterlogged blockData) {
            final boolean waterlogged = blockData.isWaterlogged();

            setItem(
                    28,
                    new ItemBuilder(waterlogged ? Material.WATER_BUCKET : Material.BUCKET)
                            .setName((waterlogged ? "&a" : "&c") + "Waterlogged")
                            .addSmartLore("Whether this block has fluid in it")
                            .build()
            );

            setClick(28, player -> applyState(blockData, d -> d.setWaterlogged(!waterlogged)));

        }

        if (blockRawData instanceof Fence blockData) {
            final Set<BlockFace> allowedFaces = blockData.getAllowedFaces();
            for (BlockFace face : allowedFaces) {
                if (!StateConstants.FACE_SLOT_MAP.containsKey(face)) {
                    continue;
                }

                final int slot = StateConstants.FACE_SLOT_MAP.get(face);
                final boolean enabled = blockData.hasFace(face);
                final String faceName = Chat.capitalize(face);

                setItem(
                        slot,
                        new ItemBuilder(type)
                                .setName((enabled ? "&a" : "&c") + faceName)
                                .setSmartLore("Whether block is connected to the &l%s&7.".formatted(faceName))
                                .predicate(enabled, ItemBuilder::glow)
                                .build()
                );

                setClick(slot, player -> {
                    applyState(blockData, d -> d.setFace(face, !enabled));
                });
            }
        }

        if (blockRawData instanceof Wall blockData) {

            final BlockFace[] allowedFaces = { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };
            final boolean isUp = blockData.isUp();

            setItem(
                    22,
                    new ItemBuilder(type)
                            .setName((isUp ? "&a" : "&c") + "Is Up")
                            .addSmartLore("Whether the well has a center post.")
                            .predicate(isUp, ItemBuilder::glow)
                            .build()
            );

            setClick(22, (c) -> applyState(blockData, d -> d.setUp(!isUp)));

            for (BlockFace face : allowedFaces) {
                if (!StateConstants.FACE_SLOT_MAP.containsKey(face)) {
                    continue;
                }

                final int slot = StateConstants.FACE_SLOT_MAP.get(face);
                final Wall.Height height = blockData.getHeight(face);
                final String faceName = Chat.capitalize(face);

                final ItemBuilder builder = new ItemBuilder(type)
                        .setName("&a" + faceName)
                        .setSmartLore("The different heights a face of a wall may have.");

                switchAddLore(slot, Wall.Height.values(), height, builder);
                switchAddClick(slot, blockData, Wall.Height.values(), height, (d, h) -> d.setHeight(face, h));

            }
        }

        // Slabs
        if (blockRawData instanceof Slab blockData) {
            final Slab.Type slabType = blockData.getType();
            final ItemBuilder builder = new ItemBuilder(type)
                    .setName("&aSlab Type")
                    .addSmartLore("Represents what state the slab is in - either top, bottom, or a double slab.");

            switchAddLore(22, Slab.Type.values(), slabType, builder);
            switchAddClick(22, blockData, Slab.Type.values(), slabType, Slab::setType);

        }

        // Bisected
        if (blockRawData instanceof Bisected blockData) {
            final Bisected.Half half = blockData.getHalf();

            setItem(
                    10,
                    new ItemBuilder(type)
                            .setName("&a" + (half == Bisected.Half.TOP ? "Top" : "Bottom"))
                            .setSmartLore("Denotes which half of a two block tall material this block is.")
                            .build()
            );

            setClick(10, player -> {
                applyState(blockData, d -> d.setHalf(half == Bisected.Half.TOP ? Bisected.Half.BOTTOM : Bisected.Half.TOP));
            });
        }

        // Directional
        if (blockRawData instanceof Directional blockData) {
            final BlockFace facing = blockData.getFacing();

            for (BlockFace face : blockData.getFaces()) {
                if (!StateConstants.FACE_SLOT_MAP.containsKey(face)) {
                    continue;
                }

                final boolean isFacing = face == facing;
                final String faceName = Chat.capitalize(face);
                final int slot = StateConstants.FACE_SLOT_MAP.get(face);

                setItem(
                        slot,
                        new ItemBuilder(type)
                                .setName((isFacing ? "&a" : "&c") + faceName)
                                .addSmartLore("Whenever block is pointing to the &l%s&7.".formatted(faceName))
                                .predicate(isFacing, ItemBuilder::glow)
                                .build()
                );

                if (isFacing) {
                    continue;
                }

                setClick(slot, player -> applyState(blockData, d -> d.setFacing(face)));
            }
        }

        // Stairs
        if (blockRawData instanceof Stairs blockData) {
            final Stairs.Shape shape = blockData.getShape();

            final ItemBuilder builder = new ItemBuilder(type)
                    .setName("&aStairs Shape")
                    .addSmartLore("Represents the texture and bounding box shape of these stairs.");

            switchAddLore(16, Stairs.Shape.values(), shape, builder);
            switchAddClick(16, blockData, Stairs.Shape.values(), shape, Stairs::setShape);

        }

        // MultipleFacing
        if (blockRawData instanceof MultipleFacing blockData) {
            for (BlockFace face : blockData.getAllowedFaces()) {
                if (!StateConstants.FACE_SLOT_MAP.containsKey(face)) {
                    continue;
                }

                final int slot = StateConstants.FACE_SLOT_MAP.get(face);
                final String faceName = Chat.capitalize(face);
                final boolean hasFace = blockData.hasFace(face);

                setItem(
                        slot,
                        new ItemBuilder(type)
                                .setName((hasFace ? "&a" : "&c") + faceName)
                                .setSmartLore("Whether block is connected to the &l%s&7.".formatted(faceName))
                                .predicate(hasFace, ItemBuilder::glow)
                                .build()
                );

                setClick(slot, player -> applyState(blockData, d -> d.setFace(face, !hasFace)));
            }
        }

        // Ageable
        if (blockRawData instanceof Ageable blockData) {
            final int age = blockData.getAge();
            final int maximumAge = blockData.getMaximumAge();

            final ItemBuilder builder = new ItemBuilder(Material.WHEAT_SEEDS)
                    .setName("&aAge")
                    .addSmartLore("Represents the different growth stages that a crop-like block can go through.");

            levelableAddLore(19, "Age", age, maximumAge, builder);
            levelableAddClick(19, blockData, age, maximumAge, Ageable::setAge);
        }

        // Bamboo
        if (blockRawData instanceof Bamboo blockData) {
            final Bamboo.Leaves leaves = blockData.getLeaves();
            int slot = 21;

            for (Bamboo.Leaves value : Bamboo.Leaves.values()) {
                setItem(
                        slot,
                        new ItemBuilder(Material.JUNGLE_LEAVES)
                                .setName("&a%s Leaves", Chat.capitalize(value))
                                .setAmount(slot - 20)
                                .predicate(leaves.equals(value), ItemBuilder::glow)
                                .setSmartLore("Represents the size of the leaves on this bamboo block.")
                                .build()
                );


                if (!leaves.equals(value)) {
                    setClick(slot, player -> applyState(blockData, d -> d.setLeaves(value)));
                }

                ++slot;
            }
        }

        // Bed
        if (blockRawData instanceof Bed blockData) {
            final Bed.Part part = blockData.getPart();

            setItem(
                    25,
                    new ItemBuilder(type)
                            .setName("&a%s Part", Chat.capitalize(part))
                            .setSmartLore("Denotes which half of the bed this block corresponds to.")
                            .build()
            );

            setClick(25, player -> applyState(blockData, d -> d.setPart(part == Bed.Part.HEAD ? Bed.Part.FOOT : Bed.Part.HEAD)));
        }

        // Rails
        if (blockRawData instanceof Rail blockData) {
            final Rail.Shape shape = blockData.getShape();
            final Rail.Shape[] allowedTypes = blockData.getShapes().toArray(new Rail.Shape[] {});

            final ItemBuilder builder = new ItemBuilder(type).setName("&aShape").addSmartLore("Represents the current layout of a rail.");

            switchAddLore(34, allowedTypes, shape, builder);
            switchAddClick(34, blockData, allowedTypes, shape, Rail::setShape);

        }

        // Redstone Power
        if (blockRawData instanceof AnaloguePowerable blockData) {
            final int power = blockData.getPower();
            final int maxPower = blockData.getMaximumPower();

            final ItemBuilder builder = new ItemBuilder(Material.REDSTONE)
                    .setName("&aRedstone Power")
                    .addSmartLore("Represents the redstone power level currently being emitted or transmitted via this block.");

            levelableAddLore(10, "Power", power, maxPower, builder);
            levelableAddClick(10, blockData, power, maxPower, AnaloguePowerable::setPower);
        }

        // Honey Level
        if (blockRawData instanceof Beehive blockData) {
            final int honeyLevel = blockData.getHoneyLevel();
            final int maximumHoneyLevel = blockData.getMaximumHoneyLevel();

            final ItemBuilder builder = new ItemBuilder(Material.HONEYCOMB)
                    .setName("&aHoney Level")
                    .addSmartLore("Represents the amount of honey stored in the hive.");

            levelableAddLore(10, "Honey", honeyLevel, maximumHoneyLevel, builder);
            levelableAddClick(10, blockData, honeyLevel, maximumHoneyLevel, Beehive::setHoneyLevel);
        }

        // Bell
        if (blockRawData instanceof Bell blockData) {
            final Bell.Attachment attachment = blockData.getAttachment();

            final ItemBuilder builder = new ItemBuilder(Material.BELL)
                    .setName("&aAttachment")
                    .setSmartLore("Denotes how the bell is attached to its block.");

            switchAddLore(10, Bell.Attachment.values(), attachment, builder);
            switchAddClick(10, blockData, Bell.Attachment.values(), attachment, Bell::setAttachment);
        }

        // Big Dripleaf
        if (blockRawData instanceof BigDripleaf blockData) {
            final BigDripleaf.Tilt tilt = blockData.getTilt();

            final ItemBuilder builder = new ItemBuilder(Material.BIG_DRIPLEAF)
                    .setName("&aTilt")
                    .setSmartLore("Indicates how far the leaf is tilted.");

            switchAddLore(10, BigDripleaf.Tilt.values(), tilt, builder);
            switchAddClick(10, blockData, BigDripleaf.Tilt.values(), tilt, BigDripleaf::setTilt);
        }

        // Cake
        if (blockRawData instanceof Cake blockData) {
            final int bites = blockData.getBites();
            final int maximumBites = blockData.getMaximumBites();

            final ItemBuilder builder = new ItemBuilder(Material.CAKE)
                    .setName("&aBites")
                    .setSmartLore("Represents the amount of bites which have been taken from this slice of cake.");

            levelableAddLore(10, "Bites", bites, maximumBites, builder);
            levelableAddClick(10, blockData, bites, maximumBites, Cake::setBites);
        }

        // Cave Vines
        if (blockRawData instanceof CaveVines blockData) {
            final boolean hasBerries = blockData.isBerries();

            setItem(
                    10,
                    new ItemBuilder(Material.GLOW_BERRIES)
                            .setName((hasBerries ? "&a" : "&c") + "Berries")
                            .addSmartLore("Indicates whether the block has berries.")
                            .build()
            );

            setClick(10, player -> applyState(blockData, d -> d.setBerries(!hasBerries)));
        }

        // Farmland
        if (blockRawData instanceof Farmland blockData) {
            final int moisture = blockData.getMoisture();
            final int maximumMoisture = blockData.getMaximumMoisture();

            final ItemBuilder builder = new ItemBuilder(Material.POTION)
                    .setName("&a&Moisture")
                    .setSmartLore(
                            "Indicates how close it is to a water source (if any). A higher moisture level leads, to faster growth of crops on this block.");

            levelableAddLore(10, "Moisture", moisture, maximumMoisture, builder);
            levelableAddClick(10, blockData, moisture, maximumMoisture, Farmland::setMoisture);
        }

        // Gate
        if (blockRawData instanceof Gate blockData) {
            final boolean inWall = blockData.isInWall();

            setItem(
                    10,
                    new ItemBuilder(type)
                            .setName((inWall ? "&a" : "&c") + "In Wall")
                            .addSmartLore(
                                    "indicates if the fence gate is attached to a wall, and if true the texture is lowered by a small amount to blend in better.")
                            .build()
            );

            setClick(10, player -> applyState(blockData, d -> d.setInWall(!inWall)));
        }

        // Levelled
        if (blockRawData instanceof Levelled blockData) {
            final int level = blockData.getLevel();
            final int maximumLevel = blockData.getMaximumLevel();

            final ItemBuilder builder = new ItemBuilder(Material.RAIL)
                    .setName("&aevel")
                    .setSmartLore(
                            "Represents the amount of fluid contained within this block, either by itself or inside a cauldron. In the case of water and lava blocks the levels have special meanings: a level of 0 corresponds to a source block, 1-7 regular fluid heights, and 8-15 to \"falling\" fluids. All falling fluids have the same behaviour, but the level corresponds to that of the block above them.")
                    .addSmartLore(
                            "Note that counterintuitively, an adjusted level of 1 is the highest level, whilst 7 is the lowest.",
                            "&7&o"
                    );
            levelableAddLore(10, "Level", level, maximumLevel, builder);
            levelableAddClick(10, blockData, level, maximumLevel, Levelled::setLevel);
        }

        // Piston
        if (blockRawData instanceof Piston blockData) {
            final boolean extended = blockData.isExtended();
            setItem(
                    10,
                    new ItemBuilder(type)
                            .setName((extended ? "&a" : "&c") + "Extended")
                            .setSmartLore("Denotes whether the piston head is currently extended or not.")
                            .predicate(extended, ItemBuilder::glow)
                            .build(),
                    player -> applyState(blockData, d -> d.setExtended(!extended))
            );
        }

        // Piston Head
        if (blockRawData instanceof PistonHead blockData) {
            final boolean isShort = blockData.isShort();
            setItem(
                    10,
                    new ItemBuilder(Material.PISTON)
                            .setName((isShort ? "&a" : "&c") + "Short")
                            .setSmartLore("Denotes this piston head is shorter than the usual amount because it is currently retracting.")
                            .predicate(isShort, ItemBuilder::glow)
                            .build(),
                    player -> applyState(blockData, d -> d.setShort(!isShort))
            );
        }

        if (blockRawData instanceof PointedDripstone blockData) {
            final PointedDripstone.Thickness thickness = blockData.getThickness();
            final BlockFace verticalDirection = blockData.getVerticalDirection();

            for (BlockFace face : blockData.getVerticalDirections()) {
                if (!StateConstants.FACE_SLOT_MAP.containsKey(face)) {
                    continue;
                }

                final String faceName = Chat.capitalize(face);
                final boolean isFacing = face == verticalDirection;
                final int slot = StateConstants.FACE_SLOT_MAP.get(face);

                setItem(
                        slot,
                        new ItemBuilder(type)
                                .setName((isFacing ? "&a" : "&c") + faceName)
                                .addSmartLore("Represents the dripstone orientation.")
                                .predicate(isFacing, ItemBuilder::glow)
                                .build()
                );

                if (!isFacing) {
                    setClick(slot, player -> applyState(blockData, d -> d.setVerticalDirection(face)));
                }
            }

            final ItemBuilder builder = new ItemBuilder(Material.DRIPSTONE_BLOCK)
                    .setName("&aThickness")
                    .setSmartLore(
                            "Represents the thickness of the dripstone, corresponding to its position within a multi-block dripstone formation.");

            switchAddLore(10, PointedDripstone.Thickness.values(), thickness, builder);
            switchAddClick(10, blockData, PointedDripstone.Thickness.values(), thickness, PointedDripstone::setThickness);
        }

        if (blockRawData instanceof RedstoneWire blockData) {
            for (BlockFace face : blockData.getAllowedFaces()) {
                if (!StateConstants.FACE_SLOT_MAP.containsKey(face)) {
                    continue;
                }

                final RedstoneWire.Connection connection = blockData.getFace(face);
                final int slot = StateConstants.FACE_SLOT_MAP.get(face);
                final String faceName = Chat.capitalize(face);

                switchAddLore(
                        slot/*powerable takes slot 10*/,
                        RedstoneWire.Connection.values(),
                        connection,
                        new ItemBuilder(Material.REDSTONE)
                                .setName("&a" + faceName)
                                .addSmartLore("The way in which a redstone wire can connect to an adjacent block face.")
                );

                switchAddClick(slot, blockData, RedstoneWire.Connection.values(), connection, (d, c) -> d.setFace(face, c));
            }
        }

        if (blockRawData instanceof Rotatable blockData) {
            final BlockFace rotation = blockData.getRotation();

            switchAddLore(
                    22,
                    StateConstants.FLOOR_SIGN_VALID_FACES,
                    rotation,
                    new ItemBuilder(type).setName("&aRotation").setSmartLore("Represents the current rotation of this block.")
            );

            switchAddClick(22, blockData, StateConstants.FLOOR_SIGN_VALID_FACES, rotation, Rotatable::setRotation);
        }

        if (blockRawData instanceof Sapling blockData) {
            final int stage = blockData.getStage();
            final int maximumStage = blockData.getMaximumStage();

            levelableAddLore(
                    22,
                    "Age",
                    stage,
                    maximumStage,
                    new ItemBuilder(type)
                            .setName("&aSapling Age")
                            .setSmartLore(
                                    "Represents the growth stage of a sapling. When the sapling reaches %s it will attempt to grow into a tree as the next stage.".formatted(
                                            maximumStage))
            );

            levelableAddClick(22, blockData, stage, maximumStage, Sapling::setStage);
        }

        if (blockRawData instanceof Scaffolding blockData) {
            final int distance = blockData.getDistance();
            final int maximumDistance = blockData.getMaximumDistance();
            final boolean bottom = blockData.isBottom();

            setItem(
                    21,
                    new ItemBuilder(Material.SPRUCE_SLAB)
                            .setName((bottom ? "&a" : "&c") + "Bottom")
                            .setSmartLore("Indicates whether the scaffolding is floating or not.")
                            .predicate(bottom, ItemBuilder::glow)
                            .build(),
                    player -> applyState(blockData, d -> d.setBottom(!bottom))
            );

            levelableAddLore(
                    23,
                    "Distance",
                    distance,
                    maximumDistance,
                    new ItemBuilder(Material.SCAFFOLDING)
                            .setName("&aDistance")
                            .setSmartLore(
                                    "Indicates the distance from a scaffolding block placed above a 'bottom' scaffold. When 'distance' reaches %s the block will drop.".formatted(
                                            maximumDistance))
            );

            levelableAddClick(23, blockData, distance, maximumDistance, Scaffolding::setDistance);
        }

        if (blockRawData instanceof SculkSensor blockData) {
            final SculkSensor.Phase phase = blockData.getPhase();
            switchAddLore(
                    22,
                    SculkSensor.Phase.values(),
                    phase,
                    new ItemBuilder(Material.SCULK_SENSOR)
                            .setName("Phase")
                            .setSmartLore("Indicates the current operational phase of the sensor.")
            );
            switchAddClick(22, blockData, SculkSensor.Phase.values(), phase, SculkSensor::setPhase);
        }

        if (blockRawData instanceof Snow blockData) {
            final int layers = blockData.getLayers();
            final int maximumLayers = blockData.getMaximumLayers();

            levelableAddLore(
                    22,
                    "Layer",
                    layers,
                    maximumLayers,
                    new ItemBuilder(Material.SNOW)
                            .setName("&aSnow Layers")
                            .addSmartLore("Represents the amount of layers of snow which are present in this block.")
            );
            levelableAddClick(22, blockData, blockData.getMinimumLayers(), layers, maximumLayers, Snow::setLayers);
        }

        if (blockRawData instanceof Snowable blockData) {
            final boolean snowy = blockData.isSnowy();

            setItem(
                    22,
                    new ItemBuilder(type)
                            .setName((snowy ? "&a" : "&c") + "Snowy")
                            .setSmartLore(
                                    "Denotes whether this block has a snow covered side and top texture (normally because the block above is snow).")
                            .predicate(snowy, ItemBuilder::glow)
                            .build(), player -> applyState(blockData, d -> d.setSnowy(!snowy))
            );

        }

        // Open Inventory
        openInventory();
    }

    /////////////
    // Helpers //
    /////////////
    private void setItem(ItemStack item, int... slots) {
        for (int slot : slots) {
            setItem(slot, item);
        }
    }

    private void levelableAddLore(int slot, String name, int currentLeve, int maxLevel, ItemBuilder builder) {
        builder
                .addLore()
                .addLore("&bCurrent %s: &l%s".formatted(name, currentLeve))
                .addLore("&bMaximum %s: &l%s".formatted(name, maxLevel))
                .addLore()
                .addLore("&eLeft Click to increase %s.".formatted(name.toLowerCase(Locale.ROOT)))
                .addLore("&eRight Click to decrease %s.".formatted(name.toLowerCase(Locale.ROOT)));

        setItem(slot, builder.build());
    }

    private <T extends BlockData> void levelableAddClick(int slot, T t, int level, int maxLevel, BiConsumer<T, Integer> consumer) {
        levelableAddClick(slot, t, 0, level, maxLevel, consumer);
    }

    private <T extends BlockData> void levelableAddClick(int slot, T t, int minLevel, int level, int maxLevel, BiConsumer<T, Integer> consumer) {
        setClick(slot, player -> applyState(t, d -> consumer.accept(d, level + 1 > maxLevel ? minLevel : level + 1)), ClickType.LEFT);
        setClick(slot, player -> applyState(t, d -> consumer.accept(d, level - 1 < minLevel ? maxLevel : level - 1)), ClickType.RIGHT);
    }

    private <T> void switchAddLore(int slot, T[] values, T value, ItemBuilder builder) {
        builder.addLore();

        for (T t : values) {
            builder.addLore("&b" + (value.equals(t) ? " ➥ &l" : "") + Chat.capitalize(t.toString()));
        }

        builder.addLore().addLore("&eLeft Click to switch forward.").addLore("&eRight Click to switch backwards.");
        setItem(slot, builder.build());
    }

    private <T extends BlockData, V> void switchAddClick(int slot, T blockData, V[] values, V currentValue, BiConsumer<T, V> consumer) {
        setClick(slot, player -> applyState(blockData, d -> {
            final V nextValue = Enums.getNextValue(values, currentValue);
            consumer.accept(d, nextValue);
        }), ClickType.LEFT);

        setClick(slot, player -> applyState(blockData, d -> {
            final V nextValue = Enums.getPreviousValue(values, currentValue);
            consumer.accept(d, nextValue);
        }), ClickType.RIGHT);
    }

    private <T extends BlockData> void applyState(T t, Consumer<T> consumer) {
        final Block block = data.getBlock();
        consumer.accept(t);
        block.setBlockData(t, false);
        playPlingSound();
    }

    private void playPlingSound() {
        PlayerLib.playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        updateMenu();
    }

}

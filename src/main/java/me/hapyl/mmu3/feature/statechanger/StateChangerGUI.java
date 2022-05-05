package me.hapyl.mmu3.feature.statechanger;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.inventory.gui.GUI;
import kz.hapyl.spigotutils.module.inventory.gui.GUIClick;
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
                        .build()
        );

        setClick(52, player -> {
            data.restoreOriginalBlockData();
            player.closeInventory();
            Message.success(player, "Restored.");
        });

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
                            .setName((waterlogged ? "&a&l" : "&c&l") + "Waterlogged")
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
                                .setName((enabled ? "&a&l" : "&c&l") + faceName)
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
                            .setName((isUp ? "&a&l" : "&c&l") + "Is Up")
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
                        .setName("&a&l" + faceName)
                        .setSmartLore("The different heights a face of a wall may have.")
                        .addLore();

                switchAddLore(slot, Wall.Height.values(), height, builder);
                switchAddClick(slot, blockData, Wall.Height.values(), height, (d, h) -> d.setHeight(face, h));

            }
        }

        // Slabs
        if (blockRawData instanceof Slab blockData) {
            final Slab.Type slabType = blockData.getType();
            final ItemBuilder builder = new ItemBuilder(type)
                    .setName("&aSlab Type")
                    .addSmartLore("Represents what state the slab is in - either top, bottom, or a double slab.")
                    .addLore();

            switchAddLore(22, Slab.Type.values(), slabType, builder);
            switchAddClick(22, blockData, Slab.Type.values(), slabType, Slab::setType);

        }

        // Bisected
        if (blockRawData instanceof Bisected blockData) {
            final Bisected.Half half = blockData.getHalf();

            setItem(
                    10,
                    new ItemBuilder(type)
                            .setName("&a&l" + (half == Bisected.Half.TOP ? "Top" : "Bottom"))
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
                                .setName((isFacing ? "&a&l" : "&c&l") + faceName)
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
                    .setName("&a&lStairs Shape")
                    .addSmartLore("Represents the texture and bounding box shape of these stairs.")
                    .addLore();

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
                                .setName((hasFace ? "&a&l" : "&c&l") + faceName)
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
                    .setName("&a&lAge")
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
                                .setName("&a&l%s Leaves", Chat.capitalize(value))
                                .setAmount(slot - 20)
                                .setSmartLore("Represents the size of the leaves on this bamboo block.")
                                .build()
                );

                ++slot;

                if (leaves == value) {
                    continue;
                }

                setClick(slot, player -> applyState(blockData, d -> d.setLeaves(value)));
            }
        }

        // Bed
        if (blockRawData instanceof Bed blockData) {
            final Bed.Part part = blockData.getPart();

            setItem(
                    25,
                    new ItemBuilder(type)
                            .setName("&a&l%s Part", Chat.capitalize(part))
                            .setSmartLore("Denotes which half of the bed this block corresponds to.")
                            .build()
            );

            setClick(25, player -> applyState(blockData, d -> d.setPart(part == Bed.Part.HEAD ? Bed.Part.FOOT : Bed.Part.HEAD)));
        }

        // Rails
        if (blockRawData instanceof Rail blockData) {
            final Rail.Shape shape = blockData.getShape();
            final Rail.Shape[] allowedTypes = blockData.getShapes().toArray(new Rail.Shape[] {});

            final ItemBuilder builder = new ItemBuilder(type)
                    .setName("&a&lShape")
                    .addSmartLore("Represents the current layout of a rail.")
                    .addLore();

            switchAddLore(34, allowedTypes, shape, builder);
            switchAddClick(34, blockData, allowedTypes, shape, Rail::setShape);

        }

        // Redstone Power
        if (blockRawData instanceof AnaloguePowerable blockData) {
            final int power = blockData.getPower();
            final int maxPower = blockData.getMaximumPower();

            final ItemBuilder builder = new ItemBuilder(Material.REDSTONE)
                    .setName("&a&lRedstone Power")
                    .addSmartLore("Represents the redstone power level currently being emitted or transmitted via this block.");

            levelableAddLore(10, "Power", power, maxPower, builder);
            levelableAddClick(10, blockData, power, maxPower, AnaloguePowerable::setPower);
        }

        // Honey Level
        if (blockRawData instanceof Beehive blockData) {
            final int honeyLevel = blockData.getHoneyLevel();
            final int maximumHoneyLevel = blockData.getMaximumHoneyLevel();

            final ItemBuilder builder = new ItemBuilder(Material.HONEYCOMB)
                    .setName("&a&lHoney Level")
                    .addSmartLore("Represents the amount of honey stored in the hive.");

            levelableAddLore(10, "Honey", honeyLevel, maximumHoneyLevel, builder);
            levelableAddClick(10, blockData, honeyLevel, maximumHoneyLevel, Beehive::setHoneyLevel);
        }

        if (blockRawData instanceof Bell blockData) {
            final Bell.Attachment attachment = blockData.getAttachment();

            final ItemBuilder builder = new ItemBuilder(Material.BELL)
                    .setName("&a&lAttachment")
                    .setSmartLore("Denotes how the bell is attached to its block.");

            switchAddLore(10, Bell.Attachment.values(), attachment, builder);
            switchAddClick(10, blockData, Bell.Attachment.values(), attachment, Bell::setAttachment);
        }


        // Open Inventory
        openInventory();
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
        setClick(slot, player -> applyState(t, d -> consumer.accept(d, level + 1 > maxLevel ? 0 : level + 1)), ClickType.LEFT);
        setClick(slot, player -> applyState(t, d -> consumer.accept(d, level - 1 < 0 ? maxLevel : level - 1)), ClickType.RIGHT);
    }

    private <T> void switchAddLore(int slot, T[] values, T value, ItemBuilder builder) {
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

    private enum SwitchType {
        NEXT, PREVIOUS
    }


}

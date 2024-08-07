package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.feature.itemcreator.ColorSignGUI;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.eterna.module.util.ThreadRandom;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ArmorColorSubGUI extends ItemCreatorSubGUI {
    public ArmorColorSubGUI(Player player) {
        super(player, "Dye Color", Size.FIVE, new ItemCreatorGUI(player));
        updateInventory();
    }

    @Override
    public void updateInventory() {
        clearSubGUI();
        final ItemCreator creator = creator();
        final Color color = creator.getOrCreateColor();

        setItem(19, new ItemBuilder(creator.buildFinalItem()).asIcon());

        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        // RGB
        int index = 12;
        for (Type type : Type.values()) {
            setItem(index, ItemBuilder.of(
                    type.material,
                    (type == Type.RED ? "&c&l" : type == Type.GREEN ? "&a&l" : "&b&l") + Chat.capitalize(type),
                    "",
                    "&aCurrent Value: %s".formatted(type == Type.RED ? red : type == Type.GREEN ? green : blue),
                    "",
                    "&eClick to increase by 1",
                    "&6Right Click to decrease by 1",
                    "&cHold Shift to increase/decrease by 10"
            ).asIcon());

            index++;
        }

        // Enter Values
        // HEX
        setItem(30, ItemBuilder.of(Material.SPRUCE_SIGN, "Enter Hex Value", "", "&eClick to enter hexadecimal value").asIcon(), player -> {
            new ColorSignGUI(player) {
                @Override
                public void onResponse(Color color) {
                    creator.setArmorColor(color);
                    Runnables.runLater(() -> updateInventory(), 1);
                }
            };
        });

        // RGB
        setItem(32, ItemBuilder.of(Material.DARK_OAK_SIGN, "Enter RGB Value", "", "&eClick to enter RBG value").asIcon(), player -> {
            new SignGUI(player, "R: ", "G: ", "B: ", "^ Enter RGB ^") {
                @Override
                public void onResponse(Response response) {
                    final String red = response.getString(0, "0");
                    final String green = response.getString(1, "0");
                    final String blue = response.getString(2, "0");

                    creator.setArmorColor(Color.fromRGB(toInt(red), toInt(green), toInt(blue)));
                    response.runSync(() -> updateInventory(), 1L);
                }

                private int toInt(String value) {
                    final String string = value.substring(value.lastIndexOf(":") + 1).trim();
                    return Numbers.clamp(Numbers.getInt(string, 0), 0, 255);
                }
            };
        });

        // Randomize Color
        setItem(
                31,
                ItemBuilder.of(Material.MAGENTA_GLAZED_TERRACOTTA, "Randomize Color", "", "&eClick to randomize color").asIcon(),
                player -> {
                    creator.setArmorColor(Color.fromRGB(
                            ThreadRandom.nextInt(0, 255),
                            ThreadRandom.nextInt(0, 255),
                            ThreadRandom.nextInt(0, 255)
                    ));
                    updateInventory();
                }
        );

        // Reset color
        setItem(25, ItemBuilder.of(Material.WATER_BUCKET, "Reset Color", "", "&eClick to wash your armor").asIcon(), player -> {
            creator.setArmorColor(null);
            new ItemCreatorGUI(player);

            // Fx
            PlayerLib.playSound(player, Sound.ITEM_BUCKET_FILL, 1.0f);
        });

        // Handle color click using event listener
        setEventListener((player, gui, ev) -> {
            final int slot = ev.getRawSlot();
            final ClickType click = ev.getClick();

            workColorClick(slot == 12 ? Type.RED : slot == 13 ? Type.GREEN : Type.BLUE, color, click.isLeftClick(), click.isShiftClick());
        });

        //        PlayerLib.playSound(getPlayer(), Sound.UI_BUTTON_CLICK, 1.0f);
        openInventory();
    }

    private void workColorClick(Type type, Color color, boolean increase, boolean shift) {
        final int value = increase ? (shift ? 10 : 1) : (shift ? -10 : -1);

        // Handle out of bounds values
        switch (type) {
            case RED -> {
                creator().setArmorColor(color.setRed(Numbers.clamp(color.getRed() + value, 0, 255)));
            }
            case GREEN -> {
                creator().setArmorColor(color.setGreen(Numbers.clamp(color.getGreen() + value, 0, 255)));
            }
            case BLUE -> {
                creator().setArmorColor(color.setBlue(Numbers.clamp(color.getBlue() + value, 0, 255)));
            }
        }

        updateInventory();
    }

    private enum Type {
        RED(12, Material.RED_WOOL),
        GREEN(13, Material.GREEN_WOOL),
        BLUE(14, Material.BLUE_WOOL);

        private final int slot;
        private final Material material;

        Type(int slot, Material material) {
            this.slot = slot;
            this.material = material;
        }
    }
}

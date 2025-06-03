package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import me.hapyl.eterna.module.inventory.gui.GUIEventListener;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.ThreadRandom;
import me.hapyl.mmu3.feature.itemcreator.ColorSignGUI;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;

public class ArmorColorSubGUI extends ItemCreatorSubGUI implements GUIEventListener {

    public ArmorColorSubGUI(Player player) {
        super(player, "Dye Color", Size.FIVE, new ItemCreatorGUI(player));

        openInventory();
    }

    @Override
    public void onClick(int slot, @Nonnull InventoryClickEvent event) {
        final Color color = creator().getOrCreateColor();
        final ClickType click = event.getClick();

        workColorClick(slot == 12 ? Type.RED : slot == 13 ? Type.GREEN : Type.BLUE, color, click.isLeftClick(), click.isShiftClick());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final ItemCreator creator = creator();
        final Color color = creator.getOrCreateColor();

        setItem(19, new ItemBuilder(creator.buildFinalItem()).asIcon());

        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        // RGB
        int index = 12;
        for (Type type : Type.values()) {
            final String colorString = type == Type.RED ? "&c" : type == Type.GREEN ? "&a" : "&b";

            setItem(
                    index,
                    new ItemBuilder(type.material)
                            .setName(colorString + "&l" + Chat.capitalize(type))
                            .addLore()
                            .addLore("Current Value: %s%s".formatted(
                                    colorString,
                                    type == Type.RED ? red : type == Type.GREEN ? green : blue
                            ))
                            .addTextBlockLore("""
                                    
                                    &8◦ &eLeft-Click to increase
                                    &8◦ &6Right-Click to decrease
                                    &b&oShift-Click to adjust in larger steps!
                                    """)
                            .asIcon()
            );

            index++;
        }

        // Enter Values
        // HEX
        setItem(
                30,
                new ItemBuilder(Material.SPRUCE_SIGN)
                        .setName("Enter Hex Value")
                        .addTextBlockLore("""
                                Allows to set a #hex color value.
                                
                                &8◦ &eLeft-Click to enter hexadecimal value
                                """)
                        .asIcon(), player -> {
                    new ColorSignGUI(player) {
                        @Override
                        public void onResponse(Color color) {
                            creator.setArmorColor(color);

                            runSync(() -> openInventory());
                        }
                    };
                }
        );

        // RGB
        setItem(
                32,
                new ItemBuilder(Material.DARK_OAK_SIGN)
                        .setName("Enter RGB Value")
                        .addTextBlockLore("""
                                Allows to set RGB color value.
                                
                                &8◦ &eLeft-Click to enter RGB value
                                """)
                        .asIcon(), player -> {
                    new SignGUI(player, "R: ", "G: ", "B: ", "^ Enter RGB ^") {
                        @Override
                        public void onResponse(Response response) {
                            final String red = response.getString(0, "0");
                            final String green = response.getString(1, "0");
                            final String blue = response.getString(2, "0");

                            creator.setArmorColor(Color.fromRGB(toInt(red), toInt(green), toInt(blue)));
                            runSync(() -> openInventory());
                        }

                        private int toInt(String value) {
                            final String string = value.substring(value.lastIndexOf(":") + 1).trim();
                            return Math.clamp(Numbers.getInt(string, 0), 0, 255);
                        }
                    };
                }
        );

        // Randomize Color
        setItem(
                31,
                new ItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA)
                        .setName("Randomize Color")
                                .addTextBlockLore("""
                                        Allows to randomize the color.
                                        
                                        &8◦ &eLeft-Click to randomize
                                        """).asIcon(),
                player -> {
                    creator.setArmorColor(Color.fromRGB(
                            ThreadRandom.nextInt(0, 255),
                            ThreadRandom.nextInt(0, 255),
                            ThreadRandom.nextInt(0, 255)
                    ));
                    openInventory();
                }
        );

        // Reset color
        setItem(25, new ItemBuilder(Material.WATER_BUCKET)
                .setName("Reset Color")
                .addTextBlockLore("""
                                Clears the existing color.
                                
                                &8◦ &eLeft-Click to wash your armor
                                """)
                .asIcon(), player -> {
                    creator.setArmorColor(null);
                    new ItemCreatorGUI(player);

                    // Fx
                    PlayerLib.playSound(player, Sound.ITEM_BUCKET_FILL, 1.0f);
                }
        );
    }

    private void workColorClick(Type type, Color color, boolean increase, boolean shift) {
        final int value = increase ? (shift ? 10 : 1) : (shift ? -10 : -1);

        // Handle out of bounds values
        switch (type) {
            case RED -> {
                creator().setArmorColor(color.setRed(Math.clamp(color.getRed() + value, 0, 255)));
            }
            case GREEN -> {
                creator().setArmorColor(color.setGreen(Math.clamp(color.getGreen() + value, 0, 255)));
            }
            case BLUE -> {
                creator().setArmorColor(color.setBlue(Math.clamp(color.getBlue() + value, 0, 255)));
            }
        }

        openInventory();
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

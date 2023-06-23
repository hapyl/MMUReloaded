package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ColorSignGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TrimGUI extends PanelGUI {

    private static final EquipmentSlot[] APPLICABLE_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private final TrimData data;

    public TrimGUI(Player player) {
        super(player, "Armor Trim Generator", Size.FIVE);

        data = Main.getRegistry().trim.getDataOrCreate(player);

        updateInventory();
    }

    @Override
    public void updateInventory() {
        final PlayerInventory inventory = player.getInventory();
        final EnumTrimPattern pattern = data.getPattern();
        final EnumTrimMaterial material = data.getMaterial();

        // Set armor
        int inventorySlot = 10;

        for (final EquipmentSlot slot : APPLICABLE_SLOTS) {
            final EnumTrimArmor armor = data.getArmor(slot);
            final ItemStack item = armor.createItem(slot, data);

            final ItemBuilder builder = new ItemBuilder(item)
                    .setName(Chat.capitalize(slot))
                    .addLore()
                    .addTextBlockLore("""
                            &b;;Keep in mind that mojang is too lazy to make a trim preview, so it will not be shown in the menu!
                            &3;;Equip the item to see the trim.
                            """)
                    .addLore()
                    .addLore("&eLeft click to get " + Chat.capitalize(slot))
                    .addLore("&6Right click to change the type");

            // If leather armor, add dying ability
            if (armor == EnumTrimArmor.LEATHER) {
                setItem(inventorySlot - 9, ItemBuilder
                        .of(Material.RED_DYE, "Set Color", "&eClick to set armor color.")
                        .asIcon(), click -> new ColorSignGUI(player) {

                    @Override
                    public void onResponse(Color color) {
                        data.setArmorColor(slot, color);
                        Runnables.runLater(TrimGUI.this::updateInventory, 1);
                    }

                });
            }

            setItem(inventorySlot, builder.asIcon());

            setClick(inventorySlot, click -> {
                inventory.addItem(item);
                Message.sound(player, Sound.ENTITY_CHICKEN_EGG, 1.0f);
            }, ClickType.LEFT, ClickType.SHIFT_LEFT);

            setClick(inventorySlot, click -> new TrimDataGUI<>(data, "Select Armor Type", Size.THREE, EnumTrimArmor.values()) {

                @Override
                public void onClick(EnumTrimArmor armor) {
                    data.setArmor(slot, armor);
                    TrimGUI.this.updateInventory();
                }

            }, ClickType.RIGHT, ClickType.SHIFT_RIGHT);

            inventorySlot += 2;
        }

        setItem(30, ItemBuilder.of(pattern.material)
                .setName("Change Pattern")
                .addLore()
                .addLore("Current Pattern: &a&l" + pattern)
                .addLore()
                .addLore("&eClick to change the pattern")
                .asIcon(), click -> new TrimDataGUI<>(data, "Select Pattern", Size.FIVE, EnumTrimPattern.values()) {

            @Override
            public void onClick(EnumTrimPattern pattern) {
                data.setPattern(pattern);
                TrimGUI.this.updateInventory();
            }

        });

        setItem(32, ItemBuilder.of(material.material)
                .setName("Change Material")
                .addLore()
                .addLore("Current Material: &a&l" + material)
                .addLore()
                .addLore("&eClick to chance the material")
                .asIcon(), click -> {
            new TrimDataGUI<>(data, "Select Material", Size.FOUR, EnumTrimMaterial.values()) {

                @Override
                public void onClick(EnumTrimMaterial material) {
                    data.setMaterial(material);
                    TrimGUI.this.updateInventory();
                }

            };
        });

        openInventory();
    }
}

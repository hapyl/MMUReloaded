package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.feature.itemcreator.LinkedAttribute;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class AttributeSubGUI extends ItemCreatorSubGUI {

    public AttributeSubGUI(Player player) {
        super(player, "Attributes", Size.FIVE);
        updateInventory();
        openInventory();
    }

    @Override
    public void updateInventory() {
        clearSubGUI();

        setItem(13, ItemBuilder.of(Material.PRISMARINE_SHARD, "Attributes").build());

        final ItemCreator creator = creator();
        final Map<Attribute, AttributeModifier> attributes = creator.getAttributes();
        final SmartComponent smartComponent = newSmartComponent();

        for (LinkedAttribute value : LinkedAttribute.values()) {
            final Attribute link = value.getLink();
            final ItemBuilder builder = ItemBuilder.of(value.getMaterial())
                    .setName(value.getName())
                    .addLore()
                    .addSmartLore(value.getDescription())
                    .addLore();

            // Contains attribute:
            // - Display values
            // - Add remove button
            if (attributes.containsKey(link)) {
                final AttributeModifier modifier = attributes.get(link);
                final EquipmentSlot slot = modifier.getSlot();

                builder.addLore("Current Values:");
                builder.addLore(" Name: &a%s", modifier.getName());
                builder.addLore(" Slot: &a%s", (slot == null ? "Any" : Chat.capitalize(slot)));
                builder.addLore(" Operation: &a%s", modifier.getOperation());
                builder.addLore(" Amount: &a%s", modifier.getAmount());

                builder.addLore();
                builder.addLore("&eClick to modify");
                builder.addLore("&6Right Click to remove");

                final ItemStack itemStack = builder.glow().asIcon();

                smartComponent.add(itemStack, player -> new AttributeModifySubGUI(player, value), ClickType.LEFT);

                smartComponent.add(itemStack, player -> {
                    attributes.remove(link);
                    updateInventory();
                }, ClickType.RIGHT);

                continue;
            }

            builder.addLore("&eClick to add modifier");
            smartComponent.add(builder.asIcon(), player -> new AttributeModifySubGUI(player, value));
        }

        smartComponent.fillItems(this, SlotPattern.FANCY, 2);

    }
}

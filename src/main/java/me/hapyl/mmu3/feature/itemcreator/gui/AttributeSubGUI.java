package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.PlayerPageGUI;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import me.hapyl.eterna.module.inventory.gui.StrictAction;
import me.hapyl.mmu3.feature.itemcreator.LinkedAttribute;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

public class AttributeSubGUI extends ItemCreatorSubGUI {

    private static final int itemsPerPage = 18;

    private int start;

    public AttributeSubGUI(Player player) {
        super(player, "Attributes", Size.FIVE);

        openInventory();
    }

    public void openInventory(int start) {
        this.start = start;

        super.openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final Map<Attribute, AttributeModifier> attributes = creator().getAttributes();
        final LinkedAttribute[] linkedAttributes = LinkedAttribute.values();

        // Set arrows
        if (start > 1) {
            setItem(18, PlayerPageGUI.ICON_PAGE_PREVIOUS, player -> openInventory(start - itemsPerPage));
        }

        if (start + itemsPerPage < linkedAttributes.length) {
            setItem(26, PlayerPageGUI.ICON_PAGE_NEXT, player -> openInventory(start + itemsPerPage));
        }

        setItem(13, makePreviewItem());

        final SmartComponent smartComponent = newSmartComponent();

        for (int i = start; i < Math.min(start + itemsPerPage, linkedAttributes.length); i++) {
            final LinkedAttribute value = linkedAttributes[i];
            final ItemBuilder builder = new ItemBuilder(value.getMaterial())
                    .setName("" + value.getName())
                    .addTextBlockLore(value.getDescription())
                    .addLore();

            final Attribute attribute = value.getLink();
            final AttributeModifier modifier = attributes.get(attribute);

            // Attribute applied, display
            if (modifier != null) {
                builder.glow();
                builder.addLore("Current Value:");
                builder.addLore("  Key: &f%s".formatted(modifier.getKey().getKey()));
                builder.addLore("  Amount: &e%.1f".formatted(modifier.getAmount()));
                builder.addLore("  Operation: &b%s".formatted(Chat.capitalize(modifier.getOperation())));
                builder.addLore("  Slot: &2%s".formatted(Chat.capitalize(modifier.getSlotGroup().toString())));

                builder.addLore();
                builder.addLore("&8◦ &eLeft-Click to modify");
                builder.addLore("&8◦ &6Right-Click to remove");
            }
            // No attribute
            else {
                builder.addLore("&8◦ &eLeft-Click to add attribute");
            }

            // Strict is just easier
            smartComponent.add(
                    builder.asIcon(), new StrictAction() {
                        @Override
                        public void onLeftClick(@Nonnull Player player) {
                            new AttributeModifySubGUI(player, value, AttributeSubGUI.this);
                        }

                        @Override
                        public void onRightClick(@Nonnull Player player) {
                            if (modifier == null) {
                                return;
                            }

                            attributes.remove(attribute);
                            openInventory();
                        }
                    }
            );
        }

        smartComponent.apply(this, SlotPattern.FANCY, 2);
    }

    private ItemStack makePreviewItem() {
        final ItemBuilder builder = new ItemBuilder(Material.PRISMARINE_SHARD)
                .setName("Attribute Preview")
                .addTextBlockLore("""
                        Displays a tooltip of the attributes.
                        """);

        final Map<Attribute, AttributeModifier> attributes = creator().getAttributes();

        if (attributes.isEmpty()) {
            builder.addLore().addLore("&8None!");
        }
        else {
            for (LinkedAttribute value : LinkedAttribute.values()) {
                final Attribute attribute = value.getLink();
                final AttributeModifier modifier = attributes.get(attribute);

                if (modifier != null) {
                    builder.addAttribute(attribute, modifier);
                }
            }
        }

        return builder.toItemStack(); // Make sure NOT to call asIcon() since we need the attribute tooltip
    }

}

package me.hapyl.mmu3.feature.itemcreator.gui;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.feature.itemcreator.LinkedAttribute;
import me.hapyl.mmu3.feature.itemcreator.LinkedEquipmentSlot;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import me.hapyl.spigotutils.module.util.Enums;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public class AttributeModifySubGUI extends ItemCreatorSubGUI {

    private final LinkedAttribute link;
    private final Map<AttributeModifier.Operation, String> operationDescriptionMap;

    private String name;
    private AttributeModifier.Operation operation;
    private double amount;
    private EquipmentSlot slot;

    public AttributeModifySubGUI(Player player, LinkedAttribute link) {
        super(player, link.getName(), Size.FIVE, new AttributeSubGUI(player));
        this.link = link;

        this.operationDescriptionMap = Maps.newHashMap();

        this.operationDescriptionMap.put(
                AttributeModifier.Operation.ADD_NUMBER,
                "Adds (or subtracts) the specified amount to the base value."
        );

        this.operationDescriptionMap.put(
                AttributeModifier.Operation.ADD_SCALAR,
                "Adds this scalar of amount to the base value."
        );

        this.operationDescriptionMap.put(
                AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                "Multiply amount by this value, after adding 1 to it."
        );

        final AttributeModifier modifier = creator().getAttributeModifierOrCompute(link);

        this.name = modifier.getName();
        this.amount = modifier.getAmount();
        this.operation = modifier.getOperation();
        this.slot = modifier.getSlot();

        updateInventory();
    }

    @Override
    public void updateInventory() {
        setItem(13, ItemBuilder.of(link.getMaterial(), link.getName()).asIcon());

        // Name
        setItem(28, ItemBuilder.of(
                Material.FLOWER_BANNER_PATTERN,
                "Name",
                "Name of this modifier.",
                "",
                "Current Value: &e" + name,
                "",
                "&eClick to change"
        ).asIcon(), player -> {
            new SignGUI(player, "Enter Name") {
                @Override
                public void onResponse(Response response) {
                    final String string = response.getAsString();
                    if (string.isBlank() || string.isEmpty()) {
                        Message.error(player, "Name cannot be empty.");
                    }
                    else {
                        name = string;
                    }

                    runSync(AttributeModifySubGUI.this::updateInventory);
                }
            };
        });

        // Amount
        setItem(
                30,
                ItemBuilder.of(
                        Material.OAK_SIGN,
                        "Amount",
                        "Amount by which this modifier will apply its Operation.",
                        "",
                        "Current Amount: &e" + amount,
                        "",
                        "&eClick to change amount"
                ).asIcon(),
                player -> {
                    new SignGUI(player, "Enter Amount as Double") {
                        @Override
                        public void onResponse(Response response) {
                            final double aDouble = response.getDouble(0);
                            if (aDouble < 0.0d) {
                                Message.error(player, "Amount cannot be negative.");
                            }
                            else {
                                amount = aDouble;
                            }

                            runSync(AttributeModifySubGUI.this::updateInventory);
                        }
                    };
                }
        );

        // Operation
        final ItemBuilder operationBuilder = ItemBuilder.of(Material.COMPARATOR, "Operation", "Operation this modifier will apply.", "");

        for (AttributeModifier.Operation value : AttributeModifier.Operation.values()) {
            operationBuilder.addLore("&b" + (value.equals(operation) ? " ➥ &l" : "") + Chat.capitalize(value));
            if (value.equals(operation)) {
                operationBuilder.addSmartLore(operationDescriptionMap.get(operation), "&3 ");
            }
        }

        operationBuilder.addLore();
        operationBuilder.addLore("&eClick to cycle");

        setItem(32, operationBuilder.asIcon(), player -> {
            operation = Enums.getNextValue(AttributeModifier.Operation.class, operation);
            updateInventory();
        });

        // Slots
        final ItemBuilder slotBuilder = ItemBuilder.of(
                LinkedEquipmentSlot.fromLink(slot).getMaterial(),
                "Equipment Slot",
                "EquipmentSlot this Attribute Modifier is active on.",
                ""
        );

        for (LinkedEquipmentSlot value : LinkedEquipmentSlot.values()) {
            final boolean isCurrent = value.getLink() == slot;
            slotBuilder.addLore("&b" + (isCurrent ? " ➥ &l" : "") + Chat.capitalize(value));

            if (isCurrent) {
                slotBuilder.addSmartLore(value.getName(), "&3 ");
            }
        }

        slotBuilder.addLore();
        slotBuilder.addLore("&eClick to cycle forward");
        slotBuilder.addLore("&6Right Click to cycle backwards");

        setItem(34, slotBuilder.asIcon());

        setClick(34, player -> {
            slot = slot == null ? EquipmentSlot.HAND : slot == EquipmentSlot.HEAD ? null : Enums.getNextValue(EquipmentSlot.class, slot);
            updateInventory();
        }, ClickType.LEFT);

        setClick(34, player -> {
            slot = slot == null ? EquipmentSlot.HEAD : slot == EquipmentSlot.HAND ? null : Enums.getPreviousValue(EquipmentSlot.class, slot);
            updateInventory();
        }, ClickType.RIGHT);

        openInventory();
    }

}

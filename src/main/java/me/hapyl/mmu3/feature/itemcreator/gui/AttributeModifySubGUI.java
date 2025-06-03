package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Enums;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.feature.itemcreator.LinkedAttribute;
import me.hapyl.mmu3.feature.itemcreator.LinkedEquipmentSlot;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttributeModifySubGUI extends ItemCreatorSubGUI {

    private static final Map<AttributeModifier.Operation, String> operationDescriptionMap = Map.of(
            AttributeModifier.Operation.ADD_NUMBER, "Adds the specified amount to the base value.",
            AttributeModifier.Operation.ADD_SCALAR, "Adds this scaler of amount to the base value.",
            AttributeModifier.Operation.MULTIPLY_SCALAR_1, "Multiplies the amount by this value after adding 1 to it."
    );

    private final LinkedAttribute attribute;

    private Key key;
    private AttributeModifier.Operation operation;
    private double amount;
    private LinkedEquipmentSlot slot;

    public AttributeModifySubGUI(Player player, LinkedAttribute link, AttributeSubGUI gui) {
        super(player, link.getName(), Size.FIVE, gui);
        this.attribute = link;

        final ItemCreator creator = creator();

        final Map<Attribute, AttributeModifier> attributes = creator.getAttributes();
        final AttributeModifier modifier = attributes.get(link.getLink());

        // If the attribute present on creator means we're modifying it so read the data
        if (modifier != null) {
            this.key = Key.ofString(modifier.getKey().getKey());
            this.operation = modifier.getOperation();
            this.amount = modifier.getAmount();
            this.slot = LinkedEquipmentSlot.ofLink(modifier.getSlotGroup());
        }
        // Else create a new instance, don't add it though
        else {
            this.key = randomKey(); // Random key by default
            this.operation = AttributeModifier.Operation.ADD_NUMBER;
            this.amount = 1;
            this.slot = LinkedEquipmentSlot.ALL_SLOTS;
        }

        openInventory();
    }

    @Override
    public boolean willDiscardIfUsedGoBackButton() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        setItem(
                13, new ItemBuilder(attribute.getMaterial())
                        .setName(attribute.getName())
                        .addTextBlockLore(attribute.getDescription())
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to confirm")
                        .asIcon(), player -> {
                    creator().setAttribute(
                            attribute.getLink(),
                            new AttributeModifier(key.asNamespacedKey(), amount, operation, slot.getLink())
                    );

                    openReturnGUI();
                }
        );

        // Key
        setItem(
                20,
                new ItemBuilder(Material.NAME_TAG)
                        .setName("Key")
                        .addTextBlockLore("""
                                A key of this modifier; modifiers with the same keys don't stack.
                                
                                Current Key: &f%s
                                
                                &8◦ &eLeft-Click to edit
                                """.formatted(key.toString()))
                        .asIcon(), player -> {
                    new SignGUI(player, "Enter Key") {
                        @Override
                        public void onResponse(Response response) {
                            final Key newKey = Key.ofStringOrNull(response.getAsString().toLowerCase());

                            if (newKey == null) {
                                Message.error(player, "Malformed key, must follow the %s pattern!".formatted(Key.PATTERN.toString()));
                            }
                            else {
                                key = newKey;
                            }

                            runSync(AttributeModifySubGUI.this::openInventory);
                        }
                    };
                }
        );

        // Amount
        setItem(
                30,
                new ItemBuilder(Material.OAK_SIGN)
                        .setName("Amount")
                        .addTextBlockLore("""
                                The amount by which this modifier will apply its operation.
                                
                                Current Amount: &e%.1f
                                
                                &8◦ &eLeft-Click to edit
                                """.formatted(amount))
                        .asIcon(), player -> {
                    new SignGUI(player, "Enter Amount") {
                        @Override
                        public void onResponse(Response response) {
                            final double newAmount = response.getDouble(0);

                            if (newAmount == 0.0) {
                                Message.error(player, "Amount cannot be 0!");
                            }
                            else if (Math.abs(newAmount) > 1_000) {
                                Message.error(player, "Amount out of range (-1000 to 1000)!");
                            }
                            else {
                                amount = newAmount;
                            }

                            runSync(AttributeModifySubGUI.this::openInventory);
                        }
                    };
                }
        );

        // Operation
        makeCycleItem(
                32,
                Material.COMPARATOR,
                "Operation",
                "Operation this modifier will perform.",
                AttributeModifier.Operation.class,
                operation,
                operationDescriptionMap::get,
                op -> operation = op
        );

        // Slot
        makeCycleItem(
                24,
                slot.getMaterial(),
                "Equipment Slot",
                "Equipment slot this attribute is active on.",
                LinkedEquipmentSlot.class,
                slot,
                LinkedEquipmentSlot::getDescription,
                sl -> slot = sl
        );
    }

    private <E extends Enum<E>> void makeCycleItem(int slot, Material material, String name, String description, Class<E> enumClass, E current, Function<E, String> currentValueDescriptionFn, Consumer<E> applyAction) {
        final ItemBuilder builder = new ItemBuilder(material)
                .setName("" + name)
                .addTextBlockLore(description)
                .addLore();

        final E[] values = enumClass.getEnumConstants();

        for (E value : values) {
            final boolean isCurrent = current.equals(value);
            final String valueName = Chat.capitalize(value);

            builder.addLore(
                    isCurrent
                            ? "&b ➥ " + valueName
                            : "&8 " + valueName
            );

            // If the current value, display the description
            if (isCurrent) {
                builder.addTextBlockLore(currentValueDescriptionFn.apply(value), "&7&o ");
            }
        }

        builder.addLore();
        builder.addLore("&8◦ &eLeft-Click to cycle");
        builder.addLore("&8◦ &6Right-Click to cycle backwards");

        setItem(slot, builder.asIcon());

        setAction(
                slot, player -> {
                    applyAction.accept(Enums.getNextValue(enumClass, current));
                    openInventory();
                }, ClickType.LEFT
        );

        setAction(
                slot, player -> {
                    applyAction.accept(Enums.getPreviousValue(enumClass, current));
                    openInventory();
                }, ClickType.RIGHT
        );
    }

    private static Key randomKey() {
        final String uuid = UUID.randomUUID().toString();

        return Key.ofString(uuid.substring(0, uuid.indexOf("-")));
    }


}

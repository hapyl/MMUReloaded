package me.hapyl.mmu3.outcast.hypixel.slayer.gui;

import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerType;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossDrop;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;
import me.hapyl.spigotutils.module.util.RomanNumber;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SlayerDropListGUI extends PanelGUI {

    private final SlayerType type;

    public SlayerDropListGUI(Player player, SlayerType type) {
        super(player, "%s %s Drops".formatted(type.getData().getName(), ARROW_FORWARD), Size.FOUR);
        this.type = type;
        updateInventory();
    }

    @Override
    public void updateInventory() {
        setPanelGoBack(PanelSlot.CENTER, type.getData().getName(), player -> new SlayerTierGUI(player, type));
        final SmartComponent component = newSmartComponent();

        for (SlayerBossDrop drop : type.getDrops()) {
            final Material material = drop.getMaterial();
            final ItemBuilder builder = new ItemBuilder(material).setName(Chat.capitalize(material));

            builder.addLore("&7Drop Chance: &b%s", drop.getChanceString());
            builder.addLore();

            drop.getTierAmounts().forEach((tier, amount) -> {
                if (amount.getMin() == amount.getMax()) {
                    builder.addLore("&7Tier %s amount: &b%s", RomanNumber.toRoman(tier), amount.getMin());
                }
                else {
                    builder.addLore("&7Tier %s amount: &b%s-%s", RomanNumber.toRoman(tier), amount.getMin(), amount.getMax());
                }
            });

            component.add(builder.build());
        }

        component.fillItems(this, SlotPattern.INNER_LEFT_TO_RIGHT);
        openInventory();
    }
}

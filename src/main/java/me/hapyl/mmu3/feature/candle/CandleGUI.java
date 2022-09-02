package me.hapyl.mmu3.feature.candle;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CandleGUI extends PanelGUI {

    private final CandleController controller;
    private final Data data;

    public CandleGUI(Player player) {
        super(player, "Candles", Size.FOUR);
        controller = Main.getRegistry().candleController;
        data = controller.getData(player);
        updateInventory();
    }

    @Override
    public void updateInventory() {
        final boolean offset = data.isOffset();

        setPanelCloseMenu();
        setPanelItem(
                PanelSlot.CENTER + 2,
                new ItemBuilder(offset ? Material.LIME_DYE : Material.GRAY_DYE)
                        .setName((offset ? "&a" : "&c") + "Random Offset")
                        .setSmartLore("If enabled, armor stand head will have a random horizontal offset.")
                        .build(), player -> {
                    data.setOffset(!offset);
                    Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
                    updateInventory();
                }
        );

        final SmartComponent component = newSmartComponent();

        for (Candle value : Candle.values()) {
            component.add(value.getItem(), player -> {
                data.setCandle(value);
                PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
                if (!controller.hasItem(player)) {
                    controller.giveItem(player);
                }
                closeInventory();
            });
        }

        component.fillItems(this, SlotPattern.CHUNKY, 1);
        openInventory();
    }
}

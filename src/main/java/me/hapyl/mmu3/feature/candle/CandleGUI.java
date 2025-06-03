package me.hapyl.mmu3.feature.candle;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CandleGUI extends PanelGUI {

    private final CandleController controller;
    private final Data data;

    public CandleGUI(Player player) {
        super(player, "Candles", Size.FIVE);

        controller = Main.getRegistry().candleController;
        data = controller.getData(player);

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final boolean offset = data.isOffset();

        fillRow(0, PANEL_ITEM);

        setPanelCloseMenu();
        setPanelItem(
                PanelSlot.CENTER + 2,
                new ItemBuilder(offset ? Material.LIME_DYE : Material.GRAY_DYE)
                        .setName("Random Offset " + BukkitUtils.checkmark(offset))
                        .addTextBlockLore("""
                                If enabled, the candle will have a randomized horizontal offset.
                                
                                &8◦ &eLeft-Click to toggle
                                """)
                        .asIcon(), player -> {
                    data.setOffset(!offset);
                    Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
                    player.updateInventory();
                }
        );

        final SmartComponent component = newSmartComponent();

        for (Candle value : Candle.values()) {
            component.add(
                    value.getItem(), player -> {
                        data.setCandle(value);
                        PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
                        if (!controller.hasItem(player)) {
                            controller.giveItem(player);
                        }
                        player.closeInventory();
                    }
            );
        }

        component.apply(this, SlotPattern.CHUNKY, 2);
    }

}

package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StandTuningGUI extends PlayerGUI {
    public StandTuningGUI(Player player) {
        super(player, "Select Part or Axis", 5);
        updateInventory();
    }

    private void updateInventory() {
        final StandEditor editor = Main.getStandEditor();
        final Player player = getPlayer();

        final TuneData data = editor.getTuneDataOrNew(player);

        editor.getTuningPartSlotMap().forEach((slot, part) -> {
            final boolean currentPart = part == data.getPart();

            setItem(slot, new ItemBuilder(part.getMaterial())
                    .setName((currentPart ? "&a" : "&c") + part.getName())
                    .predicate(currentPart, ItemBuilder::glow)
                    .hideFlags()
                    .build());

            setClick(slot, p -> {
                data.setPart(part);
                applyData(data, "Set current part to %s.", part.getName());
            });
        });

        int slot = 29;
        for (TuneData.Axis axis : TuneData.Axis.values()) {
            setItem(slot, new ItemBuilder(axis.getMaterial())
                    .setName(axis.getColor() + axis.name() + " Axis")
                    .predicate(axis == data.getAxis(), ItemBuilder::glow)
                    .hideFlags()
                    .build());

            setClick(slot, p -> {
                data.setAxis(axis);
                applyData(data, "Set current axis to %s.", axis.name());
            });

            slot += 2;
        }

        openInventory();
    }

    private void applyData(TuneData data, String message, Object... objects) {
        final Player player = getPlayer();
        Main.getStandEditor().setTuneData(player, data);
        player.closeInventory();

        Message.success(player, message, objects);
        Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
    }

}

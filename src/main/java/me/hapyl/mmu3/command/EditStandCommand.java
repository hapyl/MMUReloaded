package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.standeditor.Data;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.standeditor.StandEditorGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EditStandCommand extends SimplePlayerAdminCommand {
    public EditStandCommand(String name) {
        super(name);
        setAliases("stand", "es");
        setDescription("Opens an editor of closest armor stand if possible.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final ArmorStand closest = (ArmorStand) BukkitUtils.getClosestEntityTo(
                player.getNearbyEntities(10, 10, 10),
                player.getLocation(),
                EntityType.ARMOR_STAND
        );

        if (closest == null) {
            Message.error(player, "No armor stands nearby that can be edited.");
            return;
        }

        final StandEditor editor = Main.getStandEditor();
        if (!editor.isEditable(closest)) {
            Message.error(player, "This armor stand is not editable!");
            return;
        }

        new StandEditorGUI(player, new Data(player, closest));
    }
}

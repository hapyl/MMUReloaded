package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.standeditor.StandEditorData;
import me.hapyl.mmu3.feature.standeditor.StandEditorGUI;
import me.hapyl.mmu3.message.Message;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
        // Prioritise target armor stand
        ArmorStand stand = null;

        Entity targetEntity = player.getTargetEntity(10, true);

        // If not targeting entity or not armor stand, get the closest stand
        if (!(targetEntity instanceof ArmorStand)) {
            stand = (ArmorStand) BukkitUtils.getClosestEntityTo(
                    player.getNearbyEntities(10, 10, 10),
                    player.getLocation(),
                    EntityType.ARMOR_STAND
            );
        }

        if (stand == null) {
            Message.error(player, "Neither target armor stand nor any nearby!");
            return;
        }

        final StandEditor editor = Main.getStandEditor();

        if (editor.isTaken(stand)) {
            Message.error(player, "This armor stand is already being edited!");
            return;
        }

        final StandEditorData data = editor.getData(player);
        data.edit(stand);

        new StandEditorGUI(player, data);
    }
}

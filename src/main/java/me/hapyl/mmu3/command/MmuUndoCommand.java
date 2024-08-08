package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.UndoManager;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.feature.block.BlockChangeQueue;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class MmuUndoCommand extends SimplePlayerAdminCommand {
    public MmuUndoCommand(String name) {
        super(name);

        setDescription("Allows to undo block manipulations.");
        setUsage("mmuundo [deep]");
        setAliases("mu");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final int deep = getArgument(args, 0).toInt(1);

        if (deep < 0) {
            Message.error(player, "Cannot be 0 or negative.");
            return;
        }

        final BlockChangeQueue undoMap = UndoManager.getUndoMap(player);

        if (undoMap.isEmpty()) {
            Message.info(player, "Nothing to undo!");
            return;
        }

        final int undid = undoMap.restore(deep);
        Message.info(player, "Undid %s available operations!".formatted(undid));
    }
}

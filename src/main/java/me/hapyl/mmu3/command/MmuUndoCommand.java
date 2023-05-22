package me.hapyl.mmu3.command;

import me.hapyl.mmu3.UndoManager;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.BlockChangeQueue;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
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
        final int deep = args.length > 0 ? Validate.getInt(args[0]) : 1;

        if (deep < 0) {
            Message.error(player, "Cannot be negative.");
            return;
        }

        final BlockChangeQueue undoMap = UndoManager.getUndoMap(player);
        undoMap.restore(deep);

        if (undoMap.isEmpty()) {
            Message.error(player, "Nothing to undo!");
            return;
        }

        final int restoredBlocks = undoMap.restoreLast();

        Message.success(player, "Undid %s edits!".formatted(restoredBlocks));
    }
}

package me.hapyl.mmu3.command.worldedit;

import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class CopyShortcutCommand extends SimplePlayerAdminCommand {
    public CopyShortcutCommand(String name) {
        super(name);
        setDescription("Shortcut for a copy worldedit command.");
    }

    @Override
    protected void execute(Player player, String[] args) {

    }
}

package me.hapyl.mmu3.command.worldedit;

import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;

public abstract class WorldEditShortcutCommandCommand extends SimplePlayerAdminCommand {

    public WorldEditShortcutCommandCommand(String name, String description) {
        super(name);

        setDescription("A worldedit shortcut commands that " + description);
    }

}

package me.hapyl.mmu3.command.worldedit;

import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.util.DependencyInjector;

public class WorldEditShortcutRegistry extends DependencyInjector<Main> {

    private final CommandProcessor commandProcessor;

    public WorldEditShortcutRegistry(Main plugin) {
        super(plugin);
        commandProcessor = new CommandProcessor(plugin);

        // Register shortcuts
        register(WorldEditShortcutCommand.create("pos1", "1"));
        register(WorldEditShortcutCommand.create("pos2", "2"));
        register(WorldEditShortcutCommand.create("hPos1", "h1", "11"));
        register(WorldEditShortcutCommand.create("hPos2", "h2", "22"));
        register(WorldEditShortcutCommand.create("copy", "c", "+"));
        register(WorldEditShortcutCommand.create("cut", "x", "-"));
        register(WorldEditShortcutCommand.create("paste", "p", "_"));
        register(WorldEditShortcutCommand.create("rotate", "rot", "~"));
        register(WorldEditShortcutCommand.create("stack", "st", "#"));
        register(WorldEditShortcutCommand.create("move", "mv", "^"));
    }

    private void register(WorldEditShortcutCommand command) {
        commandProcessor.registerCommand(command);
    }
}

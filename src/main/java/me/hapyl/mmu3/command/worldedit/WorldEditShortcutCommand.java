package me.hapyl.mmu3.command.worldedit;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class WorldEditShortcutCommand extends SimplePlayerAdminCommand {

    private WorldEditShortcutCommand(@Nonnull String name, @Nullable String... aliases) {
        super(name);

        if (aliases != null) {
            setAliases(aliases);
        }

        setDescription("A WorldEdit shortcut commands for " + getShortcut());
    }

    public abstract String getShortcut();

    @Override
    protected void execute(Player player, String[] strings) {
        player.performCommand("/" + getShortcut() + " " + Chat.arrayToString(strings, 0));
    }

    public static WorldEditShortcutCommand create(
            @Nonnull String command,
            @Nonnull String shortcut,
            @Nullable String... aliases
    ) {
        return new WorldEditShortcutCommand(shortcut, aliases) {
            @Override
            public String getShortcut() {
                return command;
            }
        };
    }

}

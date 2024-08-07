package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.command.SimpleCommand;
import me.hapyl.eterna.module.util.ArgumentList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public abstract class MMUCommand extends SimpleCommand {

    public MMUCommand(@Nonnull String name, @Nonnull String permission) {
        super(name);
        setPermission(permission);
    }

    public abstract void execute(@Nonnull Player player, @Nonnull ArgumentList args);

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            Chat.sendMessage(sender, ChatColor.DARK_RED + "You must be a player to use this command!");
            return;
        }

        this.execute(player, new ArgumentList(args));
    }
}

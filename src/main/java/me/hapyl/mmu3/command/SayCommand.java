package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.ServerPro;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class SayCommand extends SimpleCommand {
    public SayCommand(String name) {
        super(name);
        setDescription("minecraft:say override");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        final ServerPro serverPro = Main.getRegistry().serverPro;
        final String strings = Chat.arrayToString(args, 0);
        if (sender instanceof ConsoleCommandSender) {
            if (strings.contains(serverPro.getStringTenMinutes())) {
                serverPro.announce(true);
            }
            else if (strings.contains(serverPro.getStringFiveMinutes())) {
                serverPro.announce(false);
            }
        }
        else {
            Chat.broadcast("[%s] %s", sender.getName(), strings);
        }
    }
}

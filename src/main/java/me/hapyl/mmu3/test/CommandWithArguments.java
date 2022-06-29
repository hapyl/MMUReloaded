package me.hapyl.mmu3.test;

import me.hapyl.spigotutils.module.annotate.ArgumentSensitive;
import me.hapyl.spigotutils.module.command.ArgumentProcessor;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWithArguments extends SimplePlayerAdminCommand {
    public CommandWithArguments(String name) {
        super(name);
    }

    @ArgumentSensitive(targetString = "hello", targetSize = 0)
    public void firstArgumentIsHello(CommandSender sender, String[] args) {
        sender.sendMessage("This works!");
        sender.sendMessage(args);
    }

    @Override
    protected void execute(Player player, String[] strings) {
        final ArgumentProcessor argumentProcessor = getArgumentProcessor();
        if (argumentProcessor != null) {
            argumentProcessor.getArgumentMap().forEach((a, b) -> {
                player.sendMessage("{%s} n=%s, s=%s".formatted(b.getName(), a.targetString(), a.targetSize()));
            });
        }
    }
}

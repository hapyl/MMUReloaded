package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PingCommand extends SimplePlayerAdminCommand {
    public PingCommand(String name) {
        super(name);
        this.setDescription("Allow to check player ping.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Player target = args.length == 0 ? player : Bukkit.getPlayer(args[0]);

        if (target == null) {
            Message.PLAYER_NOT_ONLINE.send(player, args[0]);
            return;
        }

        Message.success(player, "%s's ping is %sms.", target == player ? "Your" : target.getName(), target.getPing());
    }
}

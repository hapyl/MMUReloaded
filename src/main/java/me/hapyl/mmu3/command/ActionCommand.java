package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.action.PlayerActions;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.DisabledCommand;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionCommand extends SimplePlayerAdminCommand implements DisabledCommand {
    public ActionCommand(String name) {
        super(name);
        setDescription("Allows to perform certain actions, like sitting, swinging hands etc.");
        setUsage("/action <action> [player]");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // action (Action)
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final PlayerActions action = Validate.getEnumValue(PlayerActions.class, args[0]);
        final Player target = args.length >= 2 ? Bukkit.getPlayer(args[1]) : player;

        if (action == null) {
            Message.error(player, "%s is invalid action!", args[0]);
            return;
        }

        if (target == null) {
            Message.error(player, "This player is not online!");
            return;
        }

        action.perform(target);
    }
}

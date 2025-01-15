package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.util.ArgumentList;
import me.hapyl.mmu3.feature.action.PlayerActions;
import me.hapyl.mmu3.message.Message;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionCommand extends MMUCommand {
    public ActionCommand(String name) {
        super(name);

        addCompleterValues(1, PlayerActions.values());
    }

    @Nonnull
    @Override
    protected String description() {
        return "Allows performing certain actions, like sitting, swinging hands, etc.";
    }

    @Nullable
    @Override
    protected String usage() {
        return "/action <action> [player]";
    }

    @Override
    protected void execute(@Nonnull Player player, @Nonnull ArgumentList args) {
        // action (Action)
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final PlayerActions action = args.get(0).toEnum(PlayerActions.class);
        final Player target = args.length >= 2 ? args.get(1).toPlayer() : player;

        if (action == null) {
            Message.error(player, "%s is invalid action!", args.get(0).toString());
            return;
        }

        if (target == null) {
            Message.error(player, "This player is not online!");
            return;
        }

        action.perform(target);
    }

}

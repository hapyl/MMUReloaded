package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.command.SimplePlayerCommand;
import me.hapyl.eterna.module.util.Enums;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.mmu3.outcast.game.Games;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GameCommand extends SimplePlayerCommand {
    public GameCommand(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.error(player, "Please provide a game name instead!");
            return;
        }

        final Games game = Enums.byName(Games.class, args[0]);
        if (game == null) {
            Message.error(player, "Invalid game.");
            return;
        }

        Message.success(player, "Starting %s...", game.getName());
        // allow only ops to use arguments
        game.startGame(player, player.isOp() ? new Arguments(args, 1) : Arguments.empty());
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(Games.values(), args);
        }
        if (args.length == 2) {
            return completerSort(new String[] { "debug" }, args);
        }
        return Collections.emptyList();
    }
}

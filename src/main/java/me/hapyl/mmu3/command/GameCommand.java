package me.hapyl.mmu3.command;

import me.hapyl.mmu3.game.Arguments;
import me.hapyl.mmu3.game.GameSelectGUI;
import me.hapyl.mmu3.game.Games;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerCommand;
import me.hapyl.spigotutils.module.util.Validate;
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
            new GameSelectGUI(player);
            return;
        }

        final Games game = Validate.getEnumValue(Games.class, args[0]);
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
        return Collections.emptyList();
    }
}

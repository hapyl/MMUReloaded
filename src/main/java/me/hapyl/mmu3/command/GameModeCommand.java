package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.mmu3.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class GameModeCommand extends SimplePlayerAdminCommand {

    private final Map<GameMode, Set<String>> gameModeMap;

    public GameModeCommand(String name) {
        super(name);
        setDescription("An alias for gamemode command.");

        gameModeMap = Maps.newHashMap();
        gameModeMap.put(GameMode.CREATIVE, Sets.newHashSet("1", "creative", "c"));
        gameModeMap.put(GameMode.SURVIVAL, Sets.newHashSet("0", "survival", "s"));
        gameModeMap.put(GameMode.ADVENTURE, Sets.newHashSet("2", "adventure", "a"));
        gameModeMap.put(GameMode.SPECTATOR, Sets.newHashSet("3", "spectator", "sp"));
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED.send(player, 1);
            return;
        }

        final String value = args[0];
        final Player target = args.length >= 2 ? Bukkit.getPlayer(args[1]) : player;
        GameMode gameMode = null;

        for (GameMode gm : gameModeMap.keySet()) {
            if (gameModeMap.get(gm).contains(value.toLowerCase(Locale.ROOT))) {
                gameMode = gm;
                break;
            }
        }

        if (target == null) {
            Message.error(player, "This player is not online!");
            return;
        }

        if (gameMode == null) {
            Message.error(player, "Invalid game mode '%s'.", value);
            return;
        }

        final String modeName = Chat.capitalize(gameMode.name());

        target.setGameMode(gameMode);
        Message.success(player, "Changed %s game mode to %s.", player == target ? "own" : target.getName() + "'s", modeName);

        if (target != player) {
            Message.info(target, "Your game mode was changed to %s.", modeName);
        }
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(gameModeMap.keySet(), args);
        }
        return null;
    }

}

package me.hapyl.mmu3.outcast.chatgame;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.outcast.game.Arguments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChatGameManager extends Feature implements Listener {

    private ChatGameInstance currentGame; // Only one instance allowed

    private BukkitTask task;

    public ChatGameManager(Main mmu3plugin) {
        super(mmu3plugin);
    }

    public ChatGameInstance getCurrentGame() {
        return currentGame;
    }

    public void startGame(@Nonnull ChatGames game, @Nullable String... args) {
        currentGame = game.getGame().newInstance(args == null ? Arguments.empty() : new Arguments(args));

        if (task != null && task.isCancelled()) {
            task.cancel();
        }

        task = newTask();
    }

    private BukkitTask newTask() {
        if (currentGame == null) {
            throw new NullPointerException("currentGame not instantiated!");
        }

        return new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskLater(getPlugin(), currentGame.reference.getTimeLimit() / 50L);
    }

    public boolean isGameRunning() {
        return currentGame != null;
    }

    @EventHandler()
    public void handleChatMessage(AsyncPlayerChatEvent ev) {
        if (currentGame == null) {
            return;
        }

        ev.setCancelled(true);

        final Player player = ev.getPlayer();
        final String message = ev.getMessage();

        if (currentGame.hasAnswered(player)) {
            return;
        }
    }

}

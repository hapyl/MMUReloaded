package me.hapyl.mmu3.game;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class Game {

    private final Set<Player> playing;
    private final String name;
    private final PanelGUI.Size size;

    public Game(String name) {
        this(name, PanelGUI.Size.THREE);
    }

    public Game(String name, PanelGUI.Size size) {
        this.name = name;
        this.size = size;
        this.playing = Sets.newHashSet();
    }

    protected void start(Player player, @Nonnull Arguments arguments) {
        if (isPlaying(player)) {
            Message.error(player, "You are already playing a game!");
            return;
        }

        final GameInstance instance = newInstance(player, arguments);

        new BukkitRunnable() {
            private int tickTotal = 0;

            @Override
            public void run() {
                if (!isPlaying(player)) {
                    this.cancel();
                    return;
                }
                instance.onTick(tickTotal, tickTotal % 20);
                ++tickTotal;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);

        addPlaying(player);
    }

    /**
     * Creates an instance of this game with GUI.
     *
     * @param player    - Player.
     * @param arguments - Extra arguments a game may have.
     */
    @Nonnull
    protected abstract GameInstance newInstance(Player player, @Nonnull Arguments arguments);

    public void addPlaying(Player player) {
        playing.add(player);
    }

    public void removePlaying(Player player) {
        playing.remove(player);
    }

    public boolean isPlaying(Player player) {
        return playing.contains(player);
    }

    public PanelGUI.Size getMenuSize() {
        return size;
    }

    public String getName() {
        return name;
    }

}

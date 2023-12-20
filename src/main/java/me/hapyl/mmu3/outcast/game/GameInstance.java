package me.hapyl.mmu3.outcast.game;

import me.hapyl.spigotutils.module.inventory.gui.Action;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public abstract class GameInstance {

    protected final GameGUI gui;

    private final Player player;
    private final Game game;
    private final boolean debug;

    public GameInstance(Player player, Game game, boolean debug) {
        this.player = player;
        this.game = game;
        this.debug = debug;
        this.gui = new GameGUI(player, this);

        Runnables.runLater(() -> {
            onGameStart();
            gui.openInventory();
        }, 1L);
    }

    public GameInstance(Player player, Game game) {
        this(player, game, false);
    }

    public boolean isDebug() {
        return debug;
    }

    /**
     * Called once upon starting the game.
     */
    public abstract void onGameStart();

    /**
     * Called every time player clicked at a slot.
     *
     * @param slot      - Clicked slot.
     * @param clickType - Click Type.
     */
    public abstract void onClick(int slot, ClickType clickType);

    /**
     * Called whenever player closes inventory.
     *
     * @return true if the game should be stopped, false otherwise.
     */
    public boolean onGameStop() {
        return true;
    }

    public final void stopPlaying() {
        final boolean shouldStopPlaying = onGameStop();

        if (!shouldStopPlaying) {
            return;
        }

        getGame().removePlaying(player);
    }

    /**
     * Called every tick, the game is running.
     *
     * @param tick - Total ticks passed from starting the game.
     */
    public void onTick(int tick) {
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Nonnull
    public String getName() {
        return game.getName();
    }

    protected final void setItem(int slot, ItemStack item) {
        gui.setItem(slot, item);
    }

    protected final void setItem(int slot, ItemStack item, Action action) {
        gui.setItem(slot, item, action);
    }

}

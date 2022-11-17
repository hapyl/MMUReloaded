package me.hapyl.mmu3.outcast.game;

import me.hapyl.spigotutils.module.inventory.gui.Action;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class GameInstance {

    private final Player player;
    private final Game game;
    private final boolean debug;
    protected final GameGUI gui;

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
     * <b>If overriding, do not forget to call {@link this#stopPlaying()} to remove player from playing the game.</b>
     */
    public void onGameStop() {
        stopPlaying();
    }

    // Call onGameStop if not overwritten.
    // Do not forget to call this to actually stop player playing the game.
    public final void stopPlaying() {
        getGame().removePlaying(player);
    }

    /**
     * Called every tick the game is running.
     *
     * @param tickTotal - Total ticks passed from starting the game.
     * @param tickMod20 - Modulo of 20 of passed ticks.
     */
    public void onTick(int tickTotal, int tickMod20) {
    }

    protected final void setItem(int slot, ItemStack item) {
        gui.setItem(slot, item);
    }

    protected final void setItem(int slot, ItemStack item, Action action) {
        gui.setItem(slot, item, action);
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public String getName() {
        return game.getName();
    }

}

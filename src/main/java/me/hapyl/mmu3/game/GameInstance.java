package me.hapyl.mmu3.game;

import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GameInstance {

    private final Player player;
    private final Game game;
    protected final GameGUI gui;

    public GameInstance(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.gui = new GameGUI(player, this);

        Runnables.runLater(() -> {
            onGameStart();
            gui.openInventory();
        }, 1L);
    }

    /**
     * Called once upon starting the game.
     */
    public abstract void onGameStart();

    /**
     * Called every time player clicked at a slot.
     *
     * @param slot - Clicked slot.
     */
    public abstract void onClick(int slot);

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

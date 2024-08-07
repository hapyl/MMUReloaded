package me.hapyl.mmu3.outcast.game;

import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.eterna.module.inventory.gui.CancelType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class GameGUI extends PanelGUI {

    private final GameInstance game;

    public GameGUI(Player player, GameInstance game) {
        super(player, "★ " + game.getName() + (game.isDebug() ? " §4§lDEBUGGING" : ""), game.getGame().getMenuSize());
        this.game = game;

        setCancelType(CancelType.EITHER);
        setEventListener(((p, gui, ev) -> {
            final int rawSlot = ev.getRawSlot();
            game.onClick(rawSlot, ev.getClick());
        }));
        setCloseEvent((p) -> game.stopPlaying());
    }

    @Nonnull
    public GameInstance getGame() {
        return game;
    }

    @Override
    public void updateInventory() {
    }
}

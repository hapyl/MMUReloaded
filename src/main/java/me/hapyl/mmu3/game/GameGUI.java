package me.hapyl.mmu3.game;

import kz.hapyl.spigotutils.module.inventory.gui.CancelType;
import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Player;

public class GameGUI extends PanelGUI {

    private final GameInstance game;

    public GameGUI(Player player, GameInstance game) {
        super(player, "★ " + game.getName(), game.getGame().getMenuSize());
        this.game = game;

        setCancelType(CancelType.EITHER);
        setEventListener(((p, gui, ev) -> game.onClick(ev.getRawSlot())));
        setCloseEvent((p) -> game.onGameStop());
    }

    public GameInstance getGame() {
        return game;
    }

    @Override
    public void updateInventory() {

    }
}

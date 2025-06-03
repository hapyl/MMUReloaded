package me.hapyl.mmu3.feature.statechanger;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.util.InjectListener;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StateChangerListener extends InjectListener {

    public StateChangerListener(Main main) {
        super(main);
    }

    @EventHandler
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final ItemStack item = ev.getItem();
        final Action action = ev.getAction();
        final Block clickedBlock = ev.getClickedBlock();

        if (!player.isOp()
                || player.getGameMode() != GameMode.CREATIVE
                || clickedBlock == null
                || action == Action.PHYSICAL
                || item == null
                || !ItemBuilder.compareItemKey(item, StateChanger.ITEM_KEY)) {
            return;
        }

        ev.setCancelled(true);
        stateChanger().openEditor(player, clickedBlock);
    }

}

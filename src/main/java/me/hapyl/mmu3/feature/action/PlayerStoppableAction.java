package me.hapyl.mmu3.feature.action;

import org.bukkit.entity.Player;

public interface PlayerStoppableAction extends PlayerAction {

    void stopPerforming(Player player);
}

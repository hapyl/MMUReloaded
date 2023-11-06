package me.hapyl.mmu3.feature.action;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface PlayerAction {

    void start(@Nonnull Player player);

    void stop(@Nonnull Player player);

}

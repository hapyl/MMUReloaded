package me.hapyl.mmu3.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface MaterialCooldown {

    Material getMaterial();

    int getTicks();

    default void startCooldown(Player player) {
        player.setCooldown(getMaterial(), getTicks());
    }

    default int getTicksLeft(Player player) {
        return player.getCooldown(getMaterial());
    }

    default boolean isOnCooldown(Player player) {
        return player.hasCooldown(getMaterial());
    }

}

package me.hapyl.mmu3.feature.dye;

import me.hapyl.spigotutils.module.util.Holder;
import org.bukkit.entity.Player;

public class ArmorData extends Holder<Player> {

    public ArmorData(Player player) {
        super(player);
    }

    public void reset() {

    }

    public enum Type {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        HORSE

    }

}

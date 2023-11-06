package me.hapyl.mmu3.outcast.fishing;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FishItem extends ItemStack {

    protected UUID uuid;
    protected int size;
    protected Player catcher;
    protected UUID catcherUuid;

    public FishItem(Material material) {
        super(material);
    }

    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    public int getSize() {
        return size;
    }

    @Nonnull
    public Player getCatcher() {
        return catcher;
    }

    @Nonnull
    public UUID getCatcherUuid() {
        return catcherUuid;
    }
}

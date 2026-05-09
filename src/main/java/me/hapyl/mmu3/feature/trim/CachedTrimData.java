package me.hapyl.mmu3.feature.trim;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CachedTrimData {

    private final int id;
    private final ItemStack[] items;

    CachedTrimData(int id, @NotNull ItemStack[] items) {
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    @Nonnull
    public ItemStack[] getItems() {
        return items;
    }

    public void give(Player player) {
        final PlayerInventory inventory = player.getInventory();

        for (ItemStack stack : items) {
            inventory.addItem(stack);
        }
    }
}

package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.util.HexId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

public class CachedTrimData {

    private final HexId hexId;
    private final ItemStack[] items;

    public CachedTrimData(ItemStack[] items) {
        this.hexId = HexId.random();
        this.items = items;
    }

    @Nonnull
    public HexId getHexId() {
        return hexId;
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

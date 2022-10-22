package me.hapyl.mmu3.outcast.hypixel.slayer;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlayerStartCost {

    public static final SlayerStartCost UNAFFORDABLE = new SlayerStartCost(Material.BEDROCK, 99999999);

    private final Material material;
    private final int amount;

    public SlayerStartCost(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public boolean canAfford(Player player) {
        return player.getInventory().contains(material, amount);
    }

    public boolean removeItemsIfCanAfford(Player player) {
        if (!canAfford(player)) {
            return false;
        }

        return player.getInventory().removeItem(new ItemStack(material, amount)).isEmpty();
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "x%s %s".formatted(amount, Chat.capitalize(material));
    }
}

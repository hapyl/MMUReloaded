package me.hapyl.mmu3.outcast.backpack;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.nbt.LazyType;
import me.hapyl.spigotutils.module.nbt.NBT;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class BackpackManager {

    public static ItemStack createBackpack(int size) {
        size = Numbers.clamp(size, 1, 6);
        final ItemStack backpack = ItemBuilder
                .playerHeadUrl("2308bf5cc3e9decaf0770c3fdad1e042121cf39cc2505bbb866e18c6d23ccd0c")
                .setName(backpackName(size))
                .build();
        final ItemMeta itemMeta = backpack.getItemMeta();

        if (itemMeta == null) {
            throw new NullPointerException("meta is null");
        }

        NBT.setValue(itemMeta, "BackpackId", LazyType.STR, UUID.randomUUID().toString());

        return backpack;
    }

    public static void openBackpack(Player player, ItemStack backpack) {

    }

    private static String backpackName(int size) {
        return switch (size) {
            case 1 -> "Small";
            case 2 -> "Medium";
            case 3 -> "Large";
            case 4 -> "Greater";
            case 5 -> "Giant";
            case 6 -> "Jumbo";
            default -> "invalid";
        } + " Backpack";
    }

}

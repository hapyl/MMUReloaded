package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class MMUOperationSkullDisplay implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "skull_display";
    }

    @Override
    public @NotNull String description() {
        return "Allows spawning a block display entity with a held item.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.isEmpty()) {
            MMULogger.error(player, Component.text("You must be holding an item!"));
            return;
        }

        if (!(itemStack.getItemMeta() instanceof SkullMeta skullMeta)) {
            MMULogger.error(player, Component.text("You must be holding a player skull!"));
            return;
        }

        player.getWorld().spawn(
                player.getLocation(), ItemDisplay.class, self -> {
                    self.setItemStack(new ItemBuilder(Material.PLAYER_HEAD).editMeta(
                            SkullMeta.class,
                            meta -> meta.setPlayerProfile(skullMeta.getPlayerProfile())
                    ).build());
                }
        );

        MMULogger.success(player, Component.text("Successfully spawned an item display!"));
    }

}
package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class MMUOperationHat implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "hat";
    }

    @Override
    public @NotNull String description() {
        return "Puts the held item on your head.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack heldItem = inventory.getItemInMainHand();

        if (heldItem.getType().isAir()) {
            MMULogger.error(player, Component.text("You aren't holding anything!"));
            return;
        }

        final ItemStack currentHelmet = inventory.getHelmet();
        inventory.setHelmet(heldItem);

        if (!currentHelmet.isEmpty()) {
            inventory.setItemInMainHand(currentHelmet);
        }
        else {
            heldItem.setAmount(0);
        }

        MMULogger.success(
                player,
                Component.empty()
                        .append(Component.text("Put "))
                        .append(Component.translatable(heldItem.translationKey(), NamedTextColor.GREEN))
                        .append(Component.text(" on your head!"))
        );

        MMULogger.sound(player, Sound.ENTITY_CHICKEN_EGG, 1.0f);
    }

}

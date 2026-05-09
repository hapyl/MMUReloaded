package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MMUOperationColor implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "color";
    }

    @Override
    public @NotNull String description() {
        return "Allows to color leather items.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(0, List.of("clear", "#"));
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!(item.getItemMeta() instanceof LeatherArmorMeta leatherMeta)) {
            MMULogger.error(player, Component.text("This item cannot be colored!"));
            return;
        }

        final String hex = args.get(0).toString();

        // Clear color
        if (hex.equalsIgnoreCase("clear")) {
            leatherMeta.setColor(null);

            MMULogger.success(player, Component.text("Cleared color."));
            MMULogger.sound(player, Sound.ITEM_BUCKET_EMPTY, 0.0f);
        }
        else {
            leatherMeta.setColor(parseHex(hex));

            MMULogger.success(player, Component.text("Changed color to %s!".formatted(hex)));
        }

        item.setItemMeta(leatherMeta);
    }

    @NotNull
    private Color parseHex(@NotNull String hex) {
        // Remove #
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Trim to min of 7
        hex = hex.substring(0, Math.min(hex.length(), 6));

        // Parse to bukkit color
        try {
            return Color.fromRGB(Integer.parseInt(hex, 16));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid hex: %s".formatted(hex));
        }
    }
}

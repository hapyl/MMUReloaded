package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class MMUOperationNightVision implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "night_vision";
    }

    @Override
    public @NotNull String description() {
        return "Toggles the night vision.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);

            MMULogger.success(player, Component.text("Disabled night vision."));
        }
        else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1, false, false, false));

            MMULogger.success(player, Component.text("Enabled night vision."));
        }
    }

}

package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MMUOperationPersonalWeather implements MMUOperation {

    private final Map<String, WeatherTypeSetter> weatherTypeSuppliers = Map.of(
            "reset", player -> {
                player.resetPlayerWeather();
                MMULogger.success(player, Component.text("Reset your personal weather!"));
            },
            "rail", player -> {
                player.setPlayerWeather(WeatherType.DOWNFALL);
                MMULogger.success(player, Component.text("Set your personal weather to rain."));
            },
            "clear", player -> {
                player.setPlayerWeather(WeatherType.CLEAR);
                MMULogger.success(player, Component.text("Set your personal weather to clear."));
            }
    );

    @Override
    public @NotNull String name() {
        return "personal_weather";
    }

    @Override
    public @NotNull String description() {
        return "Allows modifying player's personal weather.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(0, weatherTypeSuppliers.keySet());
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final WeatherTypeSetter setter = weatherTypeSuppliers.get(args.get(0).toString());

        if (setter == null) {
            MMULogger.error(player, Component.text("Invalid sub-operation %s!".formatted(args.get(0))));
            return;
        }

        setter.set(player);
    }

    interface WeatherTypeSetter {
        void set(@NotNull Player player);
    }

}

package me.hapyl.mmu3.command;

import me.hapyl.mmu3.data.PlayerData;
import me.hapyl.mmu3.feature.candle.CandleData;
import me.hapyl.mmu3.feature.candle.CandleMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationCandle implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "candle";
    }

    @Override
    public @NotNull String description() {
        return "Opens the candle selection menu.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        new CandleMenu(player, PlayerData.ofPlayer(player).requestData(CandleData.class));
    }

}

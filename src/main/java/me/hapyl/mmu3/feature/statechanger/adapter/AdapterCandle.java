package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Candle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterCandle extends LevelledAdapter<Candle> {
    public AdapterCandle() {
        super(Candle.class);
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Candles";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Number of candles.";
    }

    @Nonnull
    @Override
    public BiConsumer<Candle, Integer> apply() {
        return Candle::setCandles;
    }

    @Override
    public int getLevel(Candle candle) {
        return candle.getCandles();
    }

    @Override
    public int getMinLevel(Candle candle) {
        return 1;
    }

    @Override
    public int getMaxLevel(Candle candle) {
        return candle.getMaximumCandles();
    }
}

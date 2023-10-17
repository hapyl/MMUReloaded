package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.Levelled;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterLevelled extends LevelledAdapter<Levelled> {
    public AdapterLevelled() {
        super(Levelled.class);
    }

    @Override
    public int getLevel(Levelled levelled) {
        return levelled.getLevel();
    }

    @Override
    public int getMinLevel(Levelled levelled) {
        return 1;
    }

    @Override
    public int getMaxLevel(Levelled levelled) {
        return levelled.getMaximumLevel();
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.RAIL;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Level";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return """
                Represents the amount of fluid contained within this block, either by itself or inside a cauldron.
                                
                In the case of water and lava blocks, the levels have special meanings: a level of 0 corresponds to a source block, 1-7 regular fluid heights, and 8-15 to "falling" fluids.
                                
                All falling fluids have the same behavior, but the level corresponds to that of the block above them.
                """;
    }

    @Nonnull
    @Override
    public BiConsumer<Levelled, Integer> apply() {
        return Levelled::setLevel;
    }
}

package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterScaffolding extends LevelledAdapter<Scaffolding> {
    public AdapterScaffolding() {
        super(Scaffolding.class);
    }

    @Override
    public int getLevel(Scaffolding scaffolding) {
        return scaffolding.getDistance();
    }

    @Override
    public int getMaxLevel(Scaffolding scaffolding) {
        return scaffolding.getMaximumDistance();
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Scaffolding blockData) {
        final boolean isBottom = blockData.isBottom();

        gui.setItem(this, 21, isBottom, Material.SPRUCE_SLAB, "Bottom", "Indicates whether the scaffolding is floating or not.");
        gui.applyState(21, blockData, d -> d.setBottom(!isBottom));

        super.update(gui, player, blockData);
    }

    @Override
    public int getSlot() {
        return 23;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return Material.SCAFFOLDING;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Distance";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Indicates the distance from a scaffolding block placed above a 'bottom' scaffold.";
    }

    @Nonnull
    @Override
    public BiConsumer<Scaffolding, Integer> apply() {
        return Scaffolding::setDistance;
    }
}

package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerData;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.data.Segmentable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterSegmentable extends Adapter<Segmentable> {
    public AdapterSegmentable() {
        super(Segmentable.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Segmentable blockData, @Nonnull StateChangerData data) {
        final int minimumSegmentAmount = blockData.getMinimumSegmentAmount();
        final int maximumSegmentAmount = blockData.getMaximumSegmentAmount();
        final int segmentAmount = blockData.getSegmentAmount();

        gui.setLevelableItem(
                this,
                10,
                segmentAmount,
                maximumSegmentAmount,
                blockData.getMaterial(),
                "Segments",
                "The number of segments."
        );

        gui.applyLevelable(10, blockData, minimumSegmentAmount, segmentAmount, maximumSegmentAmount, Segmentable::setSegmentAmount);
    }
}

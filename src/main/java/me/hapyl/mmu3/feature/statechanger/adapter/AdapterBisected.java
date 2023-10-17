package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterBisected extends Adapter<Bisected> {
    public AdapterBisected() {
        super(Bisected.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Bisected blockData) {
        final Bisected.Half half = blockData.getHalf();
        final boolean isTopHalf = half == Bisected.Half.TOP;

        gui.setItem(
                this, 10,
                isTopHalf,
                blockData.getMaterial(),
                (isTopHalf ? "Top" : "Bottom"),
                "Denotes which half of a two-block tall material this block is."
        );

        gui.applyState(10, blockData, d -> d.setHalf(isTopHalf ? Bisected.Half.BOTTOM : Bisected.Half.TOP));
    }
}

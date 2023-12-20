package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.data.Snowable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterSnowable extends Adapter<Snowable> {
    public AdapterSnowable() {
        super(Snowable.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Snowable blockData) {
        final boolean isSnowy = blockData.isSnowy();

        gui.setItem(
                this, 22,
                isSnowy,
                blockData.getMaterial(),
                "Snowy",
                "Denotes whether this block has a snow-covered side and top texture (normally because the block above is snow)."
        );
        gui.applyState(22, blockData, d -> d.setSnowy(!isSnowy));
    }
}

package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterPiston extends Adapter<Piston> {
    public AdapterPiston() {
        super(Piston.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Piston blockData) {
        final boolean isExtended = blockData.isExtended();

        gui.setItem(
                this,
                10, isExtended, blockData.getMaterial(), "Extended", "Denotes whether the piston head is currently extended or not.");
        gui.applyState(10, blockData, d -> d.setExtended(!isExtended));
    }
}

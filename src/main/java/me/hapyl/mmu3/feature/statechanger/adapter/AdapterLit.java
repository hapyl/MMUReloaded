package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.ConditionedMaterial;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterLit extends Adapter<Lightable> {
    public AdapterLit() {
        super(Lightable.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Lightable blockData) {
        final boolean isLit = blockData.isLit();

        gui.setItem(
                this,
                19,
                ConditionedMaterial.of(isLit, Material.HORN_CORAL_FAN, Material.DEAD_HORN_CORAL_FAN),
                "Is Lit",
                "Whenever this block is lit"
        );

        gui.applyState(19, blockData, d -> d.setLit(!isLit));
    }
}

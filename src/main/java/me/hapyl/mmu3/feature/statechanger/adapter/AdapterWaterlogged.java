package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.ConditionedMaterial;
import me.hapyl.mmu3.feature.statechanger.StateChangerData;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterWaterlogged extends Adapter<Waterlogged> {
    public AdapterWaterlogged() {
        super(Waterlogged.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Waterlogged blockData, @Nonnull StateChangerData data) {
        final boolean waterlogged = blockData.isWaterlogged();

        gui.setItem(
                this, 28,
                ConditionedMaterial.of(waterlogged, Material.WATER_BUCKET, Material.BUCKET),
                "Waterlogged",
                "Whether this block has fluid in it."
        );
        gui.applyState(28, blockData, d -> d.setWaterlogged(!waterlogged));
    }
}

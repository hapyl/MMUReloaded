package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.type.CaveVines;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterCaveVines extends Adapter<CaveVines> {
    public AdapterCaveVines() {
        super(CaveVines.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull CaveVines blockData) {
        final boolean hasBerries = blockData.isBerries();

        gui.setItem(this, 10, hasBerries, Material.GLOW_BERRIES, "Berries", "Indicates whether the block has berries.");
        gui.applyState(10, blockData, d -> d.setBerries(!hasBerries));
    }
}

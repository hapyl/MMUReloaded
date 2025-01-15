package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.data.type.Gate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterGate extends Adapter<Gate> {
    public AdapterGate() {
        super(Gate.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Gate blockData) {
        final boolean inWall = blockData.isInWall();

        gui.setItem(
                this,
                16,
                inWall,
                blockData.getMaterial(),
                "In Wall",
                "Indicates if the fence gate is attached to a wall, and if true, the texture is lowered by a small amount to blend in better."
        );

        gui.applyState(16, blockData, d -> d.setInWall(!inWall));
    }
}

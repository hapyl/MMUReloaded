package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterMultipleFacing extends BlockFaceAdapter<MultipleFacing> {
    public AdapterMultipleFacing() {
        super(MultipleFacing.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull MultipleFacing blockData) {
        for (BlockFace face : blockData.getAllowedFaces()) {
            if (isUnknownFace(face, player)) {
                continue;
            }

            final int slot = getSlot(face, player);
            final String faceName = getFaceName(face);
            final boolean hasFace = blockData.hasFace(face);

            gui.setItem(this, slot, hasFace, blockData.getMaterial(), faceName, "Whether block is connected to the %s.", faceName);
            gui.applyState(slot, blockData, d -> d.setFace(face, !hasFace));
        }
    }
}

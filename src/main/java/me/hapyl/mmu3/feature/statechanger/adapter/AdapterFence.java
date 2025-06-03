package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerData;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fence;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;

public class AdapterFence extends BlockFaceAdapter<Fence> {

    public AdapterFence() {
        super(Fence.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Fence blockData, @Nonnull StateChangerData data) {
        final Set<BlockFace> allowedFaces = blockData.getAllowedFaces();

        for (BlockFace face : allowedFaces) {
            if (isUnknownFace(face, player)) {
                continue;
            }

            final int slot = getSlot(face, player);
            final String faceName = getFaceName(face);
            final boolean isCurrentFace = blockData.hasFace(face);

            gui.setItem(this, slot, isCurrentFace, blockData.getMaterial(), faceName, "Whether block is connected to the %s.", faceName);
            gui.applyState(slot, blockData, d -> d.setFace(face, !isCurrentFace));
        }
    }
}

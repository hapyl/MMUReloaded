package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterDirectional extends BlockFaceAdapter<Directional> {
    public AdapterDirectional() {
        super(Directional.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Directional blockData) {
        final BlockFace facing = blockData.getFacing();

        for (BlockFace face : blockData.getFaces()) {
            if (isUnknownFace(face)) {
                continue;
            }

            final int slot = getSlot(face);
            final String faceName = getFaceName(face);
            final boolean isFacing = face == facing;

            gui.setItem(this, slot, isFacing, blockData.getMaterial(), faceName, "Whenever block is pointing to the %s.", faceName);

            if (isFacing) {
                continue;
            }

            gui.applyState(slot, blockData, d -> d.setFacing(face));
        }
    }
}

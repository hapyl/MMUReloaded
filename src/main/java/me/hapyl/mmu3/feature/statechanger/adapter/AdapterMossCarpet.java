package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.MossyCarpet;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterMossCarpet extends BlockFaceAdapter<MossyCarpet> {

    private final BlockFace[] allowedFaces = { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

    public AdapterMossCarpet() {
        super(MossyCarpet.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull MossyCarpet blockData) {
        final boolean isBottom = blockData.isBottom();

        // is bottom
        gui.setItem(this, ModifierSlot.FIRST, isBottom, Material.PALE_MOSS_CARPET, "Is Bottom", "Whether the carpet has a bottom block.");
        gui.applyState(ModifierSlot.FIRST, blockData, d -> d.setBottom(!isBottom));

        // height
        for (BlockFace face : allowedFaces) {
            final int slot = getSlot(face, player);
            final MossyCarpet.Height height = blockData.getHeight(face);

            gui.setSwitchItem(
                    this, slot, MossyCarpet.Height.values(), height, blockData.getMaterial(),
                    getFaceName(face), "The height of the moss on a specific face."
            );

            gui.applySwitch(slot, blockData, MossyCarpet.Height.values(), height, (d, h) -> d.setHeight(face, h));
        }
    }
}

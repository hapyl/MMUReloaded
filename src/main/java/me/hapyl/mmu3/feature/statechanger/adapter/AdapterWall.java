package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerData;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Wall;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterWall extends BlockFaceAdapter<Wall> {

    private final BlockFace[] allowedFaces = { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

    public AdapterWall() {
        super(Wall.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Wall blockData, @Nonnull StateChangerData data) {
        final boolean isUp = blockData.isUp();
        final Material material = blockData.getMaterial();

        gui.setItem(this, 22, isUp, material, "Is Up", "Whether the wall has a center post.");
        gui.applyState(22, blockData, d -> d.setUp(!isUp));

        for (BlockFace face : allowedFaces) {
            if (isUnknownFace(face, player)) {
                continue;
            }

            final int slot = getSlot(face, player);
            final Wall.Height height = blockData.getHeight(face);

            gui.setSwitchItem(
                    this, slot,
                    Wall.Height.values(), height, material,
                    getFaceName(face),
                    "The different heights a face of a wall may have."
            );
            gui.applySwitch(slot, blockData, Wall.Height.values(), height, (d, h) -> d.setHeight(face, h));
        }
    }

}

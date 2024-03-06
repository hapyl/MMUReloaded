package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterPointedDripstone extends BlockFaceAdapter<PointedDripstone> {
    public AdapterPointedDripstone() {
        super(PointedDripstone.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull PointedDripstone blockData) {
        final PointedDripstone.Thickness thickness = blockData.getThickness();
        final BlockFace verticalDirection = blockData.getVerticalDirection();

        for (BlockFace face : blockData.getVerticalDirections()) {
            if (isUnknownFace(face, player)) {
                continue;
            }

            final int slot = getSlot(face, player);
            final String faceName = getFaceName(face);
            final boolean isFacing = face == verticalDirection;

            gui.setItem(this, slot, isFacing, blockData.getMaterial(), faceName, "Represents the dripstone orientation.");

            if (!isFacing) {
                gui.applyState(slot, blockData, d -> d.setVerticalDirection(face));
            }
        }

        gui.setSwitchItem(
                this,
                10,
                PointedDripstone.Thickness.values(),
                thickness,
                Material.DRIPSTONE_BLOCK,
                "Thickness",
                "&7Represents the thickness of the dripstone, corresponding to its position within a multi-block dripstone formation.____&8Damn BOI HE THICC"
        );

        gui.applySwitch(10, blockData, PointedDripstone.Thickness.values(), thickness, PointedDripstone::setThickness);
    }
}

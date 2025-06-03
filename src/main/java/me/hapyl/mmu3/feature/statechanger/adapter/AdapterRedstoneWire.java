package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerData;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterRedstoneWire extends BlockFaceAdapter<RedstoneWire> {
    public AdapterRedstoneWire() {
        super(RedstoneWire.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull RedstoneWire blockData, @Nonnull StateChangerData data) {
        for (BlockFace face : blockData.getAllowedFaces()) {
            if (isUnknownFace(face, player)) {
                continue;
            }

            final int slot = getSlot(face, player);
            final String faceName = getFaceName(face);
            final RedstoneWire.Connection connection = blockData.getFace(face);

            gui.setSwitchItem(
                    this, slot,
                    RedstoneWire.Connection.values(),
                    connection,
                    Material.REDSTONE,
                    faceName,
                    "The way in which a redstone wire can connect to an adjacent block face."
            );
            gui.applySwitch(slot, blockData, RedstoneWire.Connection.values(), connection, (d, c) -> d.setFace(face, c));
        }

    }
}

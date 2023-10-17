package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterRotatable extends SwitchAdapter<Rotatable, BlockFace> {

    private final BlockFace[] validFaces = {
            BlockFace.SOUTH,
            BlockFace.SOUTH_WEST,
            BlockFace.WEST_SOUTH_WEST,
            BlockFace.WEST,
            BlockFace.WEST_NORTH_WEST,
            BlockFace.NORTH_WEST,
            BlockFace.NORTH_NORTH_WEST,
            BlockFace.NORTH,
            BlockFace.NORTH_NORTH_EAST,
            BlockFace.NORTH_EAST,
            BlockFace.EAST_NORTH_EAST,
            BlockFace.EAST,
            BlockFace.EAST_SOUTH_EAST,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH_SOUTH_EAST
    };

    public AdapterRotatable() {
        super(Rotatable.class);
    }

    @Nonnull
    @Override
    public BlockFace[] getValues(Rotatable blockData) {
        return validFaces;
    }

    @Nonnull
    @Override
    public BlockFace getCurrentValue(Rotatable blockData) {
        return blockData.getRotation();
    }

    @Nonnull
    @Override
    public BiConsumer<Rotatable, BlockFace> apply() {
        return Rotatable::setRotation;
    }

    @Override
    public int getSlot() {
        return 22;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Rotation";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the current rotation of this block.";
    }
}

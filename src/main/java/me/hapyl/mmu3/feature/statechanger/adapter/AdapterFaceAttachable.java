package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.FaceAttachable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterFaceAttachable extends SwitchAdapter<FaceAttachable, FaceAttachable.AttachedFace> {

    public AdapterFaceAttachable() {
        super(FaceAttachable.class);
    }

    @Nonnull
    @Override
    public FaceAttachable.AttachedFace[] getValues(FaceAttachable blockData) {
        return FaceAttachable.AttachedFace.values();
    }

    @Nonnull
    @Override
    public FaceAttachable.AttachedFace getCurrentValue(FaceAttachable blockData) {
        return blockData.getAttachedFace();
    }

    @Nonnull
    @Override
    public BiConsumer<FaceAttachable, FaceAttachable.AttachedFace> apply() {
        return FaceAttachable::setAttachedFace;
    }


    @Override
    public int getSlot() {
        return 10;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Face";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "The face this block is attached to.";
    }
}

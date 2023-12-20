package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Bell;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterBell extends SwitchAdapter<Bell, Bell.Attachment> {
    public AdapterBell() {
        super(Bell.class);
    }

    @Nonnull
    @Override
    public Bell.Attachment[] getValues(Bell blockData) {
        return Bell.Attachment.values();
    }

    @Nonnull
    @Override
    public Bell.Attachment getCurrentValue(Bell blockData) {
        return blockData.getAttachment();
    }

    @Nonnull
    @Override
    public BiConsumer<Bell, Bell.Attachment> apply() {
        return Bell::setAttachment;
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.BELL;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Attachment";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Denotes how the bell is attacked to its block.";
    }
}

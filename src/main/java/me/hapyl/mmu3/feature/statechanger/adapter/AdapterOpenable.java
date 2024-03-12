package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterOpenable extends Adapter<Openable> {
    public AdapterOpenable() {
        super(Openable.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Openable blockData) {
        final boolean isOpen = blockData.isOpen();

        gui.setItem(this, 10, isOpen, Material.TRIAL_KEY, "Is Open", "Whenever this block is open.");
        gui.applyState(10, blockData, d -> d.setOpen(!isOpen));
    }
}

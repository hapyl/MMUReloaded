package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterPistonHead extends Adapter<PistonHead> {
    public AdapterPistonHead() {
        super(PistonHead.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull PistonHead blockData) {
        final boolean isShort = blockData.isShort();

        gui.setItem(
                this, 10,
                isShort,
                Material.PISTON,
                "Is Short",
                "Denotes if this piston head is shorter than the usual amount because it is currently retracting."
        );

        gui.applyState(10, blockData, d -> d.setShort(!isShort));
    }
}

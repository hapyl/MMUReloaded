package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

public class AdapterOrientable extends Adapter<Orientable> {

    private final Map<Axis, AxisData> axisSlotMap;

    public AdapterOrientable() {
        super(Orientable.class);

        this.axisSlotMap = Map.of(
                Axis.X, new AxisData(21, Material.RED_WOOL),
                Axis.Y, new AxisData(22, Material.GREEN_WOOL),
                Axis.Z, new AxisData(23, Material.BLUE_WOOL)
        );
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Orientable blockData) {
        final Axis axis = blockData.getAxis();

        for (Axis axe : blockData.getAxes()) {
            final boolean isCurrentAxis = axis == axe;
            final AxisData data = axisSlotMap.get(axe);

            if (data == null) {
                continue;
            }

            gui.setItem(this, data.slot, isCurrentAxis, data.material, "Axis", "Whenever this block is pointing towards the %s axis.", axe);

            if (!isCurrentAxis) {
                gui.applyState(data.slot, blockData, d -> d.setAxis(axe));
            }
        }

    }

    private record AxisData(int slot, Material material) {

    }
}

package me.hapyl.mmu3.feature.designer;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class Designer extends Feature {

    private final Map<UUID, DesignerGUI> designers;

    public Designer(Main mmu3plugin) {
        super(mmu3plugin);
        designers = Maps.newHashMap();
    }

    @Nullable
    public DesignerGUI getDesigner(Player player) {
        return designers.get(player.getUniqueId());
    }

    public void setDesigner(Player player, DesignerGUI gui) {
        designers.put(player.getUniqueId(), gui);
    }

}

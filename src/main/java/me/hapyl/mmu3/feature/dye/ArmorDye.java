package me.hapyl.mmu3.feature.dye;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ArmorDye extends Feature {

    // Armor data is stored so changed can be made.
    private final Map<UUID, ArmorData> armorData = Maps.newHashMap();

    public ArmorDye(Main mmu3plugin) {
        super(mmu3plugin);
        setDescription("Allows to colorize items.");
    }

    @Nullable
    public ArmorData getData(Player player) {
        return armorData.get(player.getUniqueId());
    }

    private boolean isDyeable(ItemStack item) {
        return item.getItemMeta() instanceof LeatherArmorMeta;
    }

}

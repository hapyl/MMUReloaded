package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ItemCreatorFeature extends Feature {

    // this stores players creators in case they closed it
    private final Map<UUID, ItemCreator> creators;

    public ItemCreatorFeature(Main mmu3plugin) {
        super(mmu3plugin);

        creators = Maps.newHashMap();
    }

    @Nullable
    public ItemCreator getCreator(Player player) {
        return creators.get(player.getUniqueId());
    }

    public ItemCreator getCreatorOrCreate(Player player) {
        if (!hasCreator(player)) {
            setCreator(player, new ItemCreator(player));
        }
        return getCreator(player);
    }

    public boolean hasCreator(Player player) {
        return creators.containsKey(player.getUniqueId());
    }

    public void setCreator(Player player, ItemCreator creator) {
        creators.put(player.getUniqueId(), creator);
    }

}

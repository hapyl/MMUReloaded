package me.hapyl.mmu3.feature.search;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.List;

public class Search extends Feature {

    private final List<Material> ITEMS;

    public Search(Main mmu3plugin) {
        super(mmu3plugin);

        ITEMS = Lists.newArrayList();

        for (Material value : Material.values()) {
            if (!value.isItem()) {
                continue;
            }

            ITEMS.add(value);
        }
    }

    public List<Material> search(@Nonnull String string) {
        final List<Material> matchingItems = Lists.newArrayList();
        string = string.toLowerCase();

        for (Material item : ITEMS) {
            if (item.name().toLowerCase().contains(string)) {
                matchingItems.add(item);
            }
        }

        return matchingItems;
    }

}

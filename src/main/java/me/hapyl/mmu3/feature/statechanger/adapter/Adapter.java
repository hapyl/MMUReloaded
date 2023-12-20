package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public abstract class Adapter<T extends BlockData> {

    private final Class<T> clazz;
    private final String className;

    public Adapter(@Nonnull Class<T> clazz) {
        this.clazz = clazz;

        final String[] words = getClass().getSimpleName().substring(7).split("(?=[A-Z])");

        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }

        this.className = String.join(" ", words);
    }

    public abstract void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull T blockData);

    public void updateIfInstance(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull BlockData data) {
        if (!clazz.isInstance(data)) {
            return;
        }

        final T blockData = clazz.cast(data);
        update(gui, player, blockData);
    }

    @Override
    public String toString() {
        return ChatColor.DARK_GRAY + className;
    }

    @Nonnull
    public Class<T> getDataClass() {
        return clazz;
    }

    protected ItemBuilder createItem(Material material) {
        return new ItemBuilder(material).addLore(toString()).addLore();
    }

}

package me.hapyl.mmu3.feature.search;

import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.inventory.gui.CancelType;
import me.hapyl.spigotutils.module.inventory.gui.PlayerPageGUI;
import me.hapyl.spigotutils.module.inventory.gui.Properties;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SearchItemGUI extends PlayerPageGUI<Material> {
    public SearchItemGUI(Player player, String query) {
        super(player, "Materials matching \"%s\"".formatted(query), 6);

        final List<Material> result = Main.getRegistry().search.search(query);

        final Properties properties = getProperties();
        properties.setAllowDrag(true);
        properties.setAllowShiftClick(true);
        properties.setAllowNumbersClick(true);

        setCancelType(CancelType.NEITHER);
        setContents(result);
        openInventory(0);
    }

    @Nonnull
    @Override
    public ItemStack asItem(Player player, Material content, int index, int page) {
        return new ItemStack(content);
    }

    @Override
    public void onClick(Player player, Material content, int index, int page) {
    }
}

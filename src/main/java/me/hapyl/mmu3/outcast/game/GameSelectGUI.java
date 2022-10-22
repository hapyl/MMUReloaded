package me.hapyl.mmu3.outcast.game;

import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.DisabledGUI;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GameSelectGUI extends PanelGUI implements DisabledGUI {
    public GameSelectGUI(Player player) {
        super(player, "Game Selection", Size.FOUR);
        updateInventory();
    }

    @Override
    public void updateInventory() {
        final SmartComponent component = newSmartComponent();

        for (Games value : Games.values()) {
            final Game game = value.getGame();
            component.add(new ItemBuilder(Material.DIRT)
                                  .setName("" + (game.getName()))
                                  .addSmartLore(game.getInfo())
                                  .addLore()
                                  .addLore("&eClick to play!")
                                  .build(), player -> game.newInstance(player, Arguments.empty()));
        }

        component.fillItems(this, SlotPattern.CHUNKY, 1);
        openInventory();
    }
}

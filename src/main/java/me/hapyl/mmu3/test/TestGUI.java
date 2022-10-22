package me.hapyl.mmu3.test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestGUI extends PlayerGUI {
    public TestGUI(Player player) {
        super(player, "TEST GUI WITH COOLDOWNS", 3);
        setItem(5, new ItemBuilder(Material.APPLE).setName("1").build(), pl -> {
            pl.sendMessage("&aaaaaaaaaaaaaaaaaaaaa");
        });

        getProperties().setClickCooldown(1000);
        openInventory();
    }
}

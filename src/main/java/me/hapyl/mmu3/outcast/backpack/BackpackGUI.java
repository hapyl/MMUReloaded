package me.hapyl.mmu3.outcast.backpack;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.inventory.gui.CancelType;
import me.hapyl.spigotutils.module.inventory.gui.GUI;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.inventory.item.Event;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackpackGUI extends PlayerGUI {

    private final Backpack backpack;

    public BackpackGUI(Backpack backpack, Player player) {
        super(player, backpack.getName(), backpack.getSize().getSize());
        this.backpack = backpack;

        getInventory().setContents(backpack.getContents());
        setCancelType(CancelType.NEITHER);
        setCloseEvent(this::onClose);
        setAllowDrag(true);
        setAllowShiftClick(true);

        openInventory();

        PlayerLib.playSound(player, Sound.BLOCK_CHEST_OPEN, 1.25f);
        PlayerLib.playSound(player, Sound.ENTITY_HORSE_ARMOR, 0.75f);

        setEventListener(this::onClick);
    }

    @Event
    private void onClick(Player player, GUI gui, InventoryClickEvent ev) {
        final ItemStack item = ev.getCurrentItem();
        if (!Backpack.isBackpackItem(item)) {
            return;
        }

        ev.setCancelled(true);
        Message.error(player, "&cYou cannot put this item in a backpack!");
        Message.sound(player, Sound.ENTITY_VILLAGER_NO);
    }

    @Event
    private void onClose(Player player) {
        this.backpack.contents = getInventory().getContents();
        this.backpack.saveItems();
    }
}

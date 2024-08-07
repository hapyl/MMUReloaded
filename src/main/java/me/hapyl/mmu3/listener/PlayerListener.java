package me.hapyl.mmu3.listener;

import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerListener implements Listener {

    @EventHandler()
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        PersistentPlayerData.createData(player);
    }

    @EventHandler()
    public void handlePlayerQuit(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        PersistentPlayerData.getData(player).saveData();
    }

    // This controls the toggle shift click.
    @EventHandler()
    public void handleTogglePowerable(PlayerInteractEvent ev) {
        final Block clickedBlock = ev.getClickedBlock();
        final Player player = ev.getPlayer();

        if (clickedBlock == null
                || !player.isOp()
                || !player.isSneaking()
                || player.getGameMode() != GameMode.CREATIVE
                || ev.getAction() == Action.LEFT_CLICK_BLOCK
                || ev.getHand() == EquipmentSlot.OFF_HAND
                || !player.getInventory().getItemInMainHand().getType().isAir()) {
            return;
        }

        final BlockData blockData = clickedBlock.getBlockData();

        //ev.setCancelled(true); // Not sure if cancelling is necessary.

        // The checks must be in this order, do not change.
        if (blockData instanceof Lightable lightable) {
            lightable.setLit(!lightable.isLit());
            clickedBlock.setBlockData(lightable, false);

            Message.success(player, "%s is now %s.", Chat.capitalize(clickedBlock.getType()), lightable.isLit() ? "lit" : "unlit");
        }
        else if (blockData instanceof Openable openable) {
            openable.setOpen(!openable.isOpen());
            clickedBlock.setBlockData(openable, false);

            Message.success(player, "%s is now %s.", Chat.capitalize(clickedBlock.getType()), openable.isOpen() ? "open" : "closed");
        }
        else if (blockData instanceof Powerable powerable) {
            powerable.setPowered(!powerable.isPowered());
            clickedBlock.setBlockData(powerable, false);

            Message.success(player, "%s is now %s.", Chat.capitalize(clickedBlock.getType()), powerable.isPowered() ? "on" : "off");
        }
    }

    // Handle slot interact for checking slot number.
    @EventHandler()
    public void handleInventoryClickEvent(InventoryClickEvent ev) {
        final ClickType click = ev.getClick();
        final HumanEntity player = ev.getWhoClicked();

        if (click == ClickType.SHIFT_RIGHT && player.isOp()) {
            final int rawSlot = ev.getRawSlot();

            ev.setCancelled(true);
            Message.info((Player) player, "&fClicked &l%s&f. &7(%s of %% 9)", rawSlot, rawSlot % 9);
        }
    }

}

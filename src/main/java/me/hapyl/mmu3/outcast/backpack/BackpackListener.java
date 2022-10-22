package me.hapyl.mmu3.outcast.backpack;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.InjectListener;
import me.hapyl.spigotutils.module.inventory.gui.GUI;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class BackpackListener extends InjectListener {

    private final Map<UUID, Long> lastOpened;

    public BackpackListener(Main main) {
        super(main);
        this.lastOpened = Maps.newHashMap();
    }

    @EventHandler()
    public void handleDrop(PlayerDropItemEvent ev) {
        final Player player = ev.getPlayer();
        if (isInBackpack(player)) {
            ev.getItemDrop().remove();
            ev.setCancelled(true);
        }
    }

    @EventHandler()
    public void handlePickup(EntityPickupItemEvent ev) {
        final LivingEntity entity = ev.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        if (isInBackpack(player)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler()
    public void handleSwapHands(PlayerSwapHandItemsEvent ev) {
        final Player player = ev.getPlayer();
        if (isInBackpack(player)) {
            ev.setCancelled(true);
            return;
        }
    }

    @EventHandler()
    public void handleInteract(PlayerInteractEvent ev) {
        final Action action = ev.getAction();
        if ((action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) || ev.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        final Player player = ev.getPlayer();
        final ItemStack item = ev.getItem();
        final Backpack backpack = Backpack.loadFromItemStack(item);

        if (backpack == null) {
            return;
        }

        ev.setCancelled(true);

        // Open backpack from a backpack cancel
        if (isInBackpack(player)) {
            Message.error(player, "Cannot open backpack in current state!");
            return;
        }

        final long lastOpenedAt = lastOpened.getOrDefault(player.getUniqueId(), 0L);
        if (lastOpenedAt != 0L && System.currentTimeMillis() - lastOpenedAt <= 1000L) {
            Message.error(player, "You're opening backpacks too fast, slot down!");
            Message.sound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 0.0f);
            return;
        }

        backpack.open(player);
        lastOpened.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private boolean isInBackpack(Player player) {
        final GUI gui = GUI.getPlayerGUI(player);
        return gui != null && gui.getName().contains("Backpack");
    }

}

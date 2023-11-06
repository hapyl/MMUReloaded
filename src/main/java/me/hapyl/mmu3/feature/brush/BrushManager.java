package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class BrushManager extends Feature implements Listener {

    private final Map<UUID, PlayerBrush> playerBrushMap;

    public BrushManager(Main mmu3plugin) {
        super(mmu3plugin);
        this.playerBrushMap = Maps.newConcurrentMap();
    }

    public PlayerBrush getBrush(Player player) {
        return playerBrushMap.computeIfAbsent(player.getUniqueId(), PlayerBrush::new);
    }

    @EventHandler()
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final Action action = ev.getAction();

        if (ev.getHand() == EquipmentSlot.OFF_HAND || action == Action.PHYSICAL) {
            return;
        }

        final PlayerBrush brush = getBrush(player);
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        if (brush.getBrush() == Brushes.NONE || handItem.getType() != brush.getBrushItem()) {
            return;
        }

        // Left click is undo
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            final long currentMillis = System.currentTimeMillis();
            final boolean canUndo = brush.canUndo();

            if (!canUndo) {
                Chat.sendActionbar(player, "&c&lɴᴏᴛʜɪɴɢ ᴛᴏ ᴜɴᴅᴏ");
                return;
            }

            if (currentMillis - brush.lastUndoUsage >= 2000) {
                brush.lastUndoUsage = currentMillis;
                Chat.sendActionbar(player, "&6&lʟᴇғᴛ ᴄʟɪᴄᴋ ᴀɢᴀɪɴ ᴛᴏ ᴜɴᴅᴏ");
                PlayerLib.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f);
                return;
            }

            brush.lastUndoUsage = 0;
            brush.undo(1);

            Chat.sendActionbar(player, "&a&lᴜɴᴅɪᴅ ʟᴀsᴛ ᴀᴄᴛɪᴏɴ");
            PlayerLib.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.25f);
            return;
        }

        final Block block = player.getTargetBlockExact(100, FluidCollisionMode.ALWAYS);

        if (block == null) {
            Message.error(player, "No valid block in sight!");
            return;
        }

        brush.useBrush(block.getLocation());
        ev.setCancelled(true);
    }

    public void resetBrush(Player player) {
        playerBrushMap.put(player.getUniqueId(), new PlayerBrush(player.getUniqueId()));
    }
}

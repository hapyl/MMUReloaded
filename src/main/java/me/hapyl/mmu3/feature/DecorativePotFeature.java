package me.hapyl.mmu3.feature;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DecoratedPot;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nullable;
import java.util.Map;

public class DecorativePotFeature extends Feature implements Listener {
    private final Map<BlockFace, DecoratedPot.Side[]> directionToSideMap;

    public DecorativePotFeature(Main mmu3plugin) {
        super(mmu3plugin);

        directionToSideMap = Maps.newHashMap();
        directionToSideMap.put(
                BlockFace.NORTH,
                new DecoratedPot.Side[] { DecoratedPot.Side.BACK, DecoratedPot.Side.FRONT, DecoratedPot.Side.LEFT, DecoratedPot.Side.RIGHT }
        );

        directionToSideMap.put(
                BlockFace.SOUTH,
                new DecoratedPot.Side[] { DecoratedPot.Side.FRONT, DecoratedPot.Side.BACK, DecoratedPot.Side.RIGHT, DecoratedPot.Side.LEFT }
        );

        directionToSideMap.put(
                BlockFace.WEST,
                new DecoratedPot.Side[] { DecoratedPot.Side.RIGHT, DecoratedPot.Side.LEFT, DecoratedPot.Side.BACK, DecoratedPot.Side.FRONT }
        );

        directionToSideMap.put(
                BlockFace.EAST,
                new DecoratedPot.Side[] { DecoratedPot.Side.LEFT, DecoratedPot.Side.RIGHT, DecoratedPot.Side.FRONT, DecoratedPot.Side.BACK }
        );
    }

    @EventHandler()
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final Block clickedBlock = ev.getClickedBlock();

        if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE) {
            return;
        }

        if (ev.getAction() != Action.RIGHT_CLICK_BLOCK || ev.getHand() == EquipmentSlot.OFF_HAND || clickedBlock == null) {
            return;
        }

        if (clickedBlock.getType() != Material.DECORATED_POT || !(clickedBlock.getState() instanceof DecoratedPot decoratedPot)) {
            return;
        }

        final Directional directional = (Directional) decoratedPot.getBlockData();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack heldItem = inventory.getItemInMainHand();
        final Material type = heldItem.getType();

        if (!isSherdItem(heldItem)) {
            return;
        }

        final BlockFace clickedFace = ev.getBlockFace();
        final DecoratedPot.Side side = getSideFromFace(directional, clickedFace);

        if (side == null) {
            Message.error(player, "Invalid side somehow? Report this!");
            return;
        }

        final Material sherd = decoratedPot.getSherd(side);

        if (sherd == type) {
            return;
        }

        ev.setCancelled(true);
        decoratedPot.setSherd(side, type);
        decoratedPot.update(true, false);

        player.swingMainHand();
        Message.success(player, "Set sherd to %s.", Chat.capitalize(type));
        PlayerLib.playSound(player, Sound.BLOCK_DECORATED_POT_SHATTER, 0.0f);
    }

    public boolean isSherdItem(ItemStack item) {
        final Material type = item.getType();

        if (type.isAir()) {
            return false;
        }

        return type == Material.BRICK || Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(type);
    }

    @Nullable
    public DecoratedPot.Side getSideFromFace(Directional directional, BlockFace face) {
        final BlockFace facing = directional.getFacing();
        final DecoratedPot.Side[] sides = this.directionToSideMap.get(facing);

        if (sides == null) {
            return null;
        }

        return sides[getFaceIndex(face)];
    }

    private int getFaceIndex(BlockFace face) {
        return switch (face) {
            case NORTH -> 0;
            case SOUTH -> 1;
            case WEST -> 2;
            case EAST -> 3;
            default -> throw new IllegalArgumentException("Invalid argument!");
        };
    }
}

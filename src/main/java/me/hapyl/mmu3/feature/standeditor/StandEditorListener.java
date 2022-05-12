package me.hapyl.mmu3.feature.standeditor;

import kz.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.utils.InjectListener;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class StandEditorListener extends InjectListener {

    public StandEditorListener(Main main) {
        super(main);
    }

    @EventHandler()
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final EquipmentSlot hand = ev.getHand();
        final Action action = ev.getAction();

        if (!player.isOp() || hand == EquipmentSlot.OFF_HAND) {
            return;
        }

        if ((action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)) {
            if (Main.getStandEditor().isEditingItem(ev.getItem())) {
                new StandTuningGUI(player);
            }
        }

        final Data data = standEditor().getData(player);
        if (data != null && data.isWaitForMove() && data.canLeaveMoveMode()) {
            ev.setCancelled(true);
            standEditor().exitMoveMode(data);
        }
    }

    @EventHandler()
    public void handlePlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent ev) {
        final Player player = ev.getPlayer();
        final Entity rightClicked = ev.getRightClicked();

        if (!player.isOp() || ev.getHand() == EquipmentSlot.OFF_HAND || !(rightClicked instanceof ArmorStand stand)) {
            return;
        }

        final StandEditor editor = standEditor();

        // todo > add non-packet hologram test

        // Subtract tuning
        if (editor.isEditingItem(player.getInventory().getItemInMainHand())) {
            ev.setCancelled(true);
            editor.tuneStand(player, stand, editor.getTuneDataOrNew(player), player.isSneaking() ? -0.01d : -0.1d);
            return;
        }

        if (player.isSneaking()) {
            ev.setCancelled(true);

            if (editor.isTaken(stand)) {
                Message.error(player, "%s is currently editing this armor stand!", editor.getTakerName(stand));
                Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                return;
            }

            new StandEditorGUI(player, new Data(player, stand));
        }
    }

    @EventHandler()
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Player player) || !player.isOp() || !(ev.getEntity() instanceof ArmorStand stand)) {
            return;
        }

        final StandEditor editor = standEditor();
        if (!editor.isEditingItem(player.getInventory().getItemInMainHand())) {
            final Data data = standEditor().getData(player);
            if (data != null && data.isWaitForMove() && data.canLeaveMoveMode()) {
                ev.setCancelled(true);
                ev.setDamage(0.0d);
                standEditor().exitMoveMode(data);
            }
            return;
        }

        ev.setCancelled(true);
        ev.setDamage(0.0d);

        editor.tuneStand(player, stand, editor.getTuneDataOrNew(player), player.isSneaking() ? 0.01 : 0.1);
    }

    @EventHandler()
    public void handleToggleSneakEvent(PlayerToggleSneakEvent ev) {
        Player player = ev.getPlayer();
        final Data data = standEditor().getData(player);
        if (!player.isOp() || data == null || !data.isWaitForMove() || !player.isSneaking()) {
            return;
        }

        moveStand(data, MoveDirection.DOWN);
    }

    @EventHandler()
    public void handlePlayerSwapHandEvent(PlayerSwapHandItemsEvent ev) {
        Player player = ev.getPlayer();
        final Data data = standEditor().getData(player);
        if (!player.isOp() || data == null || !data.isWaitForMove()) {
            return;
        }

        double moveSpeed = data.getSpeed();
        double nextSpeed = moveSpeed == 0.1 ? 0.01 : (moveSpeed == 0.01 ? 1.0 : 0.1);
        data.setSpeed(nextSpeed);

        Chat.sendTitle(
                player,
                "&aCurrent Speed",
                "%s[0.1] %s[0.01] %s[1.0]".formatted(
                        nextSpeed == 0.1 ? "&b&l" : "&8",
                        nextSpeed == 0.01 ? "&b&l" : "&8",
                        nextSpeed == 1.0 ? "&b&l" : "&8"
                ),
                0,
                20,
                0
        );

        Message.sound(player, Sound.UI_BUTTON_CLICK, 1.0F);
    }

    @EventHandler()
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final Data data = standEditor().getData(player);
        if (data == null) {
            return;
        }

        standEditor().setTaken(data, false);
    }

    @EventHandler()
    public void handleEntityDeathEvent(EntityDeathEvent ev) {
        if (!(ev.getEntity() instanceof ArmorStand stand)) {
            return;
        }

        standEditor().checkStandRemoval(stand, null);
    }

    @EventHandler()
    public void handleEntityDeathByEntityEvent(EntityDamageByEntityEvent ev) {
        if (!(ev.getEntity() instanceof ArmorStand stand)) {
            return;
        }

        if (ev.getFinalDamage() >= stand.getHealth()) {
            standEditor().checkStandRemoval(stand, ev.getDamager());
        }
    }

    @EventHandler()
    public void handlePlayerMoveEvent(PlayerMoveEvent ev) {
        final Player player = ev.getPlayer();
        final Data data = standEditor().getData(player);
        if (!player.isOp() || data == null || !data.isWaitForMove()) {
            return;
        }

        final Location from = ev.getFrom();
        final Location to = ev.getTo();

        if (to == null || (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ())) {
            return;
        }

        ev.setCancelled(true);
        if (to.getX() - from.getX() >= 0.09) {
            moveStand(data, MoveDirection.X_PLUS);
        }

        if (to.getX() - from.getX() <= -0.09) {
            moveStand(data, MoveDirection.X_MINUS);
        }

        if (to.getZ() - from.getZ() >= 0.09) {
            moveStand(data, MoveDirection.Z_PLUS);
        }

        if (to.getZ() - from.getZ() <= -0.09) {
            moveStand(data, MoveDirection.Z_MINUS);
        }

        if (to.getY() - from.getY() >= 0.1) {
            moveStand(data, MoveDirection.UP);
        }
    }

    private void moveStand(Data data, MoveDirection direction) {
        final ArmorStand stand = data.getStand();
        stand.teleport(stand.getLocation().add(getRelativeVector(data.getSpeed(), direction)));
    }

    private Vector getRelativeVector(double speed, MoveDirection direction) {
        return switch (direction) {
            case X_PLUS -> new Vector(speed, 0.0, 0.0);
            case X_MINUS -> new Vector(-speed, 0.0, 0.0);
            case Z_PLUS -> new Vector(0.0, 0.0, speed);
            case Z_MINUS -> new Vector(0.0, 0.0, -speed);
            case UP -> new Vector(0.0, speed, 0.0);
            case DOWN -> new Vector(0.0, -speed, 0.0);
        };
    }

}

package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.InjectListener;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class StandEditorListener extends InjectListener {

    public StandEditorListener(Main main) {
        super(main);
    }

    @EventHandler()
    public void handlePlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent ev) {
        final Player player = ev.getPlayer();
        final Entity rightClicked = ev.getRightClicked();

        if (!player.isOp() || ev.getHand() == EquipmentSlot.OFF_HAND
                || !(rightClicked instanceof ArmorStand stand)
                || player.getGameMode() != GameMode.CREATIVE) {
            return;
        }

        final StandEditor editor = standEditor();

        if (player.isSneaking()) {
            ev.setCancelled(true);

            if (editor.isTaken(stand)) {
                Message.error(player, "%s is currently editing this armor stand!", editor.getTakerName(stand));
                Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                return;
            }

            final StandEditorData data = editor.getData(player);
            data.edit(stand);

            new StandEditorGUI(player, data);
        }
    }

    @EventHandler()
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        standEditor().remove(ev.getPlayer());
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

}

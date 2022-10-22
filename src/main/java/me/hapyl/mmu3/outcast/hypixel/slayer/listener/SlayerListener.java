package me.hapyl.mmu3.outcast.hypixel.slayer.listener;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.outcast.hypixel.slayer.Slayer;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerActiveQuest;
import me.hapyl.mmu3.outcast.hypixel.slayer.State;
import me.hapyl.mmu3.utils.InjectListener;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SlayerListener extends InjectListener {

    public SlayerListener(Main main) {
        super(main);
    }

    @EventHandler()
    public void handlePlayerDeath(PlayerDeathEvent ev) {
        final Player player = ev.getEntity();
        final Slayer slayer = Main.getRegistry().slayer;
        final SlayerActiveQuest active = slayer.getActiveQuest(player);

        if (active == null || active.getState() != State.BOSS) {
            return;
        }

        active.setState(State.FAILED);
        Slayer.sendMessage(player, "&c&lSLAYER QUEST FAILED!");
        Slayer.sendMessage(player, " &7Deaths are not permitted.");
    }

    // Make sure slayer always targets its summoner
    @EventHandler()
    public void handleBossAggro(EntityTargetLivingEntityEvent ev) {
        final Entity entity = ev.getEntity();
        final LivingEntity target = ev.getTarget();
    }

    // Slayer Boss Death Check
    @EventHandler()
    public void handleSlayerBossDeath(EntityDeathEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Player player = entity.getKiller();

        if (entity instanceof Player || (player == null)) {
            return;
        }

        final Slayer slayer = Main.getRegistry().slayer;
        final SlayerActiveQuest active = slayer.findBossQuest(entity);

        if (active == null) {
            return;
        }

        active.setState(State.SUCCESS);

        Slayer.sendMessage(player, "&a&lSLAYER BOSS SLAIN! NICE!");
        Slayer.sendMessage(player, " &7Use &e/slayer &7to claim your reward.");
    }

    // Player Quest Check
    @EventHandler()
    public void handleExpGain(EntityDeathEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Player player = entity.getKiller();

        if (entity instanceof Player || (player == null)) {
            return;
        }

        final Slayer slayer = Main.getRegistry().slayer;
        final SlayerActiveQuest active = slayer.getActiveQuest(player);

        if (active == null || active.getState() != State.ACTIVE) {
            return;
        }

        if (!active.getQuest().isAllowedType(entity.getType())) {
            return;
        }

        final long expToAdd = Math.max(1, ev.getDroppedExp());
        active.addExp(entity, expToAdd);
        PlayerLib.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f);
        Chat.sendActionbar(player, "&3&lSLAYER! &3+%s Exp", expToAdd);
    }


}

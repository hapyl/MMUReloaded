package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Report;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ColoredSign extends Feature implements Listener {
    public ColoredSign(Main mmu3plugin) {
        super(mmu3plugin);
    }

    @EventHandler()
    public void handleSignChangeEvent(SignChangeEvent ev) {
        final Player player = ev.getPlayer();
        if (!player.isOp()) {
            return;
        }

        for (int i = 0; i < ev.getLines().length; i++) {
            final String line = ev.getLine(i);
            ev.setLine(i, Chat.format(line));
        }

        ev.getBlock().getState().update(false, false);
    }

    @EventHandler()
    public void handleSignClickEvent(PlayerInteractEvent ev) {
        final Block block = ev.getClickedBlock();
        final Player player = ev.getPlayer();

        if ((ev.getHand() == EquipmentSlot.OFF_HAND) || (ev.getAction() != Action.RIGHT_CLICK_BLOCK) || (!isSign(block))) {
            return;
        }

        if ((!player.isSneaking()) || (!player.getInventory().getItemInMainHand().getType().isAir())) {
            Report.COLORED_SIGN_INVALID_USAGE.send(player);
            return;
        }

        final Sign sign = (Sign) block.getState();

        // convert color to editable char
        for (int i = 0; i < sign.getLines().length; i++) {
            sign.setLine(i, sign.getLine(i).replace("§", "&"));
        }

        sign.setEditable(true);
        sign.update(false, false);

        runTaskLater(() -> player.openSign(sign), 2L);
    }

    private boolean isSign(Block block) {
        return (block != null && block.getState() instanceof Sign);
    }

}

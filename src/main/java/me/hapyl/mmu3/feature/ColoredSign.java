package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

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

    private boolean isSign(Block block) {
        return (block != null && block.getState() instanceof Sign);
    }

}

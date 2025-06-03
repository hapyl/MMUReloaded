package me.hapyl.mmu3.feature.dotcommand;

import me.hapyl.eterna.module.inventory.gui.PlayerGUI;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public class DotCommand extends Feature implements Listener {

    private final TranslateRussian translateRussian;

    public DotCommand(Main mmu3plugin) {
        super(mmu3plugin);
        translateRussian = new TranslateRussian();
    }

    @EventHandler()
    public void handleAsyncChatEvent(AsyncPlayerChatEvent ev) {
        final Player player = ev.getPlayer();
        final String message = ev.getMessage();

        if (!message.startsWith(".")) {
            return;
        }

        ev.setCancelled(true);
        String strippedMessage = message.substring(1);

        // Check for russian words
        if (Pattern.matches(".*\\p{InCyrillic}.*", strippedMessage)) {
            final StringBuilder builder = new StringBuilder();
            for (char c : strippedMessage.toCharArray()) {
                builder.append(translateRussian.getEnglishChar(c));
            }

            strippedMessage = builder.toString();
        }

        final String finalStrippedMessage = strippedMessage;
        Message.info(player, "Converted '%s' %s '/%s'.", message, PlayerGUI.ARROW_FORWARD, finalStrippedMessage);

        Runnables.runSync(() -> {
            Bukkit.dispatchCommand(player, finalStrippedMessage);
        });
    }
}

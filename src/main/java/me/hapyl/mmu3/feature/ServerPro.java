package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.SoundQueue;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ServerPro extends Feature {

    private final String stringTenMinutes = "§6Server §6will §6expire §6in §610 §6minutes.";
    private final String stringFiveMinutes = "§6Server §6will §6expire §6in §65 §6minutes.";

    private final SoundQueue soundTenMinutes;
    private final SoundQueue soundFiveMinutes;

    public ServerPro(Main mmu3plugin) {
        super(mmu3plugin);

        soundTenMinutes = new SoundQueue()
                .appendSameSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1.3f, 0, 5, 7)
                .append(Sound.BLOCK_NOTE_BLOCK_PLING, 1.5f, 4)
                .append(Sound.BLOCK_NOTE_BLOCK_PLING, 1.7F, 2);

        soundFiveMinutes = new SoundQueue()
                .appendSameSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0, 4, 6)
                .append(Sound.BLOCK_NOTE_BLOCK_PLING, 1.25f, 3)
                .append(Sound.BLOCK_NOTE_BLOCK_PLING, 1.35f, 2);
    }

    public String getStringTenMinutes() {
        return stringTenMinutes;
    }

    public String getStringFiveMinutes() {
        return stringFiveMinutes;
    }

    public void announce(boolean ten) {
        Message.broadcastAdmins("%s minutes before server expires!%s", ten ? 10 : 5, ten ? "" : " &c&lHurry Up!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOp()) {
                continue;
            }

            Chat.sendTitle(player, "&cServer Expires", String.format("&4&lin %s minutes!", ten ? 10 : 5), 5, 40, 5);
        }

        addListenersAndPlay(ten ? soundTenMinutes : soundFiveMinutes);
    }

    public void addListenersAndPlay(SoundQueue queue) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                queue.addListener(player);
            }
        }
        queue.play();
    }

}

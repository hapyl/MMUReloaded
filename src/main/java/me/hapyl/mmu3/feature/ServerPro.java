package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.player.SoundQueue;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ServerPro extends Feature {

    private final String stringTenMinutes = "Server will expire in 10 minutes.";
    private final String stringFiveMinutes = "Server will expire in 5 minutes.";

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
        Message.broadcastAdmins("%s minutes before expires!%s", ten ? 10 : 5, ten ? "" : " &c&lHurry Up!");
        addListenersAndPlay(ten ? soundTenMinutes : soundFiveMinutes);
    }

    public void addListenersAndPlay(SoundQueue queue) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            queue.addListener(player);
        }
        queue.play();
    }

}

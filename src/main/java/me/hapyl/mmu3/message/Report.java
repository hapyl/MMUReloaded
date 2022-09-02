package me.hapyl.mmu3.message;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public enum Report {

    COLORED_SIGN_INVALID_USAGE(Type.ERROR, "You must Shift+Right-Click with an empty hand to edit a sign!", minutes(1)),

    ;

    private final Map<UUID, Long> lastSeen = Maps.newHashMap();

    private final Type type;
    private final String info;
    private final long cooldown;

    Report(Type type, String info, long cooldown) {
        this.type = type;
        this.info = info;
        this.cooldown = cooldown;
    }

    public void send(Player player, Object... replacements) {
        // last seen check
        final long last = this.lastSeen.getOrDefault(player.getUniqueId(), 0L);

        if (last == 0 || ((System.currentTimeMillis() - last) >= cooldown)) {
            switch (type) {
                case INFO -> Message.info(player, info, replacements);
                case SUCCESS -> Message.success(player, info, replacements);
                case DEBUG -> Message.debug(player, info, replacements);
                case ERROR -> Message.error(player, info, replacements);
            }
            lastSeen.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    private static long seconds(long l) {
        return l * 1000;
    }

    private static long minutes(long l) {
        return l * 60000;
    }

    private enum Type {
        INFO,
        ERROR,
        SUCCESS,
        DEBUG
    }
}

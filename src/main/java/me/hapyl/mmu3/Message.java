package me.hapyl.mmu3;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum Message {

    NO_PERMISSIONS(Type.ERROR, "No permissions."),
    NOT_ENOUGH_ARGUMENTS(Type.ERROR, "Not enough arguments."),
    NOT_ENOUGH_ARGUMENTS_EXPECTED(Type.ERROR, NOT_ENOUGH_ARGUMENTS.value + " Expected %s."),
    NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST(Type.ERROR, NOT_ENOUGH_ARGUMENTS.value + " Expected at least %s."),
    TOO_MANY_ARGUMENTS(Type.ERROR, "Too many arguments."),
    TOO_MANY_ARGUMENTS_EXPECTED(Type.ERROR, TOO_MANY_ARGUMENTS.value + " Expected %s."),
    TOO_MANY_ARGUMENTS_EXPECTED_NOT_MORE_THAN(Type.ERROR, TOO_MANY_ARGUMENTS.value + " Expected not more than %s."),
    INVALID_ARGUMENT_SIZE(Type.ERROR, "Invalid arguments length. Expected %s."),

    ;

    private final Type type;
    private final String value;
    private final Sound sound;
    private final float pitch;
    private final int expectedReplacements;

    public static final String PREFIX = "&c&lMMU3 &8➤ &7";
    public static final String ARROW = "➤";

    Message(Type type, String value) {
        this(type, value, null, 0.0f);
    }

    Message(Type type, String value, Sound sound) {
        this(type, value, sound, 1.0f);
    }

    Message(Type type, String value, Sound sound, float pitch) {
        this.type = type;
        this.value = value;
        this.sound = sound;
        this.pitch = pitch;
        this.expectedReplacements = StringUtils.countMatches(value, "%s");
    }

    public void send(Player player, Object... replacements) {
        if (replacements.length != expectedReplacements) {
            severe(
                    player,
                    "Invalid arguments length in %s! Expected %s, provided %s. Printed stack trace to console.",
                    name().toLowerCase(),
                    expectedReplacements,
                    replacements.length
            );
            new IllegalArgumentException().printStackTrace();
            return;
        }

        switch (type) {
            case INFO -> info(player, value, replacements);
            case ERROR -> error(player, value, replacements);
            case SUCCESS -> success(player, value, replacements);
            case SEVERE -> severe(player, value, replacements);
        }

        if (sound != null) {
            sound(player, sound, pitch);
        }
    }

    // static members
    public static void info(Player player, String message, Object... replacements) {
        raw(player, ChatColor.GRAY, ChatColor.YELLOW, message, replacements);
    }

    public static void debug(Player player, String message, Object... replacements) {
        raw(player, ChatColor.GRAY, ChatColor.RED, message, replacements);
    }

    public static void success(Player player, String message, Object... replacements) {
        raw(player, ChatColor.GREEN, ChatColor.DARK_GREEN, message, replacements);
    }

    public static void error(Player player, String message, Object... replacements) {
        raw(player, ChatColor.RED, ChatColor.DARK_RED, message, replacements);
    }

    public static void severe(Player player, String message, Object... replacements) {
        raw(player, ChatColor.DARK_RED, ChatColor.BOLD, message, replacements);
    }

    public static void broadcast(String message, Object... replacements) {
        Chat.broadcast(PREFIX + "Broadcast &8➤ &7" + message.formatted(replacements));
    }

    public static void broadcastAdmins(String message, Object... replacements) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                info(player, message, replacements);
            }
        }
    }

    public static void sound(Player player, Sound sound, float pitch) {
        PlayerLib.playSound(player, sound, pitch);
    }

    public static void sound(Player player, Sound sound) {
        sound(player, sound, 1.0f);
    }

    private static void raw(Player player, ChatColor color, ChatColor replacementsColor, String message, Object... replacements) {
        final Object[] coloredReplacements = new String[replacements.length];

        for (int i = 0; i < replacements.length; i++) {
            coloredReplacements[i] = replacementsColor.toString() + replacements[i].toString() + color.toString();
        }

        Chat.sendMessage(player, PREFIX + color + message, coloredReplacements);
    }

    public enum Type {
        INFO, ERROR, SUCCESS, SEVERE
    }

}

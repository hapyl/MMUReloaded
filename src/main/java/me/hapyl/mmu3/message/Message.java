package me.hapyl.mmu3.message;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.player.PlayerLib;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Message {

    // ** Errors ** //
    NO_PERMISSIONS(Type.ERROR, "No permissions."),

    NOT_ENOUGH_ARGUMENTS(Type.ERROR, "Not enough arguments."),
    NOT_ENOUGH_ARGUMENTS_EXPECTED(Type.ERROR, NOT_ENOUGH_ARGUMENTS.value + " Expected %s."),
    NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST(Type.ERROR, NOT_ENOUGH_ARGUMENTS.value + " Expected at least %s."),

    TOO_MANY_ARGUMENTS(Type.ERROR, "Too many arguments."),
    TOO_MANY_ARGUMENTS_EXPECTED(Type.ERROR, TOO_MANY_ARGUMENTS.value + " Expected %s."),
    TOO_MANY_ARGUMENTS_EXPECTED_NOT_MORE_THAN(Type.ERROR, TOO_MANY_ARGUMENTS.value + " Expected not more than %s."),

    INVALID_ARGUMENT_SIZE(Type.ERROR, "Invalid arguments length. Expected %s."),
    INVALID_ARGUMENT(Type.ERROR, "%s is not a valid argument!"),
    INVALID_ARGUMENT_TYPE(Type.ERROR, "%s is expected to be %s, not %s!"),

    PLAYER_NOT_ONLINE(Type.ERROR, "%s is not online!"),

    // ** Success Messages ** //
    LOGIN_SUCCESSFUL(Type.SUCCESS, "Successfully logged in!"),

    ;

    public static final String PREFIX = "&c&lMMU3 &8➤ &7";
    public static final String PREFIX_DEBUG = "&c&lMMU3 &8&lDebug &8➤ &7";
    public static final String PREFIX_BROADCAST = "&6&lMMU3 &8➤ &e";
    public static final String ARROW = "➤";
    private final Type type;
    private final String value;
    private final Sound sound;
    private final float pitch;
    private final int expectedReplacements;

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

    public static boolean checkError(boolean condition, Player player, String string, Object... replacements) {
        if (!condition) {
            error(player, string, replacements);
        }
        return condition;
    }

    public static boolean checkNull(Object nullCheck, Player player, String string, Object... replacements) {
        if (nullCheck == null) {
            error(player, string, replacements);
            return true;
        }
        return false;
    }

    // static members
    public static void info(CommandSender sender, String message, Object... replacements) {
        raw(sender, ChatColor.GRAY, ChatColor.YELLOW, message, replacements);
    }

    public static void debug(CommandSender sender, String message, Object... replacements) {
        raw(sender, ChatColor.GRAY, ChatColor.RED, message, replacements);
    }

    public static void debug(Object message, Object... replacements) {
        Chat.broadcastOp((PREFIX_DEBUG + message).formatted(colorReplacements(ChatColor.GRAY, ChatColor.RED, replacements)));
    }

    public static void success(CommandSender sender, String message, Object... replacements) {
        raw(sender, ChatColor.GREEN, ChatColor.DARK_GREEN, message, replacements);
    }

    public static void error(CommandSender sender, String message, Object... replacements) {
        raw(sender, ChatColor.RED, ChatColor.DARK_RED, message, replacements);
    }

    public static void severe(CommandSender sender, String message, Object... replacements) {
        raw(sender, ChatColor.DARK_RED, ChatColor.BOLD, message, replacements);
    }

    public static void click(CommandSender sender, ClickEvent event, Object prompt, Object... replacements) {
        Chat.sendClickableMessage(
                sender,
                event,
                (PREFIX + prompt.toString()).formatted(colorReplacements(ChatColor.GRAY, ChatColor.YELLOW, replacements))
        );
    }

    public static void clickHover(CommandSender sender, ClickEvent event, HoverEvent eventHover, Object prompt, Object... replacements) {
        Chat.sendClickableHoverableMessage(
                sender,
                event,
                eventHover,
                (PREFIX + prompt.toString()).formatted(colorReplacements(ChatColor.GRAY, ChatColor.YELLOW, replacements))
        );
    }

    public static void broadcast(String message, Object... replacements) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Chat.sendHoverableMessage(
                    player,
                    LazyEvent.showText("&e&oThis is a broadcast message. Everyone on the server sees it."),
                    PREFIX_BROADCAST + message.formatted(replacements)
            );
        });
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

    public static Object[] colorReplacements(ChatColor color, ChatColor replacementsColor, Object... replacements) {
        return colorReplacements(color.toString(), replacementsColor.toString(), replacements);
    }

    public static Object[] colorReplacements(String color, String replacementsColor, Object... replacements) {
        final Object[] coloredReplacements = new String[replacements.length];

        for (int i = 0; i < replacements.length; i++) {
            coloredReplacements[i] = replacementsColor + replacements[i].toString() + color;
        }

        return coloredReplacements;
    }

    private static void raw(CommandSender player, ChatColor color, ChatColor replacementsColor, String message, Object... replacements) {
        Chat.sendMessage(
                player,
                (PREFIX + color + message).formatted(colorReplacements(color.toString(), replacementsColor.toString(), replacements))
        );
    }

    public enum Type {
        INFO,
        ERROR,
        SUCCESS,
        SEVERE
    }

}

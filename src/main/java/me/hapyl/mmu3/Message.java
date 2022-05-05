package me.hapyl.mmu3;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Message {

    private static final String PREFIX = "&c&lMMU3 &8➤ &7";

    public static void info(Player player, String message, Object... replacements) {
        raw(player, ChatColor.GRAY, ChatColor.GREEN, message, replacements);
    }

    public static void success(Player player, String message, Object... replacements) {
        raw(player, ChatColor.GREEN, ChatColor.BOLD, message, replacements);
    }

    public static void error(Player player, String message, Object... replacements) {
        raw(player, ChatColor.RED, ChatColor.BOLD, message, replacements);
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

}

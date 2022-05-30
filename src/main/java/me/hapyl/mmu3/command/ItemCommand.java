package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.util.Validate;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * This class allows to create semi-complicated items using chat commands instead of GUI menu.
 */
public class ItemCommand extends SimplePlayerAdminCommand {

    //**/
    //**/ /item (Id) [Arguments]
    //**/
    //**/ Arguments are pairs of key:value strings.
    //**/

    public ItemCommand(String name) {
        super(name);
        setAliases("i");
        setDescription("Allows to create semi-complicated items. Use /i help for help.");

        for (Material value : Material.values()) {
            if (value.isItem()) {
                addCompleterValue(1, value.name().toLowerCase());
            }
        }

        addCompleterValues(2, "name()", "lore()", "smart()", "enchant()");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length < 1) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final String arg = args[0];
        if (arg.equalsIgnoreCase("help")) {
            sendHelp(player);
            return;
        }

        Material material;
        int amount;

        if (arg.contains(":")) {
            final String[] split = arg.split(":");
            material = Validate.getEnumValue(Material.class, split[0]);
            amount = NumberConversions.toInt(split[1]);
        }
        else {
            material = Validate.getEnumValue(Material.class, arg);
            amount = 1;
        }

        if (material == null) {
            Message.error(player, "%s is not a valid material!", arg);
            getSimilarString(Material.values(), arg, player);
            return;
        }

        final ItemBuilder builder = new ItemBuilder(material);
        builder.setAmount(amount);

        if (args.length >= 2) {
            if (!parseArgumentsAndApply(player, builder, Chat.arrayToString(args, 1))) {
                return;
            }
        }

        player.getInventory().addItem(builder.toItemStack());
        Message.info(player, "Gave x%s %s to %s.", builder.getAmount(), material.name(), player.getName());
    }

    public boolean parseArgumentsAndApply(Player player, ItemBuilder builder, String string) {
        final String nameSubstring = findBetween(string, "name");
        final String loreSubstring = findBetween(string, "lore");
        final String smartLoreSubstring = findBetween(string, "smart");
        final String enchantSubstring = findBetween(string, "enchant");

        if (nameSubstring != null) {
            builder.setName(nameSubstring);
        }

        if (loreSubstring != null) {
            if (loreSubstring.contains(",")) {
                final String[] lines = loreSubstring.split(",");
                for (String line : lines) {
                    builder.addLore(line.trim());
                }
            }
            else {
                builder.addLore(loreSubstring);
            }
        }

        if (smartLoreSubstring != null) {
            builder.addSmartLore(smartLoreSubstring);
        }

        // enchant(sharpness:5)
        if (enchantSubstring != null) {
            final String[] enchants = (enchantSubstring.contains(",") ? enchantSubstring.split(",") : new String[] { enchantSubstring });
            for (String str : enchants) {
                if (!str.contains(":")) {
                    Message.error(player, "Unable to parse %s as enchant! Missing %s.", str, ":");
                    Message.error(player, "Example: sharpness:5");
                    return false;
                }

                final String[] split = str.split(":");
                final Enchantment enchantment = getEnchantByName(split[0]);
                final int lvl = NumberConversions.toInt(split[1]);

                if (enchantment == null) {
                    Message.error(player, "%s is invalid enchantment!", split[0]);
                    getSimilarString(getEnchantNames(), split[0], player);
                    return false;
                }

                if (lvl <= 0 || lvl >= Short.MAX_VALUE) {
                    Message.error(player, "Enchantment level cannot be less than 0 or more greater than %s.", Short.MAX_VALUE);
                    return false;
                }

                builder.addEnchant(enchantment, lvl);
            }
        }

        return true;
    }

    @Nullable
    private Enchantment getEnchantByName(String name) {
        name = name.toLowerCase().trim();
        for (Enchantment value : Enchantment.values()) {
            if (value.getKey().getKey().contains(name)) {
                return value;
            }
        }
        return null;
    }

    private String[] getEnchantNames() {
        final Enchantment[] values = Enchantment.values();
        final String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = values[i].getKey().getKey();
        }
        return strings;
    }

    @Nullable
    private String findBetween(String string, String value) {
        return StringUtils.substringBetween(string, value + "(", ")");
    }

    private void getSimilarString(Object[] values, String str, Player player) {
        final String similar = getSimilarString(values, str);
        if (similar != null) {
            Message.error(player, "Did you meant %s?", similar);
        }
    }

    @Nullable
    private String getSimilarString(Object[] values, String str) {
        str = str.toLowerCase();
        for (Object t : values) {
            final String lowerCase = (t instanceof Enum<?> e ? e.name() : t instanceof NamespacedKey n ? n.getKey() : t.toString()).toLowerCase(
                    Locale.ROOT);
            if (lowerCase.contains(str)) {
                return lowerCase;
            }
        }
        return null;
    }

    private void sendHelp(Player player) {
        Message.info(player, "&b&m-----------------------------------");
        Message.info(player, "Basic Usage: %s [Material] or %s [Material]:[Amount]", getUsage(), getUsage());
        Message.info(player, "");
        Message.info(player, "Arguments are strings, formatted like 'argumentName(argumentValue)'");
        Message.info(player, "which are separated by space if multiple.");
        Message.info(player, "");
        Message.info(player, "&b&lValid Arguments:");
        Message.info(player, "Name: &aname(Insert Item Name)");
        Message.info(player, "Lore: &alore(First Lore Line, Another lore line, And another!)");
        Message.info(player, "Smart Lore: &asmart(Smart lore splits line automatically at the best spots!)");
        Message.info(player, "Enchants: &aenchant(shaprness:2) enchant(unbreaking:10, looting:69, thorns:420)");
        Message.info(player, "&b&m-----------------------------------");
    }
}

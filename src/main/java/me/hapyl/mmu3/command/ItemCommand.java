package me.hapyl.mmu3.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.util.Enums;
import me.hapyl.spigotutils.module.util.Validate;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * This class allows to create semi-complicated items using chat commands instead of GUI menu.
 */
public class ItemCommand extends SimplePlayerAdminCommand {

    //**/
    //**/ /item (Id) [Arguments]
    //**/
    //**/ Arguments are pairs of key:value strings.
    //**/

    private final Map<Help, String[]> helpPages = Maps.newHashMap();

    public ItemCommand(String name) {
        super(name);
        setAliases("i");
        setDescription("Allows to create semi-complicated items. Use /i help for help.");

        for (Material value : Material.values()) {
            if (value.isItem()) {
                addCompleterValue(1, value.name().toLowerCase());
            }
        }

        addCompleterValues(1, "self");

        setHelp(
                Help.GENERAL,
                "Basic Usage: %s <Material> or %s <Material>:<Amount>".formatted(getUsage(), getUsage()),
                "Using &e'self' &7in place of material name will modify held item."
        );

        setHelp(
                Help.ABOUT_ARGUMENTS,
                "Arguments are strings, formatted like 'argumentName(argumentValue)'",
                "which are separated by space if multiple."
        );

        setHelp(
                Help.VALID_ARGUMENTS,
                "&aName: &3name(&bYour Name&3)",
                "&aLore: &3lore(&bThis is lore, And another line&3)",
                "&aSmart Lore: &3smart(&bSmart lore automatically splits the lore lines!&3)",
                "&aEnchants: &3enchant(&bsharpness:2&3) enchant(&bunbreaking:10&3, &blooting:69&3, &bthorns:420&3)"
        );

    }

    public boolean parseArgumentsAndApply(Player player, ItemBuilder builder, String string) {
        final String nameSubstring = findBetween(string, "name");
        final String loreSubstring = findBetween(string, "lore");
        final String smartLoreSubstring = findBetween(string, "smart");
        final String enchantSubstring = findBetween(string, "enchant");

        // Process name
        if (nameSubstring != null) {
            builder.setName(nameSubstring);
        }

        // Process lore
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

        // Process enchants
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

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length < 1) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final String arg = args[0];
        if (arg.equalsIgnoreCase("help")) {
            sendHelp(player, args.length >= 2 ? Enums.byName(Help.class, args[1], Help.GENERAL) : Help.GENERAL);
            return;
        }

        boolean usingSelf = false;
        Material material;
        int amount = 1;

        final PlayerInventory playerInventory = player.getInventory();

        // Parse material
        if (arg.contains(":")) {
            final String[] split = arg.split(":");
            material = Validate.getEnumValue(Material.class, split[0]);
            amount = NumberConversions.toInt(split[1]);
        }
        // Check for self to modify items
        else if (arg.equalsIgnoreCase("self") || arg.equals("_")) {
            final ItemStack itemInMainHand = playerInventory.getItemInMainHand();
            material = itemInMainHand.getType();
            amount = itemInMainHand.getAmount();
            usingSelf = true;
        }
        // Default to arg parsing
        else {
            material = Validate.getEnumValue(Material.class, arg);
        }

        if (material == null) {
            // Try finding similar material
            final String similarString = getSimilarString(Material.values(), arg);
            if (similarString != null) {
                final Material similarMaterial = Validate.getEnumValue(Material.class, similarString);
                if (similarMaterial != null) {
                    material = similarMaterial;
                    Message.error(player, "Could not find %s material, using %s instead.", arg, Chat.capitalize(similarMaterial));
                }
            }

            if (material == null) {
                Message.error(player, "Invalid material %s.", arg);
                return;
            }
        }

        if (material.isAir()) {
            Message.error(player, "Cannot give nor modify air.");
            return;
        }

        final ItemBuilder builder = usingSelf ? new ItemBuilder(playerInventory.getItemInMainHand()) : ItemBuilder.of(material);
        builder.setAmount(amount);

        // Parse and apply arguments if any
        if (args.length >= 2) {
            if (!parseArgumentsAndApply(player, builder, Chat.arrayToString(args, 1))) {
                return;
            }
        }

        final ItemStack itemStack = builder.build();

        if (usingSelf) {
            playerInventory.setItem(playerInventory.getHeldItemSlot(), itemStack);
            Message.info(player, "Modified held item.");
        }
        else {
            playerInventory.addItem(itemStack);
            Message.info(player, "Gave x%s %s to %s.", builder.getAmount(), Chat.capitalize(material), player.getName());
        }
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            return completerSort(Lists.newArrayList("name()", "lore()", "smart()", "enchant()"), args);
        }

        return super.tabComplete(sender, args);
    }

    private void setHelp(Help help, String... strings) {
        helpPages.put(help, strings);
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
            Message.error(player, "Did you mean %s?", similar);
        }
    }

    @Nullable
    private String getSimilarString(Object[] values, String str) {
        str = str.toLowerCase();
        for (Object t : values) {
            final String lowerCase = (t instanceof Enum<?> e ? e.name() : t instanceof NamespacedKey n ? n.getKey() : t.toString()).toLowerCase();
            if (lowerCase.contains(str)) {
                return lowerCase;
            }
        }
        return null;
    }


    private void sendHelp(Player player, Help help) {
        final String[] helpStrings = helpPages.get(help);

        Message.info(player, "&b&m----------[&a&l %s Help &a&l[&b&m----------".formatted(Chat.capitalize(help.name())));
        for (String string : helpStrings) {
            Message.info(player, string);
        }

        // Other help
        Message.info(player, "");
        final Help previous = Enums.getPreviousValue(Help.class, help);
        final Help next = Enums.getNextValue(Help.class, help);

        Message.clickHover(
                player,
                LazyEvent.runCommand("%s help %s".formatted(getUsage().toLowerCase(), previous)),
                LazyEvent.showText("&7Click to see help for %s!".formatted(Chat.capitalize(previous))),
                "&e&lCLICK &eto see help about %s.",
                Chat.capitalize(previous)
        );

        Message.clickHover(
                player,
                LazyEvent.runCommand("%s help %s".formatted(getUsage(), next)),
                LazyEvent.showText("&7Click to see help for %s!".formatted(Chat.capitalize(next))),
                "&e&lCLICK &eto see help about %s.",
                Chat.capitalize(next)
        );

        Message.info(player, "&b&m------------------------------", Chat.capitalize(help.name()));

    }

    private enum Help {

        GENERAL,
        ABOUT_ARGUMENTS,
        VALID_ARGUMENTS

    }

}

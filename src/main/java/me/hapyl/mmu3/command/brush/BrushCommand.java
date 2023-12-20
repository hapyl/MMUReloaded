package me.hapyl.mmu3.command.brush;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.brush.*;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import me.hapyl.spigotutils.module.util.Wrap;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BrushCommand extends SimplePlayerAdminCommand {

    private final Wrap wrapper = new Wrap() {
        @Override
        public String start() {
            return "";
        }

        @Override
        public String between() {
            return ", ";
        }

        @Override
        public String end() {
            return ".";
        }
    };

    private final List<Material> blocks;

    public BrushCommand(String name) {
        super(name);
        setAliases("mb");
        setUsage("mb help");
        setDescription("Simple brush usage implementation.");

        blocks = Lists.newArrayList();
        for (Material value : Material.values()) {
            if (value.isBlock()) {
                blocks.add(value);
            }
        }

        for (Brushes value : Brushes.values()) {
            addCompleterValues(1, value.name());
        }

        addCompleterValues(1, "help", "tool", "mask", "undo", "reset", "data");
    }

    public void trySetBrush(PlayerBrush playerBrush, Brushes brush) {
        playerBrush.setBrush(brush);
        Message.info(playerBrush.getPlayer(), "Set brush to %s.", Chat.capitalize(brush));
    }

    public void trySetSize(PlayerBrush playerBrush, int size) {
        playerBrush.setSize(size);
        Message.info(playerBrush.getPlayer(), "Set brush size to %s.", size);
    }

    public void trySetMaterial(PlayerBrush playerBrush, Material material) {
        final Player player = playerBrush.getPlayer();
        if (material == null || !material.isBlock()) {
            Message.error(player, "Material must be a block!");
            return;
        }
        playerBrush.setMaterial(material);
        Message.info(player, "Set brush item to %s.", Chat.capitalize(material));
    }

    public void trySetTool(PlayerBrush playerBrush, Material material) {
        final Player player = playerBrush.getPlayer();
        if (!material.isItem()) {
            Message.error(player, "Brush material must be an item!");
            return;
        }
        playerBrush.setBrushItem(material);
        Message.info(player, "Changed brush material to %s.", Chat.capitalize(material));
    }

    @Override
    protected void execute(Player player, String[] args) {
        final BrushManager manager = Main.getRegistry().brushManager;
        final PlayerBrush playerBrush = manager.getBrush(player);

        if (args.length == 0) {
            Message.success(player, "MMU Brush's Info:");
            Message.info(player, "You're using %s brush with size of %s.", Chat.capitalize(playerBrush.getBrush()), playerBrush.getSize());
            Message.info(player, "Brush material is %s.", Chat.capitalize(playerBrush.getMaterial()));
            Message.info(player, "Brush item is %s.", Chat.capitalize(playerBrush.getBrushItem()));
            if (playerBrush.getMask().isEmpty()) {
                Message.info(player, "Brush mask is empty.");
            }
            else {
                Message.info(player, "Brush mask: %s", playerBrush.getMaskFormatted());
            }
            return;
        }

        final String firstArgument = args[0];

        // ** Single argument check
        switch (firstArgument.toLowerCase()) {
            // mb data NAME VALUE
            case "data" -> {
                final Brushes enumBrush = playerBrush.getBrush();
                final Brush brush = enumBrush.getBrush();

                if (brush == null) {
                    Message.error(player, "You don't have a brush!");
                    return;
                }

                if (!brush.hasExtraData()) {
                    Message.error(player, "This brush doesn't accept any extra data!");
                    return;
                }

                if (args.length == 1) {
                    final Map<String, BrushData<?>> data = brush.getAcceptingExtraData();
                    Message.success(player, "This brush accepts the following data:");

                    data.forEach((key, brushData) -> {
                        Message.info(player, "'%s' of type: %s, defaults to %s.", key, brushData.getType(), brushData.getDefaultValue());
                    });
                    return;
                }

                final String key = getArgument(args, 1).toString();
                final String value = getArgument(args, 2).toString();

                final BrushData<?> data = brush.getExtraDataByName(key);

                if (data == null) {
                    Message.error(player, "This brush doesn't accept any values named '%s'!", key);
                    return;
                }

                if (value.isEmpty()) {
                    final Object playerValue = data.getPlayerValue(player);
                    Message.info(player, "Current brush data '%s': %s.", key, playerValue);
                    return;
                }

                final Object newValue = data.setPlayerValue(player.getUniqueId(), value);
                Message.success(player, "Set brush data '%s' to %s!", key, newValue);

                return;
            }

            case "help" -> {
                displayHelp(player);
                return;
            }

            case "tool" -> {
                trySetTool(playerBrush, player.getInventory().getItemInMainHand().getType());
                return;
            }

            case "undo", "u" -> {
                final int times = args.length == 2 ? Validate.getInt(args[1]) : 1;
                final int undid = playerBrush.undo(times);

                if (undid == 0) {
                    Message.info(player, "Nothing to undo!");
                    return;
                }

                Message.info(player, "Undid %s available operations!", undid);
                return;
            }

            case "reset" -> {
                manager.resetBrush(player);
                Message.success(player, "Reset your brush.");
                return;
            }

            case "mask", "m" -> {
                if (args.length == 1) {
                    playerBrush.resetMask();
                    Message.info(player, "Reset your mask.");
                    return;
                }

                final Set<Material> newMask = Sets.newHashSet();

                for (int i = 1; i < args.length; i++) {
                    final Material material = Validate.getEnumValue(Material.class, args[i]);

                    if (material == null || !material.isBlock()) {
                        Message.error(player, "%s is invalid material or not a block!", args[i]);
                        return;
                    }

                    newMask.add(material);
                }

                playerBrush.setMask(newMask);
                Message.info(player, "Applied new mask of: %s.", playerBrush.getMaskFormatted());
                Message.info(player, "Use '/mb mask' to reset mask.");
                return;
            }

        }

        // ** Brush Type change
        final Brushes brush = Validate.getEnumValue(Brushes.class, firstArgument);

        if (brush != null) {
            trySetBrush(playerBrush, brush);
            return;
        }

        // ** Brush Size change
        final int size = Validate.getInt(firstArgument);

        if (size > 0 && size < 50) {
            trySetSize(playerBrush, size);
            return;
        }

        // ** Brush Material change
        final Material material = Validate.getEnumValue(Material.class, firstArgument);

        if (material != null) {
            trySetMaterial(playerBrush, material);
            return;
        }

        Message.error(player, "Invalid usage! %s", getUsage());
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1 || (args.length >= 2 && args[0].equalsIgnoreCase("mask"))) {
            return completerSort(blocks, args);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("data")) {
            if (!(sender instanceof Player player)) {
                return null;
            }

            final PlayerBrush playerBrush = Main.getRegistry().brushManager.getBrush(player);
            final Brush brush = playerBrush.getBrush().getBrush();

            if (brush == null) {
                return null;
            }

            return completerSort2(brush.getAcceptingExtraDataKeys(), args, false);
        }

        return null;
    }

    private void displayHelp(Player player) {
        Message.success(player, "MMU Brush Help:");
    }

}

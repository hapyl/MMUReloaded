package me.hapyl.mmu3.command.brush;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.brush.Brush;
import me.hapyl.mmu3.feature.brush.BrushManager;
import me.hapyl.mmu3.feature.brush.PlayerBrush;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import me.hapyl.spigotutils.module.util.Validate;
import me.hapyl.spigotutils.module.util.Wrap;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
        setAliases("tb");
        setUsage("tb (Brush Name/Material/Size) OR tb <tool/mask/reset/undo> [mask:Material...]");
        setDescription("Simple brush usage implementation.");

        blocks = Lists.newArrayList();
        for (Material value : Material.values()) {
            if (value.isBlock()) {
                blocks.add(value);
            }
        }

        for (Brush value : Brush.values()) {
            addCompleterValues(1, value.name());
        }

        addCompleterValues(1, "help", "tool", "mask", "undo", "reset");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final BrushManager manager = Main.getRegistry().brushManager;
        final PlayerBrush playerBrush = manager.getBrush(player);

        // tb (Brush)
        // tb (Material)
        // tb (Int)
        // tb mask (Material...)
        // tb tool
        // tb reset

        if (args.length == 0) {
            Message.success(player, "Tiny Brush's Info:");
            Message.info(player, "Your using %s brush with size of %s.", Chat.capitalize(playerBrush.getBrush()), playerBrush.getSize());
            Message.info(player, "Brush material is %s.", Chat.capitalize(playerBrush.getMaterial()));
            Message.info(player, "Brush item is %s.", Chat.capitalize(playerBrush.getBrushItem()));
            if (playerBrush.getMask().isEmpty()) {
                Message.info(player, "Brush mask is empty.");
            }
            else {
                Message.info(player, "Brush mask: %s", CollectionUtils.wrapToString(playerBrush.getMask(), wrapper));
            }
            return;
        }

        final String firstArgument = args[0];

        // ** Single argument check
        switch (firstArgument.toLowerCase()) {

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
                final int queueSize = playerBrush.getQueueSize();
                final int undid = playerBrush.undo(times);

                if (undid == 0) {
                    Message.info(player, "Nothing to undo!");
                    return;
                }

                Message.info(player, "Undid %s of %s available operations!", undid, queueSize);
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

                for (int i = 1; i < args.length; i++) {
                    final Material material = Validate.getEnumValue(Material.class, args[i]);
                    if (material == null || !material.isBlock()) {
                        Message.error(player, "%s is invalid material or not a block!", args[i]);
                        return;
                    }

                    playerBrush.addMask(material);
                }

                Message.info(player, "Applied new mask of: %s.", CollectionUtils.wrapToString(playerBrush.getMask(), wrapper));
                Message.info(player, "Use '/tb mask' to reset mask.");
                return;
            }

        }

        // ** Brush Type change
        final Brush brush = Validate.getEnumValue(Brush.class, firstArgument);

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

    private void displayHelp(Player player) {
        Message.success(player, "Tiny Brush Help:");
    }

    public void trySetBrush(PlayerBrush playerBrush, Brush brush) {
        playerBrush.setBrush(brush);
        Message.info(playerBrush.getPlayer(), "Set brush material to %s.", Chat.capitalize(brush));
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
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1 || (args.length >= 2 && args[0].equalsIgnoreCase("mask"))) {
            return completerSort(blocks, args);
        }
        return null;
    }

}

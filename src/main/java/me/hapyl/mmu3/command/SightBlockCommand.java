package me.hapyl.mmu3.command;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SightBlockCommand extends SimplePlayerAdminCommand {

    private final Set<Material> staticMaterialSet;

    public SightBlockCommand(String name) {
        super(name);
        setDescription("Allows to change the sight block.");
        setAliases("sb");

        staticMaterialSet = Sets.newHashSet();
        for (Material value : Material.values()) {
            if (value.isBlock()) {
                staticMaterialSet.add(value);
            }
        }
    }

    @Override
    protected void execute(Player player, String[] args) {
        // sb piston
        // sb piston[block data] -d 1
        if (args.length < 1) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        String blockDataString = null;
        String arg = args[0];
        Shift shift = Shift.NONE;
        int shiftAmount = 1;

        if (arg.contains("[") && arg.endsWith("]")) {
            blockDataString = arg.substring(arg.lastIndexOf("["));
            arg = arg.substring(0, arg.lastIndexOf("["));
        }

        final Material material = Validate.getEnumValue(Material.class, arg);
        if (material == null) {
            Message.error(player, "%s is invalid material!", arg);
            return;
        }

        if (!material.isBlock()) {
            Message.error(player, "%s is not a block!", Chat.capitalize(material));
            return;
        }

        if (args.length >= 2) {
            final String arg1 = args[1];
            if (!arg1.startsWith("-")) {
                Message.error(player, "Argument must start with a dash! '-'");
                return;
            }

            shift = Shift.byValue(arg1.replace("-", ""));
            if (shift == null) {
                Message.error(player, "%s is invalid argument!", arg1);
                return;
            }

            if (args.length >= 3) {
                shiftAmount = NumberConversions.toInt(args[2]);
            }
        }

        setBlock(player, material, blockDataString, shift, shiftAmount);
    }

    private void setBlock(Player player, Material material, String blockDataString, Shift shift, int shiftAmount) {
        Block targetBlock = player.getTargetBlockExact(10);

        if (targetBlock == null) {
            Message.error(player, "You must look at a block to change it.");
            return;
        }

        final Location location = targetBlock.getLocation();

        if (shift != Shift.NONE) {
            switch (shift) {
                case UP -> location.add(0.0d, shiftAmount, 0.0d);
                case DOWN -> location.add(0.0d, -shiftAmount, 0.0d);
                case NORTH -> location.add(0.0d, 0.0d, -shiftAmount);
                case SOUTH -> location.add(0.0d, 0.0d, shiftAmount);
                case WEST -> location.add(-shiftAmount, 0.0d, 0.0d);
                case EAST -> location.add(shiftAmount, 0.0d, 0.0d);
            }
            // update target block
            targetBlock = location.getBlock();
        }

        final String blockName = Chat.capitalize(material);
        targetBlock.setType(material, false);
        Message.info(player, "Replaced sight block to %s.", blockName);

        // Parse and apply block data
        if (blockDataString != null) {
            final String validData = Bukkit.createBlockData(material).getAsString();
            try {
                final BlockData data = Bukkit.createBlockData(material, blockDataString);
                targetBlock.setBlockData(data, false);
                Message.info(player, "Applied %s data to %s.", blockDataString, blockName);
            } catch (IllegalArgumentException e) {
                Message.error(player, "%s is not a valid data for %s.", blockDataString, blockName);
                Message.error(player, "%s only supports %s block data!", blockName, validData.substring(validData.lastIndexOf("[")));
            }
        }

    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(staticMaterialSet, args);
        }
        return Collections.emptyList();
    }

    private enum Shift {
        NONE("§"),
        UP("u"),
        DOWN("d"),
        NORTH("n"),
        SOUTH("s"),
        WEST("w"),
        EAST("e");

        private final String value;

        Shift(String value) {
            this.value = value;
        }

        public static Shift byValue(String value) {
            for (Shift shift : values()) {
                if (shift.value.equalsIgnoreCase(value)) {
                    return shift;
                }
            }
            return null;
        }
    }

}

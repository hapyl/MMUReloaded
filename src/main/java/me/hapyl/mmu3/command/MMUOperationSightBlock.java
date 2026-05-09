package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.util.NullableTuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MMUOperationSightBlock implements MMUOperation {

    private static final Map<String, Material> BLOCK_NAME_TO_BLOCK_MAP = Arrays.stream(Material.values())
            .filter(Material::isBlock)
            .map(material -> Map.entry(material.name().toLowerCase(), material))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    @Override
    public @NotNull String name() {
        return "sight_block";
    }

    @Override
    public @NotNull String description() {
        return "Allows changing the target block.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder().where(0, BLOCK_NAME_TO_BLOCK_MAP.keySet());
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        // sb piston
        // sb piston[block data] -d 1
        if (args.length < 1) {
            MMULogger.error(player, Component.text("Missing required argument (block)."));
            return;
        }

        final NullableTuple<Material, AlmostBlockData> tuple = parseBlockData(args.get(0).toString());

        final ShiftDirection shiftDirection = ShiftDirection.parse(args.get(1).toString());
        final int shift = args.get(2).toInt(1);

        final Material material = tuple.a();
        final AlmostBlockData almostBlockData = tuple.b();

        if (material == null) {
            MMULogger.error(player, Component.text("Invalid material %s!".formatted(args.get(0))));
            return;
        }

        Block targetBlock = player.getTargetBlockExact(10);

        if (targetBlock == null) {
            MMULogger.error(player, Component.text("No valid block in sight!"));
            return;
        }

        final Location location = targetBlock.getLocation();

        // Apply shift
        if (shiftDirection != ShiftDirection.NONE && shift > 0) {
            location.add(shiftDirection.vector().multiply(shift));

            // Update target bock
            targetBlock = location.getBlock();
        }

        // If data doesn't exist, just simply set the block
        if (almostBlockData == null) {
            targetBlock.setType(material, false);

            MMULogger.success(
                    player,
                    Component.empty()
                            .append(Component.text("Set the sight block to "))
                            .append(Component.translatable(material.name()))
                            .append(Component.text("."))
            );
        }
        // Else try to parse the data before setting the block
        else {
            BlockData blockData;

            try {
                blockData = material.createBlockData(almostBlockData.blockData());
            } catch (Exception ex) {
                blockData = null;
            }

            // If block data is null, it means the argument is malformed
            if (blockData == null) {
                final String compatibleData = material.createBlockData().getAsString();
                final String compatibleDataSubstring = compatibleData.substring(compatibleData.indexOf("["));

                MMULogger.error(player, Component.text("Failed to set the sight block due to malformed block data!"));
                MMULogger.error(player, Component.text(" Incompatible data: %s".formatted(compatibleDataSubstring), NamedTextColor.RED));
                MMULogger.error(player, Component.text(" Compatible data: %s".formatted(almostBlockData.blockData()), NamedTextColor.GREEN));
            }
            else {
                targetBlock.setBlockData(blockData, false);

                MMULogger.success(
                        player,
                        Component.empty()
                                .append(Component.text("Set the sight block to "))
                                .append(Component.translatable(material.name()))
                                .append(Component.text(" with the following block data:"))
                );

                MMULogger.info(player, formatBlockData(blockData.getAsString(true)));
            }
        }
    }

    @NotNull
    private static Component formatBlockData(@NotNull String blockData) {
        final TextComponent.Builder builder = Component.text();

        for (String subData : blockData.split(",")) {
            final String[] keyValuePair = subData.split(":");

            // Malformed
            if (keyValuePair.length != 2) {
                continue;
            }

            if (!builder.children().isEmpty()) {
                builder.append(Component.text(", ", NamedTextColor.DARK_GRAY));
            }

            builder.append(Component.text(keyValuePair[0], NamedTextColor.WHITE));
            builder.append(Component.text(" = ", NamedTextColor.GRAY));
            builder.append(Component.text(keyValuePair[1], NamedTextColor.GREEN));
        }

        return builder.build();
    }

    @NotNull
    private static NullableTuple<Material, AlmostBlockData> parseBlockData(@NotNull String string) {
        // Parse block data
        String blockDataString;

        if (string.contains("[") && string.endsWith("]")) {
            final int index = string.indexOf("[");

            blockDataString = string.substring(0, index);
            string = string.substring(0, index);
        }
        else {
            blockDataString = "";
        }

        // Parse material
        final Material block = BLOCK_NAME_TO_BLOCK_MAP.get(string.toLowerCase());

        return block != null
                ? NullableTuple.of(block, blockDataString.isEmpty() ? null : (AlmostBlockData) () -> blockDataString)
                : NullableTuple.empty();
    }

    private enum ShiftDirection {
        NONE("§", new Vector(0, 0, 0)),
        UP("-u", new Vector(0, 1, 0)),
        DOWN("-d", new Vector(0, -1, 0)),
        NORTH("-n", new Vector(0, 0, -1)),
        SOUTH("-s", new Vector(0, 0, 1)),
        WEST("-w", new Vector(-1, 0, 0)),
        EAST("-e", new Vector(1, 0, 0));

        private final String value;
        private final Vector vector;

        ShiftDirection(@NotNull String value, @NotNull Vector vector) {
            this.value = value;
            this.vector = vector;
        }

        public @NotNull Vector vector() {
            return new Vector(vector.getX(), vector.getY(), vector.getZ());
        }

        @NotNull
        public static ShiftDirection parse(@NotNull String value) {
            for (ShiftDirection shiftDirection : values()) {
                if (shiftDirection.value.equalsIgnoreCase(value)) {
                    return shiftDirection;
                }
            }

            return NONE;
        }
    }

    interface AlmostBlockData {

        @NotNull
        String blockData();

    }

}

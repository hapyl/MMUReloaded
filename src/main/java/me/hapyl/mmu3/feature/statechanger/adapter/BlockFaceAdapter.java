package me.hapyl.mmu3.feature.statechanger.adapter;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.util.Direction;
import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class BlockFaceAdapter<T extends BlockData> extends Adapter<T> {

    private static final int UNKNOWN_FACE = 0;
    private final Map<Direction, Map<BlockFace, Integer>> blockFaceMap = Maps.newHashMap();

    public BlockFaceAdapter(@Nonnull Class<T> clazz) {
        super(clazz);

        mapDirection(Direction.NORTH, 13, 31, 21, 23, 12, 14, 30, 32);
        mapDirection(Direction.SOUTH, 31, 13, 23, 21, 32, 30, 14, 12);
        mapDirection(Direction.WEST, 23, 21, 13, 31, 14, 32, 12, 30);
        mapDirection(Direction.EAST, 21, 23, 31, 13, 30, 12, 32, 14);
    }

    @Nonnull
    public String getFaceName(@Nonnull BlockFace face) {
        return Chat.capitalize(face);
    }

    public boolean isUnknownFace(@Nonnull BlockFace face, @Nonnull Player player) {
        return isUnknownFace(face, player.getLocation());
    }

    public boolean isUnknownFace(@Nonnull BlockFace face, @Nonnull Location location) {
        return getSlot(face, location) == UNKNOWN_FACE;
    }

    public int getSlot(@Nonnull BlockFace face, @Nonnull Player player) {
        return getSlot(face, player.getLocation());
    }

    public int getSlot(@Nonnull BlockFace face, @Nonnull Location location) {
        final Direction direction = Direction.getDirection(location);
        final Map<BlockFace, Integer> faces = blockFaceMap.get(direction);

        if (faces == null) {
            return UNKNOWN_FACE;
        }

        return faces.getOrDefault(face, UNKNOWN_FACE);
    }

    private void mapDirection(
            Direction direction,
            int north,
            int south,
            int west,
            int east,
            int northWest,
            int northEast,
            int southWest,
            int southEast
    ) {
        blockFaceMap.put(
                direction,
                Map.of(
                        BlockFace.NORTH, north,
                        BlockFace.SOUTH, south,
                        BlockFace.WEST, west,
                        BlockFace.EAST, east,
                        BlockFace.NORTH_WEST, northWest,
                        BlockFace.NORTH_EAST, northEast,
                        BlockFace.SOUTH_WEST, southWest,
                        BlockFace.SOUTH_EAST, southEast,
                        BlockFace.UP, 4,
                        BlockFace.DOWN, 40
                )
        );
    }

}

package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class Brush {

    private final String name;
    private final Map<String, BrushData<?>> acceptingExtraData = Maps.newHashMap();
    private boolean cancelPhysics;

    public Brush(String name) {
        this.name = name;
        this.cancelPhysics = true;
    }

    public void setCancelPhysics(boolean cancelPhysics) {
        this.cancelPhysics = cancelPhysics;
    }

    public boolean isCancelPhysics() {
        return cancelPhysics;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Returns collected block with brush pattern.
     *
     * @param player   - Player, who used the brush.
     * @param location - Location of the center.
     * @param radius   - Radius.
     * @return collection of blocks.
     */
    @Nullable
    public abstract Collection<Block> collect(@Nonnull Player player, @Nonnull NonNullWorldLocation location, double radius);

    public boolean hasExtraData() {
        return !acceptingExtraData.isEmpty();
    }

    public boolean hasExtraDataByName(String key) {
        return acceptingExtraData.containsKey(key.toLowerCase());
    }

    @Nullable
    public BrushData<?> getExtraDataByName(String key) {
        return acceptingExtraData.get(key);
    }

    @Nonnull
    public Map<String, BrushData<?>> getAcceptingExtraData() {
        return acceptingExtraData;
    }

    @Nonnull
    public List<String> getAcceptingExtraDataKeys() {
        return Lists.newArrayList(acceptingExtraData.keySet());
    }

    @Nonnull
    protected <T> BrushData<T> addExtraData(String key, BrushDataType<T> type, T def) {
        final BrushData<T> extraData = new BrushData<>(key, type, def);
        acceptingExtraData.put(key, extraData);

        return extraData;
    }

}

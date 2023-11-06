package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class BrushData<T> {

    private final String key;
    private final BrushDataType<T> type;
    private final T defaultValue;
    private final Map<UUID, T> playerValues;

    public BrushData(String key, BrushDataType<T> type, T defaultValue) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
        this.playerValues = Maps.newHashMap();
    }

    @Nonnull
    public T getPlayerValue(UUID player) {
        return playerValues.getOrDefault(player, defaultValue);
    }

    @Nonnull
    public T getPlayerValue(Player player) {
        return getPlayerValue(player.getUniqueId());
    }

    public void setPlayerValue(UUID player, T value) {
        playerValues.put(player, value);
    }

    @Nonnull
    public T setPlayerValue(UUID player, String string) {
        final T valueToSet = type.fromString(string);
        setPlayerValue(player, valueToSet);

        return valueToSet;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public BrushDataType<T> getType() {
        return type;
    }

    @Nonnull
    public T getDefaultValue() {
        return defaultValue;
    }
}

package me.hapyl.mmu3.data;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Stores both runtime and persistent player data.
 */
public final class PlayerData {
    
    private static final Map<Player, PlayerData> PLAYER_DATA_MAP = Maps.newHashMap();
    
    // Valued data has direct access via public modifier
    public final Valued<Byte, Boolean> entityRemover;
    public final Valued<Byte, Boolean> commandBlockPreview;
    
    private final Player player;
    private final Map<Class<? extends Data>, Data> dataMap;
    
    public PlayerData(@NotNull Player player) {
        this.player = player;
        this.dataMap = Maps.newHashMap();
        this.entityRemover = valued(player, "entity_remover", PersistentDataType.BOOLEAN, false);
        this.commandBlockPreview = valued(player, "command_block_preview", PersistentDataType.BOOLEAN, false);
    }
    
    @SuppressWarnings("unchecked") // Checked
    @NotNull
    public <D extends Data> D requestData(@NotNull Class<D> dataClass) {
        return (D) dataMap.computeIfAbsent(dataClass, _class -> createData(player, _class));
    }
    
    @NotNull
    private <D extends Data> D createData(@NotNull Player player, @NotNull Class<D> dataClass) {
        try {
            final Constructor<D> constructor = dataClass.getConstructor(Player.class);
            constructor.setAccessible(true);
            
            if (constructor.getAnnotation(DataConstructor.class) == null) {
                throw new IllegalArgumentException("Missing `%s` annotation on the constructor!".formatted(DataConstructor.class.getSimpleName()));
            }
            else if (!Modifier.isPrivate(constructor.getModifiers())) {
                throw new IllegalArgumentException("Data constructor must be private!");
            }
            
            return constructor.newInstance(player);
        }
        catch (Exception ex) {
            MMULogger.error(
                    player,
                    Component.empty()
                             .append(Component.text("Failed to create "))
                             .append(Component.text(dataClass.getSimpleName(), NamedTextColor.YELLOW))
                             .append(Component.text("!"))
                             .appendNewline()
                             .append(Component.text("Please try again and relogging before reporting this!"))
                             .appendNewline()
                             .append(Component.text("Error message: " + ex.getMessage()))
            );
            
            throw new IllegalArgumentException("Failed to create player data for %s!".formatted(player.getName()), ex);
        }
    }
    
    @NotNull
    public static PlayerData ofPlayer(@NotNull Player player) {
        return PLAYER_DATA_MAP.computeIfAbsent(player, PlayerData::new);
    }
    
    @NotNull
    private static <P, C> Valued<P, C> valued(@NotNull Player player, @NotNull String key, @NotNull PersistentDataType<P, C> type, @NotNull C defaultValue) {
        return new Valued<>(player, Main.createNamespacedKey(key), type, defaultValue);
    }
    
    public static class Valued<P, C> {
        
        private final Player player;
        private final NamespacedKey key;
        private final PersistentDataType<P, C> type;
        private final C defaultValue;
        
        Valued(@NotNull Player player, @NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C defaultValue) {
            this.player = player;
            this.key = key;
            this.type = type;
            this.defaultValue = defaultValue;
        }
        
        public C value() {
            final C value = player.getPersistentDataContainer().get(key, type);
            
            return value != null ? value : defaultValue;
        }
        
        public void value(@Nullable C value) {
            final PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
            
            // Null values removes the mapping
            if (value == null) {
                persistentDataContainer.remove(key);
            }
            else {
                persistentDataContainer.set(key, type, value);
            }
        }
    }
    
}
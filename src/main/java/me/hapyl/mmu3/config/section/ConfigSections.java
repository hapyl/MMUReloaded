package me.hapyl.mmu3.config.section;

import com.google.common.collect.Lists;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;

// Can't use enum because we need the generic class
public final class ConfigSections {
    
    // TODO @May 09, 2026 (xanyjl) -> Duplicate feature key and supplier identifier! Make it so Feature returns the supplier that belongs to it?
    
    public static final ConfigSectionSupplier<ConfigSection> BANNER_EDITOR;
    public static final ConfigSectionSupplier<ConfigSectionCandle> CANDLE;
    
    private static final List<ConfigSectionSupplier<?>> VALUES;
    
    static {
        VALUES = Lists.newArrayList();
        
        BANNER_EDITOR = common("banner_editor");
        CANDLE = create("candle", ConfigSectionCandle.class, ConfigSectionCandle::new);
    }
    
    @NotNull
    public static List<ConfigSectionSupplier<?>> values() {
        // Trusted, so not defensive copy
        return VALUES;
    }
    
    @NotNull
    private static ConfigSectionSupplier<ConfigSection> common(@NotNull String identifier) {
        return create(identifier, ConfigSection.class, ConfigSectionImpl::new);
    }
    
    @NotNull
    private static <C extends ConfigSection> ConfigSectionSupplier<C> create(
            @NotNull String identifier,
            @NotNull Class<C> sectionClass,
            @NotNull BiFunction<String, ConfigurationSection, C> supplier
    ) {
        final ConfigSectionSupplier<C> sectionSupplier = new ConfigSectionSupplier<>() {
            @NotNull
            @Override
            public String identifier() {
                return identifier;
            }
            
            @NotNull
            @Override
            public Class<C> sectionClass() {
                return sectionClass;
            }
            
            @NotNull
            @Override
            public C supply(@NotNull String identifier, @NotNull ConfigurationSection section) {
                return supplier.apply(identifier, section);
            }
        };
        
        VALUES.add(sectionSupplier);
        
        return sectionSupplier;
    }
    
}
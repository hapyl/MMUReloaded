package me.hapyl.mmu3.feature.banner;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.feature.FeatureKey;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.jetbrains.annotations.NotNull;

public class BannerEditor extends Feature {
    
    public static final Registry<PatternType> REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN);
    public static final int MAX_PATTERNS = 16;
    
    public BannerEditor(@NotNull Main plugin) {
        super(FeatureKey.create("banner_editor"), plugin);
    }
    
}

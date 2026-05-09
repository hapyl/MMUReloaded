package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import org.jetbrains.annotations.NotNull;

public interface FeatureBase {
    
    @NotNull
    FeatureKey getKey();
    
    default boolean isEnabled() {
        Main.config().section()
        return Main.config().featureEnabled(this);
    }
    
    default boolean isDisabled() {
        return !Main.config().featureEnabled(this);
    }
    
}

package me.hapyl.mmu3.feature;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public final class FeatureKey implements Comparable<FeatureKey> {
    
    private static final Pattern PATTERN = Pattern.compile("^[a-z0-9_]+$");
    
    private final String key;
    
    FeatureKey(@NotNull String key) {
        if (!PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException("Key `%s` does not match the pattern `%s`!".formatted(key, PATTERN.pattern()));
        }
        
        this.key = key;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.key);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final FeatureKey that = (FeatureKey) object;
        return Objects.equals(this.key, that.key);
    }
    
    @Override
    public String toString() {
        return key;
    }
    
    @Override
    public int compareTo(@NotNull FeatureKey that) {
        return this.key.compareTo(that.key);
    }
    
    @NotNull
    public static FeatureKey create(@NotNull String key) {
        return new FeatureKey(key);
    }
    
}

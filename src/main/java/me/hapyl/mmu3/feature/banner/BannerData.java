package me.hapyl.mmu3.feature.banner;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.data.Data;
import me.hapyl.mmu3.data.DataConstructor;
import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.EncodedString;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BannerData extends Data implements ItemCreator {
    
    private final List<Pattern> patterns;
    private BannerColor baseColor;
    
    @DataConstructor
    private BannerData(@NotNull Player player) {
        super(player);
        
        this.baseColor = BannerColor.WHITE;
        this.patterns = Lists.newArrayList();
    }
    
    @Nonnull
    public BannerColor getBaseColor() {
        return baseColor;
    }
    
    public void setBaseColor(@Nonnull BannerColor baseColor) {
        this.baseColor = baseColor;
    }
    
    @NotNull
    public List<Pattern> getPatterns() {
        return patterns;
    }
    
    public int patternSize() {
        return patterns.size();
    }
    
    @Nullable
    public Pattern getPattern(int index) {
        if (isIndexOutOfBounds(index)) {
            return null;
        }
        
        return patterns.get(index);
    }
    
    public void addPattern(@Nonnull Pattern pattern) {
        if (this.patterns.size() >= BannerEditor.MAX_PATTERNS) {
            throw new IllegalStateException("Illegal pattern add.");
        }
        
        this.patterns.add(pattern);
    }
    
    @Nonnull
    public List<Pattern> subList(int from, int to) {
        return patterns.subList(Math.max(0, from), Math.min(patterns.size(), to));
    }
    
    public void move(int from, int to) {
        if (isIndexOutOfBounds(from) || isIndexOutOfBounds(to)) {
            return;
        }
        
        final Pattern toPattern = patterns.get(to);
        
        patterns.set(to, patterns.get(from));
        patterns.set(from, toPattern);
    }
    
    public void removePattern(int index) {
        if (isIndexOutOfBounds(index)) {
            return;
        }
        
        patterns.remove(index);
    }
    
    public void setPattern(int index, @NotNull Pattern pattern) {
        if (isIndexOutOfBounds(index)) {
            return;
        }
        
        patterns.set(index, pattern);
    }
    
    @NotNull
    @Override
    public BannerItemBuilder createBuilder() {
        return new BannerItemBuilder(this);
    }
    
    @NotNull
    public ItemBuilder createPreviewBuilder() {
        return createBuilder()
                .setPattern(patterns)
                .setName(Component.text("Banner Preview"));
    }
    
    @NotNull
    public ItemBuilder createLayerBuilder(int from, int to, boolean includePatternDescription) {
        final BannerItemBuilder builder = createBuilder();
        final List<Pattern> subList = subList(from, to);
        
        builder.setAmount(to);
        
        if (!subList.isEmpty()) {
            builder.setPattern(subList);
            
            if (includePatternDescription) {
                builder.addLore(Component.text("Patterns:"));
                
                subList.forEach(pattern -> {
                    final NamespacedKey patternKey = BannerEditor.REGISTRY.getKeyOrThrow(pattern.getPattern());
                    
                    builder.addLore(
                            Component.empty()
                                     .append(Component.text(" › ", NamedTextColor.DARK_GRAY))
                                     .append(ComponentHelper.capitalize(patternKey.getKey()).color(NamedTextColor.GRAY))
                    );
                });
            }
        }
        
        return builder;
    }
    
    @Nonnull
    public SerializedItemStack createFinalItem() {
        final EncodedString encodedString = BannerSerializer.serialize(this);
        final ItemStack itemStack = createBuilder().setPattern(patterns)
                                                   .addLore(encodedString.asComponent().color(NamedTextColor.DARK_GRAY))
                                                   .build();
        
        return SerializedItemStack.create(itemStack, encodedString);
    }
    
    @Override
    public void reset() {
        patterns.clear();
        baseColor = BannerColor.WHITE;
    }
    
    private boolean isIndexOutOfBounds(int index) {
        return index < 0 || index >= patterns.size();
    }
    
}
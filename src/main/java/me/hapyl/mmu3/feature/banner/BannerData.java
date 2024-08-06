package me.hapyl.mmu3.feature.banner;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.utils.Sized;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.joml.Math;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BannerData implements Sized {

    protected final List<Pattern> patterns;
    protected BaseBannerColor baseColor;

    public BannerData() {
        this.baseColor = BaseBannerColor.WHITE;
        this.patterns = Lists.newArrayList();
    }

    public ItemBuilder currentItem() {
        return builder()
                .setPattern(patterns)
                .setName("Banner Preview");
    }

    public ItemBuilder createItem(int from, int to, boolean data) {
        final List<Pattern> subList = subList(from, to);
        final BannerItemBuilder builder = builder();

        builder.setAmount(to);

        if (!subList.isEmpty()) {
            builder.setPattern(subList);

            if (data) {
                builder.addLore("Patterns:");
                subList.forEach(pattern -> {
                    builder.addLore("› " + Chat.capitalize(pattern.getPattern()));
                });
            }
        }

        return builder;
    }

    @Nonnull
    public BaseBannerColor getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(@Nonnull BaseBannerColor baseColor) {
        this.baseColor = baseColor;
    }

    public BannerItemBuilder builder() {
        return new BannerItemBuilder(this);
    }

    @Override
    public int size() {
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
            throw new IllegalStateException("illegal pattern add");
        }

        this.patterns.add(pattern);
    }

    @Nonnull
    public List<Pattern> subList(int from, int to) {
        return patterns.subList(Math.max(0, from), Math.min(patterns.size(), to));
    }

    @Nonnull
    public ItemStack createFinalItem() {
        return createFinalBuilder().addLore("&8" + BannerSerializer.serialize(this)).toItemStack();
    }

    public void move(int from, int to) {
        if (isIndexOutOfBounds(from) || isIndexOutOfBounds(to)) {
            return;
        }

        final Pattern toPattern = patterns.get(to);

        patterns.set(to, patterns.get(from));
        patterns.set(from, toPattern);
    }

    protected ItemBuilder createFinalBuilder() {
        final ItemBuilder builder = new ItemBuilder(baseColor.material);

        for (Pattern pattern : patterns) {
            builder.addBannerPattern(pattern.getPattern(), pattern.getColor());
        }

        return builder;
    }

    public void removePattern(int index) {
        if (isIndexOutOfBounds(index)) {
            return;
        }

        patterns.remove(index);
    }

    public void setPattern(int index, Pattern pattern) {
        if (isIndexOutOfBounds(index)) {
            return;
        }

        patterns.set(index, pattern);
    }

    private boolean isIndexOutOfBounds(int index) {
        return index < 0 || index >= patterns.size();
    }
}

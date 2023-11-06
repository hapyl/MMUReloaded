package me.hapyl.mmu3.outcast.fishing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Represents a fish object.
 */
public class Fish {

    private final String name;
    private final Material material;
    private final FishProperties properties;
    private final FishPattern pattern;

    public Fish(@Nonnull String name, @Nonnull Material material) {
        this.name = name;
        this.material = material;
        this.properties = new FishProperties();
        this.pattern = new FishPattern();
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public FishProperties getProperties() {
        return properties;
    }

    public FishPattern getPattern() {
        return pattern;
    }

    public ItemStack getItem(FishData data) {
        final FishItemBuilder builder = new FishItemBuilder(material);
        final int fishSize = data.getRandomSize(this);
        final FishGrade fishGrade = properties.getGrade(fishSize);

        builder.setName(name + fishGrade.getSuffix());

        builder.setFishSize(fishSize);
        builder.setFishUuid(UUID.randomUUID());
        builder.setCatcher(data.getPlayer());

        builder.addLore("Length: &b%s cm.", fishSize);

        return builder.build();
    }
}

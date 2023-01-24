package me.hapyl.mmu3.outcast.fishing;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
        final ItemBuilder builder = new ItemBuilder(material);
        final int fishSize = data.getRandomSize(this);
        final FishGrade fishGrade = properties.getGrade(fishSize);

        builder.setName(name + fishGrade.getSuffix());

        builder.setPersistentData("FishSize", PersistentDataType.INTEGER, fishSize);
        builder.setPersistentData("Uuid", PersistentDataType.STRING, UUID.randomUUID().toString());
        builder.setPersistentData("Cather", PersistentDataType.STRING, data.getPlayer().getName());
        builder.setPersistentData("CatherUuid", PersistentDataType.STRING, data.getPlayer().getUniqueId().toString());

        builder.addLore("Length: &b%s cm.", fishSize);

        return builder.build();
    }
}

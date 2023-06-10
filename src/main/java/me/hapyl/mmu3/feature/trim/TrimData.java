package me.hapyl.mmu3.feature.trim;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TrimData {

    private final Player player;
    private final EnumTrimArmor[] armor;
    private final Color[] armorColor;
    private EnumTrimPattern pattern;
    private EnumTrimMaterial material;

    public TrimData(Player player) {
        this.player = player;

        this.armor = new EnumTrimArmor[4];
        this.armorColor = new Color[4];

        this.pattern = EnumTrimPattern.SENTRY;
        this.material = EnumTrimMaterial.QUARTZ;

        // default armor to iron
        setArmor(EnumTrimArmor.IRON);
    }

    @Nullable
    public Color getArmorColor(@Nonnull EquipmentSlot slot) {
        return armorColor[slotToIndex(slot)];
    }

    public void setArmorColor(@Nonnull EquipmentSlot slot, @Nullable Color color) {
        armorColor[slotToIndex(slot)] = color;
    }

    public void setArmor(@Nonnull EnumTrimArmor armor) {
        setArmor(EquipmentSlot.HEAD, armor);
        setArmor(EquipmentSlot.CHEST, armor);
        setArmor(EquipmentSlot.LEGS, armor);
        setArmor(EquipmentSlot.FEET, armor);
    }

    public void setArmor(@Nonnull EquipmentSlot slot, @Nonnull EnumTrimArmor armor) {
        this.armor[slotToIndex(slot)] = armor;
    }

    public Player getPlayer() {
        return player;
    }

    public EnumTrimPattern getPattern() {
        return pattern;
    }

    public void setPattern(@Nonnull EnumTrimPattern pattern) {
        this.pattern = pattern;
    }

    public EnumTrimMaterial getMaterial() {
        return material;
    }

    public void setMaterial(@Nonnull EnumTrimMaterial material) {
        this.material = material;
    }

    @Nonnull
    public EnumTrimArmor getArmor(@Nonnull EquipmentSlot slot) {
        return armor[slotToIndex(slot)];
    }

    @Nonnull
    public EnumTrimArmor[] getArmor() {
        return armor;
    }

    private int slotToIndex(@Nonnull EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> throw new IllegalArgumentException("invalid slot: " + slot);
        };
    }
}

package me.hapyl.mmu3.feature.statechanger;

import org.bukkit.Material;

import javax.annotation.Nonnull;

public class ConditionedMaterial {

    private final boolean condition;
    private final Material[] materials;

    private ConditionedMaterial(boolean condition, Material[] materials) {
        this.condition = condition;
        this.materials = materials;
    }

    @Nonnull
    public Material getMaterial() {
        final Material material = materials[condition ? 0 : 1];
        return material.isItem() ? material : Material.BEDROCK;
    }

    public boolean isCondition() {
        return condition;
    }

    @Nonnull
    public static ConditionedMaterial of(boolean condition, Material ifTrue, Material ifFalse) {
        return new ConditionedMaterial(condition, new Material[] { ifTrue, ifFalse });
    }
}

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

        // If not an item, probably WALL block, so try to get the non-wall variant, or give up and use bedrock
        if (!material.isItem()) {
            final String name = material.name();

            if (name.contains("WALL_")) {
                final Material nonWallmMaterial = Material.getMaterial(name.replace("WALL_", ""));

                if (nonWallmMaterial != null) {
                    return nonWallmMaterial;
                }
            }

            // Try placement material
            final Material placementMaterial = material.createBlockData().getPlacementMaterial();

            if (!placementMaterial.isAir()) {
                return placementMaterial;
            }

            // Else just fucking give up
            return Material.BEDROCK;
        }

        return material;
    }

    public boolean isCondition() {
        return condition;
    }

    @Nonnull
    public static ConditionedMaterial of(boolean condition, Material ifTrue, Material ifFalse) {
        return new ConditionedMaterial(condition, new Material[] { ifTrue, ifFalse });
    }
}

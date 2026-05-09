package me.hapyl.mmu3.feature.trim;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EnumTrim {

    @Nonnull
    Component trimName();

    @Nullable
    static Trim trimFromItemStack(@NotNull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();

        if (meta instanceof ArmorMeta armorMeta) {
            final ArmorTrim armorTrim = armorMeta.getTrim();

            return armorTrim != null ? new Trim() {
                @Override
                public @NotNull EnumTrimPattern pattern() {
                    return EnumTrimPattern.fromBukkit(armorTrim.getPattern());
                }

                @Override
                public @NotNull EnumTrimMaterial material() {
                    return EnumTrimMaterial.fromBukkit(armorTrim.getMaterial());
                }
            } : null;
        }

        return null;
    }

    interface Trim {
        @NotNull
        EnumTrimPattern pattern();

        @NotNull
        EnumTrimMaterial material();
    }
}

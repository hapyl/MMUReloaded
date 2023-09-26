package me.hapyl.mmu3.feature.trim;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public enum TrimType {

    HELMET {
        @Override
        public void setItem(ArmorStand stand, ItemStack item) {
            stand.setHelmet(item);
        }

        @Nonnull
        @Override
        public ItemStack getItem(ArmorStand stand) {
            return stand.getHelmet();
        }
    },
    CHESTPLATE {
        @Override
        public void setItem(ArmorStand stand, ItemStack item) {
            stand.setChestplate(item);
        }

        @Nonnull
        @Override
        public ItemStack getItem(ArmorStand stand) {
            return stand.getChestplate();
        }
    },
    LEGGINGS {
        @Override
        public void setItem(ArmorStand stand, ItemStack item) {
            stand.setLeggings(item);
        }

        @Nonnull
        @Override
        public ItemStack getItem(ArmorStand stand) {
            return stand.getLeggings();
        }
    },
    BOOTS {
        @Override
        public void setItem(ArmorStand stand, ItemStack item) {
            stand.setBoots(item);
        }

        @Nonnull
        @Override
        public ItemStack getItem(ArmorStand stand) {
            return stand.getBoots();
        }
    };

    public void setItem(ArmorStand stand, ItemStack item) {
        throw new IllegalStateException();
    }

    @Nonnull
    public ItemStack getItem(ArmorStand stand) {
        throw new IllegalStateException();
    }
}

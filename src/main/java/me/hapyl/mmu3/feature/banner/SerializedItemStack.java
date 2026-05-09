package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.EncodedString;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface SerializedItemStack {
    @NotNull
    ItemStack itemStack();

    @NotNull
    EncodedString encodedString();

    @NotNull
    static SerializedItemStack create(@NotNull ItemStack itemStack, @NotNull EncodedString encodedString) {
        return new SerializedItemStackImpl(itemStack, encodedString);
    }

}

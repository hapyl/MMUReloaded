package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.EncodedString;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SerializedItemStackImpl implements SerializedItemStack {

    private final ItemStack itemStack;
    private final EncodedString encodedString;

    SerializedItemStackImpl(@NotNull ItemStack itemStack, @NotNull EncodedString encodedString) {
        this.itemStack = itemStack;
        this.encodedString = encodedString;
    }

    @Override
    public @NotNull ItemStack itemStack() {
        return itemStack;
    }

    @Override
    public @NotNull EncodedString encodedString() {
        return encodedString;
    }
}

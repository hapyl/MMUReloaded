package me.hapyl.mmu3.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class EncodedString implements ComponentLike {

    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private final String base64;

    private EncodedString(@NotNull String base64) {
        this.base64 = base64;
    }

    @NotNull
    public String decode() {
        try {
            return new String(DECODER.decode(base64));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid base64: %s".formatted(base64));
        }
    }

    @Override
    public String toString() {
        return base64;
    }

    @NotNull
    @Override
    public Component asComponent() {
        return Component.text(base64);
    }

    @NotNull
    public static EncodedString encode(byte[] base64) {
        return new EncodedString(ENCODER.encodeToString(base64));
    }

    @NotNull
    public static String decode(@NotNull String base64) throws IllegalArgumentException {
        return new EncodedString(base64).decode();
    }

}
package me.hapyl.mmu3.util;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.security.SecureRandom;

public class HexId implements Comparable<HexId> {

    private static final int HEX_LENGTH = 6;

    private final String hexValue;

    public HexId(@Nonnull String hexValue) {
        if (!isValidHex(hexValue)) {
            this.hexValue = "000000";
        }
        else if (hexValue.length() != HEX_LENGTH) {
            this.hexValue = hexValue.substring(0, Math.min(hexValue.length(), HEX_LENGTH));
        }
        else {
            this.hexValue = hexValue;
        }
    }

    @Override
    public int compareTo(HexId other) {
        return hexValue.compareTo(other.hexValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final HexId other = (HexId) o;
        return hexValue.equals(other.hexValue);
    }

    @Override
    public String toString() {
        return hexValue;
    }

    @Override
    public int hashCode() {
        return hexValue.hashCode();
    }

    private boolean isValidHex(String input) {
        try {
            new BigInteger(input, 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static HexId random() {
        final SecureRandom random = new SecureRandom();
        final StringBuilder randomHex = new StringBuilder(HEX_LENGTH);

        for (int i = 0; i < HEX_LENGTH; i++) {
            randomHex.append(Integer.toHexString(random.nextInt(16)));
        }

        return new HexId(randomHex.toString());
    }
}

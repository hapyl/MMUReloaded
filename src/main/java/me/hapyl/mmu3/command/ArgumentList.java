package me.hapyl.mmu3.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ArgumentList {

    public final int length;
    private final String[] args;

    ArgumentList(@NotNull String[] args) {
        this.args = args;
        this.length = args.length;
    }

    @NotNull
    public Argument get(int index) {
        if (index < 0 || index >= length) {
            return Argument.empty();
        }

        return Argument.of(args[index]);
    }

    public @NotNull String concat(int from) {
        final StringBuilder builder = new StringBuilder();

        for (int i = from; i < length; i++) {
            if (i != from) {
                builder.append(" ");
            }

            builder.append(args[i]);
        }

        return builder.toString();
    }

    public static class Argument {
        private final Object object;

        Argument(@NotNull Object object) {
            this.object = object;
        }

        public int toInt(int defaultValue) {
            return parseNumber(Integer.class, Integer::parseInt, defaultValue);
        }

        public int toInt() {
            return toInt(0);
        }

        public long toLong(long defaultValue) {
            return parseNumber(Long.class, Long::parseLong, defaultValue);
        }

        public long toLong() {
            return toLong(0L);
        }

        public double toDouble(double defaultValue) {
            return parseNumber(Double.class, Double::parseDouble, defaultValue);
        }

        public double toDouble() {
            return toDouble(0);
        }

        public float toFloat(float defaultValue) {
            return parseNumber(Float.class, Float::parseFloat, defaultValue);
        }

        public float toFloat() {
            return toFloat(0);
        }

        public byte toByte(byte defaultValue) {
            return parseNumber(Byte.class, Byte::parseByte, defaultValue);
        }

        public byte toByte() {
            return toByte((byte) 0);
        }

        public short toShort(short defaultValue) {
            return parseNumber(Short.class, Short::parseShort, defaultValue);
        }

        public short toShort() {
            return toShort((short) 0);
        }

        @NotNull
        public String toString() {
            return object.toString();
        }

        @NotNull
        public <E extends Enum<E>> Optional<E> toEnum(@NotNull Class<E> enumClass) {
            try {
                return Optional.of(Enum.valueOf(enumClass, object.toString().toUpperCase()));
            } catch (Exception ex) {
                return Optional.empty();
            }
        }

        @NotNull
        public Optional<Player> toPlayer() {
            return Optional.ofNullable(Bukkit.getPlayer(object.toString()));
        }

        @NotNull
        static Argument empty() {
            class Holder {
                private static final Argument EMPTY = new Argument(new Object());
            }

            return Holder.EMPTY;
        }

        @NotNull
        static Argument of(@NotNull Object object) {
            return new Argument(Objects.requireNonNull(object, "Object must not be null!"));
        }

        private <N extends Number> N parseNumber(@NotNull Class<N> numberClass, @NotNull Function<String, N> function, @NotNull N defaultValue) {
            if (numberClass.isInstance(object)) {
                return numberClass.cast(object);
            }
            else {
                try {
                    return function.apply(object.toString());
                } catch (Exception ex) {
                    return defaultValue;
                }
            }
        }
    }

}

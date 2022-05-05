package me.hapyl.mmu3.utils;

public class Enums {

    public static <T> T getNextValue(T[] values, T current) {
        for (int i = 0; i < values.length; i++) {
            final T t = values[i];
            if (t.equals(current)) {
                return values.length > (i + 1) ? values[i + 1] : values[0];
            }
        }
        return current;
    }

    public static <T> T getPreviousValue(T[] values, T current) {
        for (int i = 0; i < values.length; i++) {
            final T t = values[i];
            if (t.equals(current)) {
                return i == 0 ? values[values.length - 1] : values[i - 1];
            }
        }
        return current;
    }

}

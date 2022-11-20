package me.hapyl.mmu3.outcast.game;

import com.google.common.collect.Lists;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class Arguments {

    private static final Arguments EMPTY = new Arguments();

    private final List<String> arguments;

    public Arguments(@Nonnull String[] arguments, int copyFrom) {
        this.arguments = Lists.newArrayList();
        this.arguments.addAll(Arrays.asList(arguments).subList(copyFrom, arguments.length));
    }

    public Arguments(@Nonnull String[] arguments) {
        this(arguments, 0);
    }

    private Arguments() {
        this.arguments = Lists.newArrayList();
    }

    public boolean isEmpty() {
        return arguments.isEmpty();
    }

    public int getInt(int index, int def) {
        return index < arguments.size() ? NumberConversions.toInt(arguments.get(index)) : def;
    }

    public long getLong(int index, long def) {
        return index < arguments.size() ? NumberConversions.toLong(arguments.get(index)) : def;
    }

    public String getString(int index, String def) {
        return index < arguments.size() ? arguments.get(index) : def;
    }

    public static Arguments empty() {
        return EMPTY;
    }

    public boolean isDebug() {
        for (String argument : arguments) {
            if (argument.equalsIgnoreCase("debug")) {
                return true;
            }
        }
        return false;
    }
}

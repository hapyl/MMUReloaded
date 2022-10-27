package me.hapyl.mmu3.utils.nbt;

import com.google.common.collect.Lists;
import net.minecraft.nbt.*;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class NBTTagVisitor implements TagVisitor {

    private static final Pattern PATTERN = Pattern.compile("[A-Za-z\\d._+-]+");
    private final StringBuilder builder;

    public NBTTagVisitor() {
        builder = new StringBuilder();
    }

    public String toString(NBTBase base) {
        base.a(this);
        return builder.toString();
    }

    @Override
    public void a(NBTTagLong nbtTagLong) {
        append("&6%s&cL", nbtTagLong.k());
    }

    @Override
    public void a(NBTTagFloat nbtTagFloat) {
        append("&6%s&cf", nbtTagFloat.k());
    }

    @Override
    public void a(NBTTagDouble nbtTagDouble) {
        append("&6%s&cd", nbtTagDouble.k());
    }

    @Override
    public void a(NBTTagByteArray nbtTagByteArray) {
        append("&f[");

        final byte[] bytes = nbtTagByteArray.d();
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                append(", ");
            }

            append("&6%s&cb", bytes[i]);
        }

        append("]");
    }

    @Override
    public void a(NBTTagIntArray nbtTagIntArray) {
        append("&f[");

        final int[] bytes = nbtTagIntArray.f();
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                append(", ");
            }

            append("&6%s", bytes[i]);
        }

        append("]");
    }

    @Override
    public void a(NBTTagLongArray nbtTagLongArray) {
        append("&f[");

        final long[] bytes = nbtTagLongArray.f();
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                append(", ");
            }

            append("&6%s&cL", bytes[i]);
        }

        append("]");
    }

    @Override
    public void a(NBTTagList nbtTagList) {
        append("&f[");

        for (int i = 0; i < nbtTagList.size(); i++) {
            if (i != 0) {
                append(", ");
            }

            append(new NBTTagVisitor().toString(nbtTagList.get(i)));
        }

        append("]");
    }

    @Override
    public void a(NBTTagString nbtTagString) {
        append("&a%s", nbtTagString);
    }

    @Override
    public void a(NBTTagByte nbtTagByte) {
        append("&6%s&cb", nbtTagByte.k());
    }

    @Override
    public void a(NBTTagShort nbtTagShort) {
        append("&6%s&cb", nbtTagShort.k());
    }

    @Override
    public void a(NBTTagInt nbtTagInt) {
        append("&6%s", nbtTagInt.k());
    }

    @Override
    public void a(NBTTagCompound nbtTagCompound) {
        append("{");

        List<String> keys = Lists.newArrayList(nbtTagCompound.d());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            final String next = keys.get(i);
            final NBTBase base = nbtTagCompound.c(next);
            if (base == null) {
                continue;
            }

            append(checkPattern(next));
            append(":");
            append(new NBTTagVisitor().toString(base));

            if (i < (keys.size() - 1)) {
                append(", ");
            }
        }

        append("}");
    }

    @Override
    public void a(NBTTagEnd nbtTagEnd) {
        builder.append("END");
    }

    protected static String checkPattern(String var0) {
        return PATTERN.matcher(var0).matches() ? var0 : NBTTagString.b(var0);
    }

    private void append(String str, Object... format) {
        builder.append(str.formatted(format)).append("&f");
    }

}

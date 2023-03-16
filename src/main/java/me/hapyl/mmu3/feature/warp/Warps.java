package me.hapyl.mmu3.feature.warp;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Warps extends Feature {

    protected final Map<String, Warp> byName;
    private final WarpConfig config;

    public Warps(Main mmu3plugin) {
        super(mmu3plugin);
        this.byName = Maps.newHashMap();
        this.config = new WarpConfig(mmu3plugin, this);
    }

    public WarpConfig getConfig() {
        return config;
    }

    public boolean register(Warp newWarp) {
        final String name = newWarp.getName();
        final Warp warp = byName(name);

        if (warp != null) {
            return false;
        }

        byName.put(name.toLowerCase(Locale.ROOT), newWarp);
        return true;
    }

    public void unregister(Warp warp) {
        byName.remove(warp.getName().toLowerCase(Locale.ROOT));
    }

    @Nullable
    public Warp byName(String name) {
        return byName.get(name.toLowerCase(Locale.ROOT));
    }

    public Set<String> names() {
        return byName.keySet();
    }

}

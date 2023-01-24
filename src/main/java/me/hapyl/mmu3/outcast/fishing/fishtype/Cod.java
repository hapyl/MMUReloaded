package me.hapyl.mmu3.outcast.fishing.fishtype;

import me.hapyl.mmu3.outcast.fishing.Fish;
import me.hapyl.mmu3.outcast.fishing.FishProperties;
import org.bukkit.Material;

public class Cod extends Fish {
    public Cod() {
        super("Cod", Material.COD);
        final FishProperties properties = getProperties();

        properties.setMinSize(10);
        properties.setMaxSize(45);
    }
}

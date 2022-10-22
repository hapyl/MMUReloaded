package me.hapyl.mmu3.outcast.hypixel.slayer.boss;

public class SlayerBossAbility {

    private final String name;
    private final String info;

    public SlayerBossAbility(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public void tick() {

    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}

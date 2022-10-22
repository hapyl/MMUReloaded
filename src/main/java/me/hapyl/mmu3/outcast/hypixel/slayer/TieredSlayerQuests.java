package me.hapyl.mmu3.outcast.hypixel.slayer;

import me.hapyl.spigotutils.module.math.Numbers;

import javax.annotation.Nullable;

public class TieredSlayerQuests {

    private final SlayerType type;
    private final SlayerQuest[] quests;

    public TieredSlayerQuests(SlayerType type) {
        this.type = type;
        this.quests = new SlayerQuest[5];
    }

    public SlayerType getType() {
        return type;
    }

    @Nullable
    public SlayerQuest getTier(int tier) {
        if (tier >= quests.length) {
            return null;
        }
        return quests[tier];
    }

    public void setTier(int tier, SlayerQuest quest) {
        tier = Numbers.clamp(tier, 1, 5);

        quest.setTier(tier);
        quests[tier] = quest;
    }

    public boolean hasTier(int i) {
        return (i > 0 && i <= 5) && quests[i] != null;
    }
}

package me.hapyl.mmu3.game;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

public class CookieClicker extends Game {

    private final Map<Integer, String[]> milestoneString;

    public CookieClicker() {
        super("Cookie Clicker");
        milestoneString = Maps.newHashMap();
        milestoneString.put(0, new String[] { "Noob", "Do you even know how to click?" });
    }

    private String[] getMilestone(int clicks) {
        return milestoneString.getOrDefault(clicks / 1000, new String[] { "Invalid Rank", "There is no rank for this milistone!" });
    }

    @Override
    @Nonnull
    protected GameInstance newInstance(Player player, @Nonnull Arguments arguments) {
        return new GameInstance(player, this) {

            private final PersistentPlayerData data = PersistentPlayerData.getData(player);
            private int clicks = data.getCookieClicks();

            private void updateInventory() {
                final String[] milestone = getMilestone(clicks);
                setItem(
                        13,
                        new ItemBuilder(Material.COOKIE)
                                .setName("&aCookie")
                                .addLore()
                                .addLore("Clicks &b" + clicks)
                                .addLore()
                                .addLore("Milestone &b" + milestone[0])
                                .addSmartLore(milestone[1])
                                .setAmount(Numbers.clamp(clicks, 1, 64))
                                .build()
                );
            }

            @Override
            public void onClick(int slot) {
                if (slot != 13) {
                    return;
                }

                clicks++;
                PlayerLib.playSound(getPlayer(), Sound.ENTITY_CHICKEN_EGG, (float) (0.002 * (clicks % 1000)));
                updateInventory();
            }

            @Override
            public void onGameStart() {
                gui.setPanelCloseMenu();
                updateInventory();
            }

            @Override
            public void onGameStop() {
                data.setCookieClicks(clicks);
                stopPlaying();
            }
        };
    }
}

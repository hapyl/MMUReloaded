package me.hapyl.mmu3.outcast.hypixel.slayer.gui;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.outcast.hypixel.slayer.*;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SlayerGUI extends PanelGUI {

    private final Slayer slayer;

    public SlayerGUI(Player player) {
        super(player, "Slayer", Size.THREE);
        slayer = Main.getRegistry().slayer;
        updateInventory();
    }

    @Override
    public void updateInventory() {
        setPanelCloseMenu();
        final SlayerActiveQuest activeQuest = slayer.getActiveQuest(getPlayer());

        // No quest (Main menu)
        if (activeQuest == null) {
            final SmartComponent smart = newSmartComponent();
            final SlayerType[] values = SlayerType.values();

            for (SlayerType type : values) {
                final SlayerData data = type.getData();
                smart.add(
                        new ItemBuilder(data.getIcon())
                                .setName("&c☠ &a" + data.getName())
                                .setSmartLore(data.getLore())
                                .addLore()
                                .addLore("&eClick to view")
                                .build(),
                        player -> new SlayerTierGUI(player, type)
                );
            }

            smart.fillItems(this, SlotPattern.INNER_LEFT_TO_RIGHT);
        }
        else {
            final SlayerQuest quest = activeQuest.getQuest();

            switch (activeQuest.getState()) {
                case ACTIVE, BOSS -> {
                    final ItemBuilder builder = new ItemBuilder(Material.YELLOW_TERRACOTTA)
                            .setName("&eQuest in Progress!")
                            .addLore("&8%s %s", quest.getType().getData().getName(), quest.getTierRomanNumeral())
                            .addLore()
                            .addLore("&7Earn %s Exp worth of:", quest.getExpToSpawn());

                    for (EntityType allowedType : quest.getType().getAllowedTypes()) {
                        builder.addLore("&b- &c" + Chat.capitalize(allowedType.getKey().getKey()));
                    }

                    builder.addLore()
                            .addLore("&7Progress: &b%s/%s", activeQuest.getCurrentExp(), quest.getExpToSpawn())
                            .addLore()
                            .addLore("&eClick to cancel");

                    setItem(
                            13,
                            builder.build(), player -> {
                                slayer.clearActiveQuest(player);
                                updateInventory();
                            }
                    );
                }

                case SUCCESS -> {
                    setItem(
                            13,
                            new ItemBuilder(Material.LIME_TERRACOTTA)
                                    .setName("Quest Complete!")
                                    .addLore("&8%s %s", quest.getType().getData().getName(), quest.getTierRomanNumeral())
                                    .addLore()
                                    .addLore("&7Time to spawn: &b???")
                                    .addLore("&7Time to kill: &b???")
                                    .addLore()
                                    .addLore("&eClick to claim rewards")
                                    .build(),
                            (player) -> {
                                activeQuest.grantRewards();
                                slayer.clearActiveQuest(player);
                                updateInventory();
                            }
                    );
                }

                case FAILED -> {
                    setItem(
                            13,
                            new ItemBuilder(Material.REDSTONE_BLOCK)
                                    .setName("&cQuest Failed!")
                                    .addSmartLore("Better luck next time...")
                                    .addLore("")
                                    .addLore("&eClick to clear")
                                    .build(),
                            (player) -> {
                                slayer.clearActiveQuest(player);
                                updateInventory();
                                PlayerLib.playSound(player, Sound.ENTITY_WOLF_WHINE, 1.0f);
                            }
                    );
                }
            }
        }

        openInventory();
    }
}

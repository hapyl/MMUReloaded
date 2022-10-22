package me.hapyl.mmu3.outcast.hypixel.slayer.gui;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.outcast.hypixel.slayer.*;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossAbility;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SlayerTierGUI extends PanelGUI {

    private final SlayerType type;
    private final Slayer slayer;

    public SlayerTierGUI(Player player, SlayerType type) {
        super(player, type.getData().getName(), Size.FIVE);
        this.type = type;
        this.slayer = Main.getRegistry().slayer;
        updateInventory();
    }

    @Override
    public void updateInventory() {
        setPanelGoBack("Slayer", SlayerGUI::new);
        setPanelCloseMenu();

        final TieredSlayerQuests quests = type.getQuests();
        final SlayerData data = type.getData();

        int slot = 11;
        for (int i = 0; i < 5; i++, slot++) {
            final SlayerQuest quest = quests.getTier(i + 1);

            if (quest == null) {
                setItem(slot, new ItemBuilder(Material.COAL_BLOCK).setName("&cNot released yet!").build());
                continue;
            }

            final SlayerStartCost cost = quest.getCost();
            final SlayerBoss boss = quest.getBoss();

            final ItemBuilder builder = new ItemBuilder(data.getIcon())
                    .setName(quest.getName())
                    .setAmount(i + 1)
                    .addLore("&8" + quest.getDifficulty())
                    .addLore();

            // Basic Info
            builder.addLore("&7Health: &c%s❤", boss.getHealth());
            builder.addLore("&7Damage: &c%s &7per second", boss.getDPS());
            builder.addLore();

            // Abilities
            for (SlayerBossAbility ability : boss.getAbilities()) {
                builder.addLore(ability.getName());
                builder.addSmartLore(ability.getInfo());
                builder.addLore();
            }

            // Rewards
            builder.addLore("&7Reward: &d???");
            builder.addLore("&8  + Boss drops");
            builder.addLore();

            // Cost
            builder.addLore("&7Starting cost: &6%s", quest.getCostString());
            builder.addLore();

            if (cost.canAfford(getPlayer())) {
                builder.addLore("&eClick to slay!");
                setClick(slot, (player) -> {
                    if (cost.removeItemsIfCanAfford(player)) {
                        slayer.startQuest(player, quest);
                        player.closeInventory();
                    }
                    else {
                        Slayer.sendMessage(player, "Cannot afford quest.");
                    }
                });
            }
            else {
                builder.addLore("&cCannot afford!");
            }

            setItem(slot, builder.build());
        }

        // Boss Drops
        setItem(
                29,
                new ItemBuilder(Material.GOLD_NUGGET)
                        .setName("&eBoss Drops")
                        .setSmartLore("Bosses will always drop common items, but sometimes, they will drop &drare &7items!")
                        .addLore()
                        .addLore("&eClick to view drops")
                        .build(), (player) -> new SlayerDropListGUI(player, type)
        );

        // Boss Guide
        final ItemBuilder expGainItem = new ItemBuilder(Material.WITCH_SPAWN_EGG)
                .setName("Exp Gain")
                .setSmartLore("Gain &aExp &7from these mobs in order to spawn the boss:")
                .addLore();

        for (EntityType allowedType : type.getAllowedTypes()) {
            expGainItem.addLore("&b- &c" + Chat.capitalize(allowedType.getKey().getKey()));
        }

        setItem(31, expGainItem.addLore().addSmartLore("Any other will not yield exp for this boss.").build());

        // Boss Levelling
        setItem(
                33,
                new ItemBuilder(Material.BOOK)
                        .setName("Levelling")
                        .setSmartLore("Slay bosses to level up!")
                        .addLore()
                        .addLore("&eClick to view progress")
                        .build()
        );

        openInventory();
    }
}

package me.hapyl.mmu3.feature.statechanger;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.chat.Gradient;
import kz.hapyl.spigotutils.module.chat.gradient.Interpolators;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.inventory.gui.GUI;
import kz.hapyl.spigotutils.module.util.ThreadRandom;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.awt.*;

public class StateChanger extends Feature {

    private final ItemStack baseItem = new ItemBuilder(Material.SHEARS, "state_changer")
            .setName("&aState Changer")
            .addSmartLore(
                    "What looks like a normal pair or shears is actually an ancient item, used to modify shapes and forms of blocks. Unknown origin of this item makes it even more mysterious.",
                    "&8&o"
            )
            .glow()
            .build();

    public StateChanger(Main main) {
        super(main);
    }

    public void openEditor(Player player, Block block) {
        final String blockName = Chat.capitalize(block.getType());

        if (isBannedBlock(block.getType())) {
            Message.error(player, "%s is not allowed!", blockName);
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (!isAllowedBlock(block)) {
            Message.error(player, "%s doesn't have any block data!", blockName);
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        final StringBuilder builder = new StringBuilder("Editing ");
        if (blockName.length() >= 22) {
            builder.append(blockName, 0, 22);
            builder.append("...");
        }
        else {
            builder.append(blockName);
        }

        new StateChangerGUI(player, builder.toString(), new Data(player, block));
    }

    public void giveItem(Player player) {
        final PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            Message.error(player, "Not enough space in inventory!");
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        inventory.addItem(new ItemBuilder(baseItem)
                                  .addLore()
                                  .addLore("Created by almighty %s&7.", randomColorName(player))
                                  .toItemStack());

        Message.success(player, "Success!");
    }

    private static String randomColorName(Player player) {
        final Gradient gradient = new Gradient(player.getName()).makeBold();
        return gradient.rgb(
                new Color(ThreadRandom.nextInt(255), ThreadRandom.nextInt(255), ThreadRandom.nextInt(255)),
                new Color(ThreadRandom.nextInt(255), ThreadRandom.nextInt(255), ThreadRandom.nextInt(255)),
                Interpolators.LINEAR
        );
    }

    public boolean isAllowedBlock(Block block) {
        final BlockData data = block.getBlockData();
        return data instanceof Waterlogged || data instanceof Fence || data instanceof Slab || data instanceof Bisected ||
                data instanceof Directional || data instanceof MultipleFacing || data instanceof Stairs || data instanceof Ageable ||
                data instanceof Bamboo || data instanceof Bed || data instanceof Rail || data instanceof AnaloguePowerable;
    }

    public boolean isBannedBlock(Material material) {
        return switch (material) {
            case KELP_PLANT, KELP -> true;
            default -> false;
        };
    }

}

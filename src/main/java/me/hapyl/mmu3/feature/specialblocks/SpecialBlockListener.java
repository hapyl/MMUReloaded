package me.hapyl.mmu3.feature.specialblocks;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.utils.InjectListener;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class SpecialBlockListener extends InjectListener {

    public SpecialBlockListener(Main main) {
        super(main);
    }

    @EventHandler()
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (!ItemBuilder.itemHasID(itemInMainHand)) {
            return;
        }

        final SpecialBlock specialBlock = specialBlocks().byId(ItemBuilder.getItemID(itemInMainHand));
        if (specialBlock == null) {
            return;
        }

        ev.setCancelled(true);
        ev.setBuild(false);
        specialBlock.acceptEvent(ev);
    }

}

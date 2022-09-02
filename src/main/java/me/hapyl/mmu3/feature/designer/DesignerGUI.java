package me.hapyl.mmu3.feature.designer;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.inventory.gui.CancelType;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DesignerGUI {

    private final UUID owner;
    private final String ownerName;
    private final int size;
    private ItemStack[] items;

    public DesignerGUI(Player player, int size) {
        this.owner = player.getUniqueId();
        this.ownerName = player.getName();
        this.size = Numbers.clamp(size, 1, 6);
        this.items = new ItemStack[size * 9];
    }

    public void open(Player player) {
        final boolean isOwner = player.getUniqueId() == owner;
        final PlayerGUI gui = new PlayerGUI(player, ownerName + "'s Designer" + (isOwner ? "" : " &7[P]"), this.size);

        // load items
        gui.getInventory().setContents(items);

        if (isOwner) {
            gui.setAllowDrag(true);
            gui.setAllowShiftClick(true);
            gui.setCancelType(CancelType.NEITHER);
            gui.setCloseEvent((pl) -> items = gui.getInventory().getContents());
        }
        else {
            gui.setCancelType(CancelType.EITHER);
            gui.setEventListener((pl, inv, ev) -> {
                Message.error(pl, "You cannot edit other's designers!");
                Message.sound(pl, Sound.ENTITY_VILLAGER_NO);
            });
        }

        Message.sound(player, Sound.BLOCK_CHEST_OPEN, 1.25f);
        gui.openInventory();
    }

}

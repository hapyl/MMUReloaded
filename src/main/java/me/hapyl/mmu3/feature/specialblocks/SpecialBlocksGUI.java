package me.hapyl.mmu3.feature.specialblocks;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.util.menu.Menu;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.util.menu.Size;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SpecialBlocksGUI extends Menu {
    public SpecialBlocksGUI(Player player) {
        super(player, "Special Blocks", Size.SIZE_5);

        openInventory();
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        final SpecialBlocks specialBlocks = Main.getSpecialBlocks();

        // Nature
        for (Type type : Type.values()) {
            createSubMenuItemAndSet(
                    type.getSlot(),
                    type.getMaterial(),
                    type.getName(),
                    type.getDescription(),
                    specialBlocks.getByType(type),
                    type.getSize()
            );
        }
    }

    private void createSubMenuItemAndSet(int slot, Material material, String name, String lore, Set<SpecialBlock> blocks, Size size) {
        setItem(slot, createSubMenuItem(material, name, lore), (menu, player, clickType, clickedSlot) -> new SBSubGUI(player, name, size, blocks));
    }

    private ItemStack createSubMenuItem(Material material, String name, String lore) {
        return new ItemBuilder(material).setName("&a" + name).setSmartLore(lore).addLore().addLore("&8◦ &eLeft-Click to open").build();
    }
}

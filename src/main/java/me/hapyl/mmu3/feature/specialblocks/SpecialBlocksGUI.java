package me.hapyl.mmu3.feature.specialblocks;

import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SpecialBlocksGUI extends PanelGUI {
    public SpecialBlocksGUI(Player player) {
        super(player, "Special Blocks", Size.FIVE);
        setPanelCloseMenu();
        updateInventory();
    }

    public void updateInventory() {
        final SpecialBlocks specialBlocks = Main.getSpecialBlocks();
        for (SpecialBlock block : specialBlocks.getByType(Type.COMMON)) {
            final int slot = block.getSlot();
            setItem(slot, block.getIcon());
            setClick(slot, block::giveItem);
        }

        // Doors
        createSubMenuItemAndSet(
                40,
                Material.OAK_DOOR,
                "Doors",
                "Opens menu with different doors and their variants.",
                specialBlocks.getByType(Type.DOOR)
        );

        // Snow Layers
        createSubMenuItemAndSet(
                42,
                Material.SNOW,
                "Layered Snow",
                "Opens menu with layered snow blocks.",
                specialBlocks.getByType(Type.SNOW)
        );


        // Fluids
        createSubMenuItemAndSet(
                37,
                Material.WATER_BUCKET,
                "Levelled Water",
                "Opens menu with levelled water blocks.",
                specialBlocks.getByType(Type.FLUID_WATER), Size.FIVE
        );
        createSubMenuItemAndSet(
                38,
                Material.LAVA_BUCKET,
                "Levelled Lava",
                "Opens menu with levelled lava blocks.",
                specialBlocks.getByType(Type.FLUID_LAVA), Size.FIVE
        );


        openInventory();
    }

    private void createSubMenuItemAndSet(int slot, Material material, String name, String lore, Set<SpecialBlock> blocks, Size size) {
        setItem(slot, createSubMenuItem(material, name, lore), player -> new SBSubGUI(player, name, size, blocks));
    }

    private void createSubMenuItemAndSet(int slot, Material material, String name, String lore, Set<SpecialBlock> blocks) {
        createSubMenuItemAndSet(slot, material, name, lore, blocks, Size.FOUR);
    }

    private ItemStack createSubMenuItem(Material material, String name, String lore) {
        return new ItemBuilder(material).setName("&a" + name).setSmartLore(lore).addLore().addLore("&eClick to open").build();
    }
}

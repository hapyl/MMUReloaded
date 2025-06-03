package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.util.PanelGUI;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class TrimGUI extends PanelGUI {

    private final TrimEditor editor;

    public TrimGUI(Player player, TrimEditor editor) {
        super(player, "Trim GUI", Size.FOUR);

        this.editor = editor;

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final ItemStack currentItem = editor.getCurrentItem();
        final TrimData data = editor.getData();

        setPanelCloseMenu();
        setItem(
                22,
                new ItemBuilder(currentItem).setName("&aCurrent Item").setSmartLore("This is the current item you're editing!").asIcon()
        );

        // Randomize
        setItem(
                20,
                new ItemBuilder(Material.COMMAND_BLOCK).setName("&aRandomize")
                        .addTextBlockLore("""
                                Randomize this armor piece's pattern and material of the trim!
                                
                                &eClick to randomize!
                                """)
                        .asIcon(),
                player -> {
                    editor.randomize();
                    player.closeInventory();
                }
        );

        final Color color = data.getColor();

        // Color
        setItem(
                24, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setName("Set Armor Color")
                        .addTextBlockLore("""
                                Change the color of this armor piece!
                                
                                &8;;Changing the color of the armor will convert the item to leather if it isn't already.
                                
                                &eClick to set color.
                                """)
                        .setLeatherArmorColor(color)
                        .asIcon()
        );

        setAction(
                24, player -> editor.tryColor(), ClickType.LEFT
        );

        // Finalize
        setPanelItem(
                6, new ItemBuilder(Material.DIAMOND)
                        .setName("&aFinalize!")
                        .addTextBlockLore("""
                                Finalize this armor and get the items!
                                
                                &eClick to finalize!
                                """)
                        .asIcon(), player -> {
                    editor.remove();
                    player.closeInventory();
                }
        );

        // Remove trim
        setPanelItem(
                2, new ItemBuilder(Material.RED_DYE)
                        .setName("&cRemove Trim")
                        .addTextBlockLore("""
                                Don't want the trim? Need a clean armor piece?
                                Yep, this option is for you!
                                
                                &cClick to remove trim!
                                """).asIcon(), player -> {
                    data.removeTrim();
                    data.update();

                    player.closeInventory();

                    editor.sendInfo("Removed trim!");
                    editor.sendSound(Sound.BLOCK_ANVIL_DESTROY, 0.0f);
                }
        );
    }

}

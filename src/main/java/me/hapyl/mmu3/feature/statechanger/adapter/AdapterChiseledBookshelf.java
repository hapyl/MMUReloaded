package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.data.type.ChiseledBookshelf;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

public class AdapterChiseledBookshelf extends Adapter<ChiseledBookshelf> {

    private final Map<Integer, Integer> slotsMap = Map.of(
            0, 10,
            1, 19,
            2, 28,
            3, 16,
            4, 25,
            5, 34
    );

    public AdapterChiseledBookshelf() {
        super(ChiseledBookshelf.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull ChiseledBookshelf blockData) {
        for (int i = 0; i < 6; i++) {
            final int slot = slotsMap.get(i);
            final boolean isOccupied = blockData.isSlotOccupied(i);
            final int finalI = i;

            gui.setItem(
                    slot,
                    createItem(Material.BOOK)
                            .setAmount(i + 1)
                            .predicate(isOccupied, ItemBuilder::glow)
                            .setName("Has Book at " + (i + 1))
                            .addLoreIf("This slot is occupied.", isOccupied)
                            .addLoreIf("This slot is not occupied.", !isOccupied)
                            .addLore()
                            .addLore("&6Click to change that!")
                            .asIcon()
            );

            gui.applyState(slot, blockData, d -> d.setSlotOccupied(finalI, !isOccupied));
        }
    }
}

package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterBamboo extends Adapter<Bamboo> {
    public AdapterBamboo() {
        super(Bamboo.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Bamboo blockData) {
        final Bamboo.Leaves leaves = blockData.getLeaves();
        int slot = 21;

        for (Bamboo.Leaves value : Bamboo.Leaves.values()) {
            gui.setItem(
                    slot,
                    createItem(Material.JUNGLE_LEAVES)
                            .setName("&a%s Leaves".formatted(Chat.capitalize(value)))
                            .setAmount(slot - 20)
                            .setSmartLore("Represents the size of the leaves on this bamboo block.")
                            .predicate(leaves.equals(value), ItemBuilder::glow)
                            .build()
            );


            if (!leaves.equals(value)) {
                gui.applyState(slot, blockData, d -> d.setLeaves(value));
            }

            ++slot;
        }

    }
}

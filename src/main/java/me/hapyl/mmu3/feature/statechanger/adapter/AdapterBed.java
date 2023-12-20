package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AdapterBed extends Adapter<Bed> {
    public AdapterBed() {
        super(Bed.class);
    }

    @Override
    public void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull Bed blockData) {
        final Bed.Part part = blockData.getPart();

        gui.setItem(25, createItem(blockData.getMaterial())
                .setName(Chat.capitalize(part) + " Part")
                .setSmartLore("Denotes which half of the bed this block corresponds to.")
                .addLore()
                .addLore("&6Click to change!")
                .build());

        gui.applyState(25, blockData, d -> d.setPart(part == Bed.Part.HEAD ? Bed.Part.FOOT : Bed.Part.HEAD));
    }
}

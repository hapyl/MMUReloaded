package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import org.bukkit.entity.Player;

public class BannerEditorBaseColorGUI extends PanelGUI {

    private final BannerData data;

    public BannerEditorBaseColorGUI(Player player, BannerData data) {
        super(player, "Select Base Color", Size.TWO);

        this.data = data;
        updateInventory();
    }

    @Override
    public void updateInventory() {
        final SmartComponent component = new SmartComponent();

        for (BaseBannerColor color : BaseBannerColor.values()) {
            final ItemBuilder builder = data.createFinalBuilder();

            builder.setType(color.material);
            builder.setName(color.color + Chat.capitalize(color));

            builder.addLore("");
            builder.addLore("&eClick to select");

            component.add(builder.asIcon(), player -> {
                data.setBaseColor(color);
                new BannerEditorGUI(player);
            });
        }

        component.apply(this, SlotPattern.CHUNKY, 0);
        openInventory();
    }
}

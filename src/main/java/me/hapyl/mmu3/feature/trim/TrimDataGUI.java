package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;

public abstract class TrimDataGUI<T extends EnumTrim> extends PanelGUI {

    protected final TrimData data;
    private final T[] values;

    public TrimDataGUI(TrimData data, String name, Size size, T[] values) {
        super(data.getPlayer(), name, size);

        this.data = data;
        this.values = values;

        updateInventory();
    }

    public abstract void onClick(T t);

    @Override
    public void updateInventory() {
        final SmartComponent component = newSmartComponent();

        for (T value : values) {
            component.add(new ItemBuilder(value.getMaterial())
                    .setName(value.getName())
                    .addLore()
                    .addLore("&eClick to select " + value.getName())
                    .asIcon(), player -> onClick(value)
            );
        }

        component.apply(this, SlotPattern.FANCY, 1);
        openInventory();
    }
}

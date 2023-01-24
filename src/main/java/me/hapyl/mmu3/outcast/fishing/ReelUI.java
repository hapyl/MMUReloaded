package me.hapyl.mmu3.outcast.fishing;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.ListIterator;

/**
 * Represents custom reel UI and handles it.
 */
public class ReelUI extends PlayerGUI {

    private final FishData data;
    private final ReelTask task;

    public ReelUI(FishData data) {
        super(data.getPlayer(), "Reel the Fish!", 5);
        this.data = data;

        this.task = new ReelTask(this, data);
        this.task.runTaskTimer(Main.getPlugin(), 1L, 1L);

        setCloseEvent(player -> {
            this.task.cancel();
        });

        // Static clicks
        for (int i = 0; i < getSize(); i++) {
            setClick(i, player -> this.reel());
        }

        openInventory();
    }

    // TODO -> Add rod-based reeling data, for now static increase
    public void reel() {
        task.playerPos += 1.75f;
    }

    public void updateInventory() {
        final ItemBuilder builder = new ItemBuilder(Material.FISHING_ROD);

        builder.setName("&aClick to Reel!");
        final List<String> renderList = Lists.newArrayList();

        for (int i = 0; i < 20; i++) {
            // Render fish position first
            final StringBuilder line = new StringBuilder();
            if (Math.floor(task.fishPos) == i) {
                line.append("&b@@@•");
            }
            else {
                line.append("&0••••••••");
            }

            // Render player pos next
            if (range((int) Math.floor(task.playerPos), i, 3)) {
                line.append("&a🎣🎣🎣");
            }
            else {
                line.append("&0•••••••••");
            }

            // TODO -> Render progress
            renderList.add(line.toString());
        }

        final ListIterator<String> iterator = renderList.listIterator(renderList.size());
        while (iterator.hasPrevious()) {
            builder.addLore(iterator.previous());
        }

        builder.addLore("&cDebug");
        builder.addLore("&fProgress: %s%%", task.progress);
        builder.addLore("&fFish Pos: %s", task.fishPos);
        builder.addLore("&fPlayers Pos: %s", task.playerPos);

        final ItemStack item = builder.build();

        for (int i = 0; i < getSize(); i++) {
            setItem(i, item);
        }
    }

    // FIXME: 016, Jan 16, 2023 -> 3 = 4 bars
    private boolean range(int pos, int i, int range) {
        return (i > pos - range && i < pos + range) || pos == i;
    }

}

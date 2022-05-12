package me.hapyl.mmu3.feature.itemcreator.gui;

import com.google.common.collect.Lists;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.inventory.SignGUI;
import kz.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.feature.itemcreator.Category;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MaterialSubGUI extends ICSubGUI {

    private final int PAGE_ITEM_AMOUNT = 36;
    private final ItemStack PANEL_ITEM_GRAY = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&f").build();

    private final PersistentPlayerData data;
    private Category category;
    private List<Material> items;

    public MaterialSubGUI(Player player, Category category) {
        super(player, "Select Material", Size.FIVE, false);
        this.category = category;
        this.items = category.getItems();
        this.data = PersistentPlayerData.getData(player);
        updateInventory();
        openInventory();
    }

    public void updateInventory(int start) {
        final String query = data.getLastItemCreatorQuery();
        fillItem(0, 8, PANEL_ITEM);
        setPanelGoBack(0, "Item Creator", ItemCreatorGUI::new);
        setPanelCloseMenu(8);

        if (query == null) {
            items = category.getItems();
        }
        else {
            sortByQuery();
        }

        // set arrows
        if (start >= PAGE_ITEM_AMOUNT) {
            setItem(0, new ItemBuilder(Material.ARROW).setName("&aPrevious Page").build());
            setClick(0, player -> updateInventory(start - PAGE_ITEM_AMOUNT));
        }

        if (items.size() - start > PAGE_ITEM_AMOUNT) {
            setItem(8, new ItemBuilder(Material.ARROW).setName("&aNext Page").build());
            setClick(8, player -> updateInventory(start + PAGE_ITEM_AMOUNT));
        }

        // search
        setItem(
                4,
                new ItemBuilder(Material.OAK_SIGN)
                        .setName("&aSearch")
                        .addSmartLore("Search item by it's name.")
                        .addLore()
                        .addLore("Current Query: &b" + (query == null ? "None" : query))
                        .addLore()
                        .addLore("&eLeft Click to enter query")
                        .addLore("&eRight Click to clear")
                        .predicate(query != null, ItemBuilder::glow)
                        .build()
        );

        setClick(4, player -> new SignGUI(player, "Enter Query") {
            @Override
            public void onResponse(Player player, String[] strings) {
                data.setLastItemCreatorQuery(concatString(strings));
                sortByQuery();
                Runnables.runSync(() -> {
                    updateInventory();
                    openInventory();
                });
            }
        }.openMenu(), ClickType.LEFT);

        setClick(4, player -> {
            data.setLastItemCreatorQuery(null);
            items = category.getItems();
            Message.sound(player, Sound.ENTITY_CAT_AMBIENT, 2.0f);
            updateInventory();
        }, ClickType.RIGHT);

        // set items
        for (int i = 0; i < PAGE_ITEM_AMOUNT; i++) {
            int slot = 9 + i;
            if (start + i < items.size()) {
                final Material material = items.get(start + i);
                setItem(slot, new ItemBuilder(material).setName("&a" + Chat.capitalize(material)).addLore("Click to select.").build());
                setClick(slot, player -> {
                    creator().setMaterial(material);
                    new ItemCreatorGUI(player);
                });
            }
            else {
                setItem(slot, PANEL_ITEM_GRAY);
            }
        }

        // set categories
        int slot = 2;
        for (Category value : Category.values()) {
            final boolean selected = value == category;
            setPanelItem(
                    slot,
                    new ItemBuilder(value.getMaterial())
                            .setName("&a" + Chat.capitalize(value.name()))
                            .addSmartLore(value.getString())
                            .addLore()
                            .addLore(selected ? "&eCurrently selected!" : "&eClick to select")
                            .predicate(selected, ItemBuilder::glow)
                            .toItemStack(),
                    player -> {
                        category = value;
                        items = category.getItems();
                        updateInventory(0);
                    }
            );
            ++slot;
        }
    }

    private void sortByQuery() {
        final List<Material> items = Lists.newArrayList();

        for (Category value : Category.values()) {
            final List<Material> categoryItems = value.getItems();
            for (Material material : categoryItems) {
                final String replace = material.name().replace("_", " ").toLowerCase();
                if (replace.contains(data.getLastItemCreatorQuery().toLowerCase())) {
                    items.add(material);
                }
            }
        }

        this.items = items;
    }

    @Override
    public void updateInventory() {
        updateInventory(0);
    }
}

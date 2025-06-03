package me.hapyl.mmu3.feature.itemcreator.gui;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import me.hapyl.eterna.module.inventory.gui.PlayerPageGUI;
import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

// Todo -> Btw Mojang broke something again duh, fix this.
public class MaterialSubGUI extends ItemCreatorSubGUI {

    private static final List<Material> allItems;

    static {
        allItems = Lists.newArrayList();

        for (Material material : Material.values()) {
            if (!material.isAir() && material.isItem()) {
                allItems.add(material);
            }
        }

        // Default sort by names, can't fucking sort by category anymore because, I'll give you one guess........ YES! MOJANG BROKE IT!
        allItems.sort(Comparator.comparing(Material::name));
    }

    private final int PAGE_ITEM_AMOUNT = 36;
    private final ItemStack PANEL_ITEM_GRAY = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&f").build();

    private final PersistentPlayerData data;
    private List<Material> items;
    private int start;

    public MaterialSubGUI(Player player) {
        super(player, "Select Material", Size.FIVE);

        this.data = PersistentPlayerData.getData(player);
        this.items = Lists.newArrayList(allItems);

        openInventory();
    }

    public void openInventory(int start) {
        this.start = start;

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final String query = data.getLastItemCreatorQuery();

        sortByQuery();

        // set arrows
        if (start >= PAGE_ITEM_AMOUNT) {
            setItem(0, PlayerPageGUI.ICON_PAGE_PREVIOUS);
            setAction(0, player -> openInventory(start - PAGE_ITEM_AMOUNT));
        }

        if (items.size() - start > PAGE_ITEM_AMOUNT) {
            setItem(8, PlayerPageGUI.ICON_PAGE_NEXT);
            setAction(8, player -> openInventory(start + PAGE_ITEM_AMOUNT));
        }

        // Search
        setItem(
                4,
                new ItemBuilder(Material.OAK_SIGN)
                        .setName("&aSearch")
                        .addSmartLore("Search item by it's name.")
                        .addLore()
                        .addLore("Current Query: &b" + (query == null ? "None" : query))
                        .addLore()
                        .addLore("&eLeft Click to enter query")
                        .addLore("&6Right Click to clear")
                        .predicate(query != null, ItemBuilder::glow)
                        .build()
        );

        setAction(
                4, player -> new SignGUI(player, "Enter Query") {
                    @Override
                    public void onResponse(Response response) {
                        data.setLastItemCreatorQuery(response.getAsString());
                        start = 0; // Reset pointer as well
                        runSync(() -> openInventory());
                    }
                }, ClickType.LEFT
        );

        setAction(
                4, player -> {
                    data.setLastItemCreatorQuery(null);
                    items = Lists.newArrayList(allItems);
                    Message.sound(player, Sound.ENTITY_CAT_AMBIENT, 2.0f);
                    openInventory();
                }, ClickType.RIGHT
        );

        // Set items
        for (int i = 0; i < PAGE_ITEM_AMOUNT; i++) {
            int slot = 9 + i;
            if (start + i < items.size()) {
                final Material material = items.get(start + i);
                setItem(slot, new ItemBuilder(material).setName("&a" + Chat.capitalize(material)).addLore("Click to select.").build());
                setAction(
                        slot, player -> {
                            creator().setMaterial(material);
                            new ItemCreatorGUI(player);
                        }
                );
            }
            else {
                setItem(slot, PANEL_ITEM_GRAY);
            }
        }

    }

    private void sortByQuery() {
        @Nullable final String query = data.getLastItemCreatorQuery();

        if (query == null) {
            return;
        }

        this.items = allItems.stream()
                .filter(material -> {
                    final String materialName = material.name().replace("_", " ").toLowerCase();

                    return materialName.contains(query.toLowerCase());
                })
                .toList();
    }

}

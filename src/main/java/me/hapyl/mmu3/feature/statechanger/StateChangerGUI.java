package me.hapyl.mmu3.feature.statechanger;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.statechanger.adapter.Adapter;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StateChangerGUI extends PanelGUI {

    private static final String REPORT_MISSING_MODIFIER_URL = "https://github.com/hapyl/MMUReloaded/issues/new?template=replacer-missing-modifiers.md";

    private final Data data;

    public StateChangerGUI(Player player, String name, Data data) {
        super(player, name, Size.FIVE);
        this.data = data;

        setBottomPanel();
        updateInventory();
    }

    public void updateInventory() {
        final BlockData blockData = data.getBlockData();

        Main.getRegistry().stateChanger.getAdapters().forEach(adapter -> {
            adapter.updateIfInstance(this, player, blockData);
        });

        // Open Inventory
        openInventory();
    }

    // FIXME (hapyl): 012, Mar 12: Maybe add like auto adapters but like I kinda like that water is always in the same slot idk man
    public void setItem(Adapter<?> adapter, int slot, boolean condition, @Nonnull Material material, @Nonnull String name, @Nonnull String description, @Nullable Object... format) {
        setItem(adapter, slot, ConditionedMaterial.of(condition, material, material), name, description, format);
    }

    public void setItem(Adapter<?> adapter, int slot, @Nonnull ConditionedMaterial material, @Nonnull String name, @Nonnull String description, @Nullable Object... format) {
        final boolean condition = material.isCondition();

        // Color formats
        if (format != null) {
            for (int i = 0; i < format.length; i++) {
                format[i] = "&f&l" + format[i] + "&7";
            }
        }

        setItem(
                slot,
                new ItemBuilder(material.getMaterial())
                        .setName((condition ? ChatColor.GREEN : ChatColor.RED) + name)
                        .addLore(adapter.toString())
                        .addLore()
                        .addTextBlockLore(description.formatted(format))
                        .addLore()
                        .addLore(ChatColor.GOLD + "Click to toggle!")
                        .predicate(condition, ItemBuilder::glow)
                        .asIcon()
        );
    }

    public <T extends BlockData> void applyState(int slot, @Nonnull T data, @Nonnull Consumer<T> consumer) {
        setClick(slot, player -> {
            final Block block = this.data.getBlock();

            consumer.accept(data);
            block.setBlockData(data, false);
            playSoundAndUpdateInventory();
        });
    }

    public <T> void setSwitchItem(Adapter<?> adapter, int slot, @Nonnull T[] values, @Nonnull T currentValue, @Nonnull Material material, @Nonnull String name, @Nonnull String description, @Nullable Object... format) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name)
                .addLore(adapter.toString())
                .addLore()
                .addTextBlockLore(description.formatted(format))
                .addLore();

        for (T t : values) {
            builder.addLore("&a" + (currentValue.equals(t) ? " ➥ &l" : "") + Chat.capitalize(t.toString()));
        }

        builder.addLore();
        builder.addLore("&eLeft Click to cycle.");
        builder.addLore("&6Right Click to cycle backwards.");

        setItem(slot, builder.build());
    }

    public <T extends BlockData, V> void applySwitch(int slot, @Nonnull T blockData, @Nonnull V[] values, @Nonnull V currentValue, @Nonnull BiConsumer<T, V> consumer) {
        setClick(
                slot,
                player -> {
                    applyState(blockData, d -> consumer.accept(d, CollectionUtils.getNextValue(values, currentValue)));
                },
                ClickType.LEFT
        );

        setClick(
                slot,
                player -> {
                    applyState(blockData, d -> consumer.accept(d, CollectionUtils.getPreviousValue(values, currentValue)));
                },
                ClickType.RIGHT
        );
    }

    public void setLevelableItem(Adapter<?> adapter, int slot, int level, int maxLevel, Material material, String name, String description, @Nullable Object... format) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name)
                .setAmount(Math.max(level, 1))
                .addLore(adapter.toString())
                .addLore()
                .addTextBlockLore(description.formatted(format));
        final String nameLowercase = name.toLowerCase(Locale.ROOT);

        builder.addLore();
        builder.addLore("&aCurrent %s: &l%s".formatted(name, level));
        builder.addLore("&aMaximum %s: &l%s".formatted(name, maxLevel));
        builder.addLore();
        builder.addLore("&eLeft Click to increase %s.".formatted(nameLowercase));
        builder.addLore("&6Right Click to decrease %s.".formatted(nameLowercase));

        setItem(slot, builder.build());
    }

    public <T extends BlockData> void applyLevelable(int slot, T blockData, int level, int maxLevel, BiConsumer<T, Integer> consumer) {
        applyLevelable(slot, blockData, 0, level, maxLevel, consumer);
    }

    public <T extends BlockData> void applyLevelable(int slot, T blockData, int minLevel, int level, int maxLevel, BiConsumer<T, Integer> consumer) {
        setClick(
                slot,
                player -> applyState(blockData, d -> consumer.accept(d, level + 1 > maxLevel ? minLevel : level + 1)),
                ClickType.LEFT
        );
        setClick(
                slot,
                player -> applyState(blockData, d -> consumer.accept(d, level - 1 < minLevel ? maxLevel : level - 1)),
                ClickType.RIGHT
        );
    }

    private void setBottomPanel() {
        setPanelCloseMenu();

        // Missing data
        setPanelItem(
                6,
                new ItemBuilder(Material.MAP)
                        .setName("Missing Modifiers?")
                        .setSmartLore("If this block is missing some modifiers, you can report it!")
                        .addLore()
                        .addLore("&6Click to get link!")
                        .build(), player -> {
                    Chat.sendClickableHoverableMessage(
                            player,
                            LazyEvent.openUrl(REPORT_MISSING_MODIFIER_URL),
                            LazyEvent.showText("&eClick to open the link!"),
                            "&6&lCLICK HERE&f to report a missing modifier!"
                    );

                    PlayerLib.plingNote(player, 2.0f);
                    player.closeInventory();
                }
        );

        // Restore
        setPanelItem(
                7,
                new ItemBuilder(Material.COMMAND_BLOCK)
                        .setName("&aRestore Block")
                        .setSmartLore("Restores blocks to its original form before opening the menu.")
                        .build(),
                player -> {
                    data.restoreOriginalBlockData();
                    player.closeInventory();
                    Message.success(player, "Restored.");
                }
        );

        setItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&cNo Modifier").build(), 10, 19, 28, 16, 25, 34);
    }

    private void setItem(ItemStack item, int... slots) {
        for (int slot : slots) {
            setItem(slot, item);
        }
    }

    private <T extends BlockData> void applyState(T t, Consumer<T> consumer) {
        final Block block = data.getBlock();

        consumer.accept(t);
        block.setBlockData(t, false);
        playSoundAndUpdateInventory();
    }

    private void playSoundAndUpdateInventory() {
        PlayerLib.playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        updateInventory();
    }

}

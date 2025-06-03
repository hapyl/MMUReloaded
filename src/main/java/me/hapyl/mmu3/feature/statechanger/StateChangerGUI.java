package me.hapyl.mmu3.feature.statechanger;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.statechanger.adapter.Adapter;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StateChangerGUI extends PanelGUI {

    private static final String REPORT_MISSING_MODIFIER_URL = "https://github.com/hapyl/MMUReloaded/issues/new?template=replacer-missing-modifiers.md";

    private final StateChangerData data;

    public StateChangerGUI(Player player, String name, StateChangerData data) {
        super(player, name, Size.FOUR);
        this.data = data;

        openInventory();
    }

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
                        .setName(name + " " + BukkitUtils.checkmark(condition))
                        .addLore(adapter.toString())
                        .addLore()
                        .addTextBlockLore(description.formatted(format))
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to toggle")
                        .predicate(condition, ItemBuilder::glow)
                        .asIcon()
        );
    }

    public <T extends BlockData> void applyState(int slot, @Nonnull T data, @Nonnull Consumer<T> consumer) {
        setAction(
                slot, player -> {
                    final Block block = this.data.getBlock();

                    consumer.accept(data);
                    block.setBlockData(data, false);
                    playSoundAndUpdateInventory();
                }
        );
    }

    public <T> void setSwitchItem(Adapter<?> adapter, int slot, @Nonnull T[] values, @Nonnull T currentValue, @Nonnull Material material, @Nonnull String name, @Nonnull String description, @Nullable Object... format) {
        final ItemBuilder builder = new ItemBuilder(material)
                .setName(name)
                .addLore(adapter.toString())
                .addLore()
                .addTextBlockLore(description.formatted(format))
                .addLore();

        for (T t : values) {
            builder.addLore((currentValue.equals(t) ? "&b ➥ " : "&8 ") + Chat.capitalize(t.toString()));
        }

        builder.addLore();
        builder.addLore("&8◦ &eLeft-Click to cycle");
        builder.addLore("&8◦ &6Right-Click to cycle backwards");

        setItem(slot, builder.build());
    }

    @Override
    public void setItem(int slot, @Nullable ItemStack item) {
        super.setItem(slot, item);

        // This is really a hacky wacky fucky ducky mucky way of doing this by I ain't rewriting everything
        // just because my stupid ass wants player heads to show their textures
        fixBlockSpecificState(slot);
    }

    public <T extends BlockData, V> void applySwitch(int slot, @Nonnull T blockData, @Nonnull V[] values, @Nonnull V currentValue, @Nonnull BiConsumer<T, V> consumer) {
        setAction(
                slot,
                player -> applyState(blockData, d -> consumer.accept(d, CollectionUtils.getNextValue(values, currentValue))),
                ClickType.LEFT
        );

        setAction(
                slot,
                player -> applyState(blockData, d -> consumer.accept(d, CollectionUtils.getPreviousValue(values, currentValue))),
                ClickType.RIGHT
        );
    }

    public void setLevelableItem(Adapter<?> adapter, int slot, int level, int maxLevel, Material material, String name, String description) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name)
                .setAmount(Math.max(level, 1))
                .addLore(adapter.toString())
                .addLore()
                .addTextBlockLore(description);
        final String nameLowercase = name.toLowerCase(Locale.ROOT);

        builder.addLore();
        builder.addLore("Current %s: &a%s&8/&6%s".formatted(name, level, maxLevel));
        builder.addLore();
        builder.addLore("&8◦ &eLeft-Click to increase %s".formatted(nameLowercase));
        builder.addLore("&8◦ &6Right-Click to decrease %s".formatted(nameLowercase));

        setItem(slot, builder.build());
    }

    public <T extends BlockData> void applyLevelable(int slot, T blockData, int level, int maxLevel, BiConsumer<T, Integer> consumer) {
        applyLevelable(slot, blockData, 0, level, maxLevel, consumer);
    }

    public <T extends BlockData> void applyLevelable(int slot, T blockData, int minLevel, int level, int maxLevel, BiConsumer<T, Integer> consumer) {
        setAction(
                slot,
                player -> applyState(blockData, d -> consumer.accept(d, level + 1 > maxLevel ? minLevel : level + 1)),
                ClickType.LEFT
        );
        setAction(
                slot,
                player -> applyState(blockData, d -> consumer.accept(d, level - 1 < minLevel ? maxLevel : level - 1)),
                ClickType.RIGHT
        );
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        fillRow(0, PANEL_ITEM);
        setBottomPanel();

        final BlockData blockData = data.getBlockData();

        Main.getRegistry().stateChanger.getAdapters().forEach(adapter -> {
            adapter.updateIfInstance(this, player, blockData, data);
        });
    }

    public void fixBlockSpecificState(int slot) {
        // Fix skulls
        if (data.getBlock().getState() instanceof Skull skull) {
            final ItemStack item = getItem(slot);

            if (item != null) {
                ItemBuilder.modifyMeta(
                        item, SkullMeta.class, meta -> meta.setPlayerProfile(skull.getPlayerProfile())
                );
            }
        }
    }

    private void setBottomPanel() {
        setPanelCloseMenu();

        // Missing data
        setPanelItem(
                6,
                new ItemBuilder(Material.MAP)
                        .setName("Missing Modifiers?")
                        .addTextBlockLore("""
                                Is this block missing modifiers?
                                Report it!
                                
                                &8◦ &eLeft-Click to get link
                                """)
                        .asIcon(), player -> {
                    Chat.sendClickableHoverableMessage(
                            player,
                            LazyEvent.openUrl(REPORT_MISSING_MODIFIER_URL),
                            LazyEvent.showText("&eClick to open the link!"),
                            Message.PREFIX + "&6&lCLICK HERE&f to report a missing modifier!"
                    );

                    PlayerLib.plingNote(player, 2.0f);
                    player.closeInventory();
                }
        );

        // Restore
        setPanelItem(
                7,
                new ItemBuilder(Material.COMMAND_BLOCK)
                        .setName("Restore Block")
                        .addTextBlockLore("""
                                Restores this block to its original state before opening the menu.
                                
                                &8◦ &eLeft-Click to restore
                                """)
                        .asIcon(),
                player -> {
                    data.restoreOriginalBlockData();
                    player.closeInventory();
                    Message.success(player, "Restored!");
                }
        );

        setItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("&cNo Modifier").build(), 10, 19, 28, 16, 25, 34);
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
        PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        openInventory();
    }

}

package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.GUIEventListener;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings("deprecation")
public class StandEditorModifyTeamColorGUI extends PanelGUI implements GUIEventListener {

    private static final Map<ChatColor, ItemStack> validColors = Map.ofEntries(
            Map.entry(ChatColor.WHITE, makeBanner(Material.WHITE_BANNER, null)),
            Map.entry(ChatColor.GRAY, makeBanner(Material.LIGHT_GRAY_BANNER, null)),
            Map.entry(ChatColor.DARK_GRAY, makeBanner(Material.GRAY_BANNER, null)),
            Map.entry(ChatColor.BLACK, makeBanner(Material.BLACK_BANNER, null)),
            Map.entry(ChatColor.RED, makeBanner(Material.RED_BANNER, null)),
            Map.entry(ChatColor.DARK_RED, makeBanner(Material.RED_BANNER, new Pattern(DyeColor.BLACK, PatternType.GRADIENT))),
            Map.entry(ChatColor.GOLD, makeBanner(Material.ORANGE_BANNER, null)),
            Map.entry(ChatColor.YELLOW, makeBanner(Material.YELLOW_BANNER, null)),
            Map.entry(ChatColor.GREEN, makeBanner(Material.LIME_BANNER, null)),
            Map.entry(ChatColor.DARK_GREEN, makeBanner(Material.GREEN_BANNER, null)),
            Map.entry(ChatColor.AQUA, makeBanner(Material.LIGHT_BLUE_BANNER, null)),
            Map.entry(ChatColor.DARK_AQUA, makeBanner(Material.CYAN_BANNER, null)),
            Map.entry(ChatColor.BLUE, makeBanner(Material.BLUE_BANNER, null)),
            Map.entry(ChatColor.DARK_BLUE, makeBanner(Material.BLUE_BANNER, new Pattern(DyeColor.BLACK, PatternType.GRADIENT))),
            Map.entry(ChatColor.LIGHT_PURPLE, makeBanner(Material.MAGENTA_BANNER, null)),
            Map.entry(ChatColor.DARK_PURPLE, makeBanner(Material.PURPLE_BANNER, null))
    );

    private static final ItemStack ITEM_GO_BACK = ItemBuilder.playerHeadUrl("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9")
            .setName("Go Back")
            .addTextBlockLore("""
                    Changed you mind instead of team color?
                    
                    &8◦ &eLeft-Click to return to Stand Editor
                    """)
            .asIcon();


    private final StandEditorData data;
    private final Team team;

    public StandEditorModifyTeamColorGUI(Player player, StandEditorData data, Team team) {
        super(player, "Modify Team Color", Size.TWO);

        this.data = data;
        this.team = team;

        openInventory();
    }

    @Override
    public void onClose(@Nonnull InventoryCloseEvent event) {
        data.await = null;

        BukkitUtils.runLater(
                () -> {
                    // In case killed, don't reopen the editor
                    if (!data.isEditing()) {
                        return;
                    }

                    new StandEditorGUI(player, data);
                }, 1
        );
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final ChatColor currentColor = team.getColor();
        final SmartComponent smartComponent = newSmartComponent();

        // Iterating over enum to preserve the order
        for (ChatColor color : ChatColor.values()) {
            final ItemStack stack = validColors.get(color);
            final boolean isCurrentColor = currentColor == color;

            // Not supported color, don't care
            if (stack == null) {
                continue;
            }

            final String colorNameColorized = color + Chat.capitalize(color.name());

            final ItemBuilder builder = new ItemBuilder(stack)
                    .setName(colorNameColorized)
                    .addTextBlockLore("""
                            Changes the team color to %s&7.
                            """.formatted(colorNameColorized))
                    .addLore();

            if (isCurrentColor) {
                builder.addLore("&a&oCurrent color!");
                builder.addLore();
                builder.addLore("&8◦ &eLeft-Click to return to Stand Editor");
                builder.glow();

                smartComponent.add(builder.asIcon(), Player::closeInventory);
            }
            else {
                builder.addLore("&8◦ &eLeft-Click to change the color");

                smartComponent.add(
                        builder.asIcon(), player -> {
                            Message.info(player, "Set team color to %s.", colorNameColorized);

                            team.setColor(color);
                            player.closeInventory();
                        }
                );
            }
        }

        // Just a cancel button, yeah you can close the GUI but no one knows nor reads that
        setItem(22, ITEM_GO_BACK, Player::closeInventory);

        smartComponent.apply(this, SlotPattern.CHUNKY, 0);
    }

    private static ItemStack makeBanner(Material base, @Nullable Pattern pattern) {
        return pattern != null ? new ItemBuilder(base).setBannerPattern(pattern).getItem() : new ItemStack(base);
    }
}

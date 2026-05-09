package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.data.PlayerData;
import me.hapyl.mmu3.feature.FeatureBase;
import me.hapyl.mmu3.feature.FeatureKey;
import me.hapyl.mmu3.feature.banner.BannerData;
import me.hapyl.mmu3.feature.banner.BannerEditorMenu;
import me.hapyl.mmu3.feature.banner.BannerSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MMUOperationBannerEditor implements MMUOperation, FeatureBase {

    @Override
    public @NotNull String name() {
        return "banner_editor";
    }

    @Override
    public @NotNull String description() {
        return "Opens the banner editor menu.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        // No argument opens the menu
        if (args.length == 0) {
            new BannerEditorMenu(player, 1);
            return;
        }

        final String operation = args.get(0).toString();

        if (operation.equalsIgnoreCase("clear")) {
            PlayerData.ofPlayer(player).requestData(BannerData.class).reset();
            
            MMULogger.success(player, Component.text("Reset banner editor!"));
            return;
        }
        
        final Optional<ItemStack> banner = BannerSerializer.deserialize(args.get(0).toString());
        
        if (banner.isEmpty()) {
            MMULogger.error(player, Component.text("Failed to deserialize banner, make sure the code is formatted properly!"));
            return;
        }

        player.getInventory().addItem(banner.get());
        MMULogger.success(player, Component.text("Gave you a banner!"));
    }
    
    @Override
    public @NotNull FeatureKey getKey() {
        return Main.featureRegistry().bannerEditor.getKey();
    }
}

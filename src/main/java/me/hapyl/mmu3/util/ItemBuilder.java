package me.hapyl.mmu3.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemBuilder {
    
    private static final Style STYLE_NAME = Style.style(NamedTextColor.GREEN);
    private static final Style STYLE_LORE = Style.style(NamedTextColor.GRAY);
    
    private static final String MINECRAFT_SKIN_URL = "https://textures.minecraft.net/texture/";
    
    @NotNull private ItemStack itemStack;
    
    public ItemBuilder(@NotNull Material material) {
        this(new ItemStack(material));
    }
    
    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    @NotNull
    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    
    @NotNull
    public ItemBuilder setName(@NotNull Component name) {
        return editMeta(meta -> meta.itemName(ComponentHelper.applyDefaultStyle(name, STYLE_NAME)));
    }
    
    @NotNull
    public ItemBuilder setLore(@NotNull Component... lore) {
        return editExistingLore(existingLore -> Arrays.stream(lore)
                                                      .map(component -> ComponentHelper.applyDefaultStyle(component, STYLE_LORE))
                                                      .toList());
    }
    
    @NotNull
    public ItemBuilder addLore(@NotNull Component lore) {
        return editExistingLore(existingLore -> {
            existingLore.add(ComponentHelper.applyDefaultStyle(lore, STYLE_LORE));
            return existingLore;
        });
    }
    
    @NotNull
    public ItemBuilder addLore() {
        return editExistingLore(existingLore -> {
            existingLore.add(Component.empty());
            return existingLore;
        });
    }
    
    @NotNull
    public ItemBuilder addLore(@NotNull List<? extends Component> lore) {
        return editExistingLore(existingLore -> {
            existingLore.addAll(lore);
            return existingLore;
        });
    }
    
    @NotNull
    public <M extends ItemMeta> ItemBuilder editMeta(@NotNull Class<M> metaClass, @NotNull Consumer<M> consumer) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        
        if (metaClass.isInstance(itemMeta)) {
            consumer.accept(metaClass.cast(itemMeta));
            itemStack.setItemMeta(itemMeta);
        }
        
        return this;
    }
    
    @NotNull
    public ItemBuilder editMeta(@NotNull Consumer<ItemMeta> consumer) {
        return editMeta(ItemMeta.class, consumer);
    }
    
    @NotNull
    public ItemStack build() {
        return itemStack;
    }
    
    @NotNull
    public ItemStack icon() {
        editMeta(meta -> meta.setHideTooltip(true));
        
        return itemStack;
    }
    
    @NotNull
    public ItemBuilder predicate(boolean predicate, @NotNull Consumer<ItemBuilder> edit) {
        if (predicate) {
            edit.accept(this);
        }
        
        return this;
    }
    
    @NotNull
    public ItemBuilder setType(@NotNull Material material) {
        itemStack = itemStack.withType(material);
        return this;
    }
    
    @NotNull
    public ItemBuilder glow() {
        return editMeta(meta -> meta.setEnchantmentGlintOverride(true));
    }
    
    @NotNull
    public ItemBuilder setItemModel(Material material) {
        return editMeta(meta -> meta.setItemModel(material.getKey()));
    }
    
    @NotNull
    public <P, C> ItemBuilder setPersistentData(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @Nullable C value) {
        return editMeta(meta -> {
            final PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            
            if (value == null) {
                persistentDataContainer.remove(key);
            }
            else {
                persistentDataContainer.set(key, type, value);
            }
        });
    }
    
    @NotNull
    public ItemBuilder setMaxStackSize(int maxStackSize) {
        return editMeta(meta -> meta.setMaxStackSize(maxStackSize));
    }
    
    @NotNull
    private ItemBuilder editExistingLore(@NotNull Function<@NotNull List<Component>, @NotNull List<Component>> edit) {
        return editMeta(meta -> {
            final List<Component> existingLore = Objects.requireNonNullElseGet(meta.lore(), Lists::newArrayList);
            
            meta.lore(edit.apply(existingLore));
        });
    }
    
    @NotNull
    public static ItemBuilder playerHead(@NotNull String url) {
        final ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
        
        builder.editMeta(
                SkullMeta.class, meta -> {
                    final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
                    final PlayerTextures textures = playerProfile.getTextures();
                    
                    try {
                        textures.setSkin(URI.create(MINECRAFT_SKIN_URL + url).toURL());
                    }
                    catch (MalformedURLException ex) {
                        throw new IllegalArgumentException("Failed to create minecraft skin texture: %s".formatted(ex.getMessage()), ex);
                    }
                    
                    playerProfile.setTextures(textures);
                    meta.setPlayerProfile(playerProfile);
                }
        );
        
        return builder;
    }
    
}

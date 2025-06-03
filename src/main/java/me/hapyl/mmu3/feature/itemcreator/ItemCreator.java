package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.feature.itemcreator.gui.LoreSubGUI;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * TODO for Item Creator
 * - Add supports for components.
 * - Bring back the Minecraft Command
 * - Add save/load presets, maybe even copy/paste command
 */
public class ItemCreator {

    private final Player player;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantMap;
    private final Map<Attribute, AttributeModifier> attributes;

    public boolean hideFlags;

    private String name;
    private Material material;
    private int amount;
    private String headTexture;
    private Color armorColor;

    public ItemCreator(Player player) {
        this.player = player;
        this.material = Material.GRASS_BLOCK;
        this.amount = 1;
        this.name = null;
        this.lore = Lists.newArrayList();
        this.enchantMap = Maps.newHashMap();
        this.attributes = Maps.newHashMap();
    }

    public String getHeadTexture() {
        return headTexture;
    }

    public void setHeadTexture(String headTexture) {
        this.headTexture = headTexture;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name == null ? Chat.capitalize(material) : name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public boolean hasName() {
        return name != null;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<Enchantment, Integer> getEnchantMap() {
        return enchantMap;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = Math.clamp(amount, 1, 100);
    }

    public ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(buildFinalItem());

        builder.addLore("&8&m ".repeat(42));
        builder.addTextBlockLore("""
                This is a preview of your item, click the button at the bottom to build your item!
                
                &8◦ &eLeft-Click to change material
                &8◦ &6Right-Click to pick amount
                """);

        return builder.toItemStack();
    }

    public ItemStack buildFinalItem() {
        final ItemBuilder builder = new ItemBuilder(material).setAmount(amount);

        if (name != null) {
            builder.setName(name);
        }

        if (!lore.isEmpty()) {
            for (String str : lore) {
                builder.addLore(str);
            }
        }

        if (!enchantMap.isEmpty()) {
            enchantMap.forEach(builder::addEnchant);
        }

        if (headTexture != null && material == Material.PLAYER_HEAD) {
            builder.setHeadTextureUrl(headTexture);
        }

        if (armorColor != null) {
            builder.setLeatherArmorColor(armorColor);
        }

        if (hideFlags) {
            builder.hideFlags();
        }

        return builder.build();
    }

    public void setItem(ItemStack item) {
        material = item.getType();
        amount = item.getAmount();
        name = Chat.capitalize(material);
        lore.clear();
        enchantMap.clear();

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        final List<String> lore = meta.getLore();
        final Map<Enchantment, Integer> enchants = meta.getEnchants();

        if (lore != null) {
            this.lore.addAll(lore);
        }

        enchantMap.putAll(enchants);

        if (meta.hasDisplayName()) {
            name = meta.getDisplayName();
        }
    }

    public void addSmartLore(String string) {
        if (lore.size() >= LoreSubGUI.MAX_LORE_LINES) {
            return;
        }

        this.lore.addAll(ItemBuilder.splitString(string, 30));
        Message.info(player, "Added smart lore.");
    }

    public void setSmartLore(String string) {
        this.lore.clear();
        addSmartLore(string);
        Message.info(player, "Set new smart lore.");
    }

    public void setAttribute(Attribute attribute, AttributeModifier modifier) {
        attributes.put(attribute, modifier);
    }

    public Map<Attribute, AttributeModifier> getAttributes() {
        return attributes;
    }

    public Color getOrCreateColor() {
        if (armorColor == null) {
            armorColor = Color.fromBGR(0, 0, 0);
        }

        return armorColor;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public void setArmorColor(Color color) {
        armorColor = color;
    }

    public net.md_5.bungee.api.ChatColor getArmorColorAsChatColor() {
        return armorColor ==
                null ? net.md_5.bungee.api.ChatColor.DARK_GRAY : net.md_5.bungee.api.ChatColor.of(new java.awt.Color(
                armorColor.getRed(),
                armorColor.getGreen(),
                armorColor.getBlue()
        ));
    }
}
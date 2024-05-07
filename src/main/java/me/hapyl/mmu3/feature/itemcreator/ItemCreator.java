package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemCreator {

    private String name;
    private Material material;
    private int amount;
    private int customModelData;
    private String headTexture;
    private Color armorColor;

    private final Player player;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantMap;
    private final Map<Attribute, AttributeModifier> attributes;

    public ItemCreator(Player player) {
        this.player = player;
        this.material = Material.GRASS_BLOCK;
        this.amount = 1;
        this.customModelData = 0;
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

    public boolean hasName() {
        return name != null;
    }

    public void setName(@Nullable String name) {
        this.name = name;
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
        this.amount = Numbers.clamp(amount, 1, 64);
    }

    public String buildMinecraftGiveItem() {
        final StringBuilder command = new StringBuilder("give @s %s{".formatted(material.getKey().getKey().toLowerCase()));

        // display
        if (name != null || !lore.isEmpty()) {
            final StringBuilder display = new StringBuilder("display: {");

            if (name != null) {
                display.append("Name: '\"%s\"'".formatted(name));
            }

            if (!lore.isEmpty()) {
                final StringBuilder loreBuilder = new StringBuilder("Lore: [");
                if (name != null) {
                    display.append(", ");
                }

                for (int i = 0; i < lore.size(); i++) {
                    final String loreLine = lore.get(i);
                    if (i != 0) {
                        loreBuilder.append(", ");
                    }

                    loreBuilder.append("'\"%s\"'".formatted(loreLine));
                }

                display.append(loreBuilder.append("]"));
            }

            command.append(display.append("}"));
        }

        return command.append("}").toString();
    }

    public ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(buildFinalItem());

        builder.addLore("&8&m                                   ");
        builder.addLore();
        builder.addSmartLore("This is a preview of your item! Click the button at the bottom to build your item.", "&7&o");
        builder.addLore();
        builder.addLore("&eClick to change material");
        builder.addLore("&6Right Click to pick amount");

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
        this.lore.addAll(ItemBuilder.splitString(string, 30));
        Message.info(player, "Added smart lore.");
    }

    public void setSmartLore(String string) {
        this.lore.clear();
        addSmartLore(string);
        Message.info(player, "Set new smart lore.");
    }

    public void setCustomModelData(int value) {
        customModelData = value;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setAttribute(Attribute attribute, AttributeModifier modifier) {
        attributes.put(attribute, modifier);
    }

    public Map<Attribute, AttributeModifier> getAttributes() {
        return attributes;
    }

    @Nonnull
    public AttributeModifier getAttributeModifierOrCompute(LinkedAttribute attribute) {
        final Attribute link = attribute.getLink();
        return attributes.computeIfAbsent(link, m -> new AttributeModifier(attribute.name(), 0.0d, AttributeModifier.Operation.ADD_NUMBER));
    }

    public void setArmorColor(Color color) {
        armorColor = color;
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

    public net.md_5.bungee.api.ChatColor getArmorColorAsChatColor() {
        return armorColor ==
                null ? net.md_5.bungee.api.ChatColor.DARK_GRAY : net.md_5.bungee.api.ChatColor.of(new java.awt.Color(
                armorColor.getRed(),
                armorColor.getBlue(),
                armorColor.getGreen()
        ));
    }
}
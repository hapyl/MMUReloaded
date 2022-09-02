package me.hapyl.mmu3.feature.itemcreator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemCreator {

    private final Player player;
    private String name;
    private Material material;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantMap;
    private int amount;

    public ItemCreator(Player player) {
        this.player = player;
        this.material = Material.GRASS_BLOCK;
        this.amount = 1;
        this.name = null;
        this.lore = Lists.newArrayList();
        this.enchantMap = Maps.newHashMap();
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

        return builder.toItemStack();
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

    public String exportCode() {
        final StringBuilder builder = new StringBuilder("{");

        builder.append("Id=").append(material.name().toLowerCase()).append(";");
        builder.append("Count=").append(amount).append(";");
        builder.append("Name=").append(name.replace("§", "&")).append(";");

        if (!lore.isEmpty()) {
            builder.append("Lore=[");
            for (int i = 0; i < lore.size(); i++) {
                final String str = lore.get(i);
                builder.append(str.replace("§", "&"));
                if (i <= lore.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("];");
        }

        if (!enchantMap.isEmpty()) {
            builder.append("Ench=[");
            enchantMap.forEach((e, l) -> {

            });
            builder.append("];");
        }

        return builder.append("}").toString();
    }

    public void importCode(String code) {

    }

    public boolean validateCode(String string) {
        return string.startsWith("{") && string.endsWith("}") && string.contains("Id=");
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
}

package me.hapyl.mmu3.feature.itemcreator;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

public enum LinkedAttribute {

    MAX_HEALTH(Attribute.MAX_HEALTH, Material.APPLE, "Maximum health of an Entity."),
    FOLLOW_RANGE(Attribute.FOLLOW_RANGE, Material.LEAD, "Range at which an Entity will follow others."),
    KNOCKBACK_RESISTANCE(Attribute.KNOCKBACK_RESISTANCE, Material.NETHERITE_INGOT, "Resistance of an Entity to knockback."),
    MOVEMENT_SPEED(Attribute.MOVEMENT_SPEED, Material.RABBIT_FOOT, "Movement speed of an Entity."),
    FLYING_SPEED(
            Attribute.FLYING_SPEED,
            Material.ELYTRA,
            "Flying speed of an Entity. Only works for bees and parrots. (Unfortunately)"
    ),
    ATTACK_DAMAGE(Attribute.ATTACK_DAMAGE, Material.IRON_SWORD, "Attack damage of an Entity."),
    ATTACK_KNOCKBACK(Attribute.ATTACK_KNOCKBACK, Material.SLIME_BALL, "Attack knockback of an Entity."),
    ATTACK_SPEED(Attribute.ATTACK_SPEED, Material.DIAMOND_HOE, "Attack speed of an Entity."),
    ARMOR(Attribute.ARMOR, Material.IRON_CHESTPLATE, "Armor bonus of an Entity.S"),
    ARMOR_TOUGHNESS(Attribute.ARMOR_TOUGHNESS, Material.IRON_BLOCK, "Armor durability bonus of an Entity."),
    LUCK(Attribute.LUCK, Material.BIG_DRIPLEAF, "Luck bonus of an Entity."),
    HORSE_JUMP_STRENGTH(Attribute.JUMP_STRENGTH, Material.LEATHER_HORSE_ARMOR, "Strength with which a horse will jump."),
    ZOMBIE_SPAWN_REINFORCEMENTS(Attribute.SPAWN_REINFORCEMENTS, Material.ZOMBIE_HEAD, "Chance of a zombie to spawn reinforcements.");

    private final Attribute link;
    private final Material material;
    private final String description;

    LinkedAttribute(Attribute link, Material material, String description) {
        this.link = link;
        this.material = material;
        this.description = description;
    }

    public Attribute getLink() {
        return link;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return Chat.capitalize(this);
    }
}

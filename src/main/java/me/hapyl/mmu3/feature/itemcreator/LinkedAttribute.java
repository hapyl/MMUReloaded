package me.hapyl.mmu3.feature.itemcreator;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nonnull;

public enum LinkedAttribute {

    MAX_HEALTH(
            Attribute.MAX_HEALTH, Material.APPLE, """
            The maximum health of this mob in half-hearts.
            """, 1, 1024
    ),
    FOLLOW_RANGE(
            Attribute.FOLLOW_RANGE, Material.LEAD, """
            The range in blocks within which a mob with this attribute targets players or other mobs to track.
            """, 0, 2048
    ),
    KNOCKBACK_RESISTANCE(
            Attribute.KNOCKBACK_RESISTANCE, Material.NETHERITE_INGOT, """
            The scale of horizontal knockback resisted from attacks and projectiles.
            """, 0, 1
    ),
    MOVEMENT_SPEED(
            Attribute.MOVEMENT_SPEED, Material.SUGAR, """
            Movement speed is the speed at which entities can move.
            """, 0, 1024
    ),
    FLYING_SPEED(
            Attribute.FLYING_SPEED,
            Material.ELYTRA,
            """
                    Flight speed modifier in some unknown metric.
                    &c&o;;Not applicable to players!
                    """, 0, 1024
    ),
    ATTACK_DAMAGE(
            Attribute.ATTACK_DAMAGE, Material.IRON_SWORD, """
            Damage dealt by attacks, in half-hearts.
            """, 0, 2048
    ),
    ATTACK_KNOCKBACK(
            Attribute.ATTACK_KNOCKBACK, Material.SLIME_BALL, """
            Knockback applied to attacks.
            
            Applies only to mobs with physical attacks.
            """, 0, 5
    ),
    ATTACK_SPEED(
            Attribute.ATTACK_SPEED, Material.DIAMOND_HOE, """
            Determines recharging rate of attack strength.
            
            Value is the number of full-strength attacks per second.
            """, 0, 1024
    ),
    ARMOR(
            Attribute.ARMOR, Material.IRON_CHESTPLATE, """
            Armor defense points.
            """, 0, 30
    ),
    ARMOR_TOUGHNESS(
            Attribute.ARMOR_TOUGHNESS, Material.IRON_BLOCK, """
            Armor toughness.
            &8&o;;https://minecraft.wiki/w/Armor#Armor_toughness
            """, 0, 20
    ),
    FALL_DAMAGE_MULTIPLIER(
            Attribute.FALL_DAMAGE_MULTIPLIER, Material.FEATHER, """
            The amount of fall damage an entity takes as a multiplier.
            """, 0, 100
    ),
    LUCK(
            Attribute.LUCK, Material.BIG_DRIPLEAF, """
            Affects the results of loot tables using the quality or bonus_rolls tag (e.g. when opening chests or chest minecarts, fishing, and killing mobs).
            &a&o;;Only applicable to players!
            """, -1024, 1024
    ),
    MAX_ABSORPTION(
            Attribute.MAX_ABSORPTION, Material.GOLDEN_APPLE, """
            The maximum absorption of this mob (in half-hearts); determines the highest health they may gain by the Absorption effect.
            """, 0, 2048
    ),
    SAFE_FALL_DISTANCE(
            Attribute.SAFE_FALL_DISTANCE, Material.CHICKEN, """
            The number of blocks an entity can fall before fall damage starts to be accumulated.
            
            Also the minimum amount of blocks the entity has to fall to make falling particles and sounds.
            """, -1024, 1024
    ),
    SCALE(
            Attribute.SCALE, Material.PUFFERFISH, """
            The multiplier of the size of an entity.
            """, 0.0625, 16
    ),
    STEP_HEIGHT(
            Attribute.STEP_HEIGHT, Material.RABBIT_HIDE, """
            The maximum number of blocks that an entity can step up without jumping.
            """, 0, 10
    ),
    GRAVITY(
            Attribute.GRAVITY, Material.SHULKER_SHELL, """
            The gravity affecting an entity in blocks per tick squared.
            """, -1, 1
    ),
    JUMP_STRENGTH(
            Attribute.JUMP_STRENGTH, Material.RABBIT_FOOT, """
            The initial vertical velocity of an entity when they jump, in blocks per tick.
            """, 0, 32
    ),
    BURNING_TIME(
            Attribute.BURNING_TIME, Material.FIRE_CHARGE, """
            Amount of time how long an entity remains on fire after being ignited as a multiplier.
            
            A value of 0 eliminates the burn time. Has no impact on the burning time increase when staying in fire.
            """, 0, 1024
    ),
    EXPLOSION_KNOCKBACK_RESISTANCE(
            Attribute.EXPLOSION_KNOCKBACK_RESISTANCE, Material.TNT, """
            Defines what percentage of knockback an entity resists.
            
            A value of 1 eliminates the knockback.
            """, 0, 1
    ),
    MOVEMENT_EFFICIENCY(
            Attribute.MOVEMENT_EFFICIENCY, Material.NETHERITE_BOOTS, """
            A factor to improve walking on terrain that slows down movement.
            
            A value of 1 removes the slowing down.
            """, 0, 1
    ),
    OXYGEN_BONUS(
            Attribute.OXYGEN_BONUS, Material.TURTLE_HELMET, """
            Determines the chance that an entity's Air data tag decreases in any given game tick, while the entity is underwater.
            """, 0, 1024
    ),
    WATER_MOVEMENT_EFFICIENCY(
            Attribute.WATER_MOVEMENT_EFFICIENCY, Material.WATER_BUCKET, """
            The movement speed factor when submerged.
            
            A higher value lets entities move faster.
            """, 0, 1
    ),
    TEMPT_RANGE(
            Attribute.TEMPT_RANGE, Material.WHEAT, """
            Determines the range, in blocks, at which temptable mobs can be tempted.
            &e&o;;Only applicable to temptable mobs!
            """, 0, 2048
    ),
    BLOCK_INTERACTION_RANGE(
            Attribute.BLOCK_INTERACTION_RANGE, Material.IRON_PICKAXE, """
            The block interaction range for players in blocks.
            &a&o;;Only applicable to players!
            """, 0, 64
    ),
    ENTITY_INTERACTION_RANGE(
            Attribute.ENTITY_INTERACTION_RANGE, Material.IRON_SWORD, """
            The entity interaction range for players in blocks.
            &a&o;;Only applicable to players!
            """, 0, 64
    ),
    BLOCK_BREAK_SPEED(
            Attribute.BLOCK_BREAK_SPEED, Material.DIAMOND_PICKAXE, """
            The speed the player can break blocks as a multiplier.
            &a&o;;Only applicable to players!
            """, 0, 1024
    ),
    SNEAKING_SPEED(
            Attribute.SNEAKING_SPEED, Material.DIAMOND_LEGGINGS, """
            The movement speed factor when sneaking or crawling.
            
            A factor of 1 means sneaking or crawling is as fast as walking, a factor of 0 means unable to move while sneaking or crawling.
            &a&o;;Only applicable to players!
            """, 0, 1
    ),
    SUBMERGED_MINING_SPEED(
            Attribute.SUBMERGED_MINING_SPEED, Material.POTION, """
            The mining speed factor when underwater.
            
            A factor of 1 means mining as fast as on land, a factor of 0 means unable to mine while submerged.
            &a&o;;Only applicable to players!
            """, 0, 20
    ),
    SWEEPING_DAMAGE_RATIO(
            Attribute.SWEEPING_DAMAGE_RATIO, Material.DIAMOND_SWORD, """
            Determines how much of the base attack damage gets transferred to secondary targets in a sweep attack.
            
            This is in addition to the base attack of the sweep damage itself, a value of 1 means that all of the base attack damage is transferred.
            &a&o;;Only applicable to players!
            """, 0, 1
    ),
    SPAWN_REINFORCEMENTS(
            Attribute.SPAWN_REINFORCEMENTS, Material.ZOMBIE_HEAD, """
            Chance for a zombie to spawn another zombie when attacked.
            &2&o;;Only applicable to zombie entities!
            """, 0, 1
    );

    private final Attribute link;
    private final Material material;
    private final String description;
    private final double[] minmax;

    LinkedAttribute(Attribute link, Material material, String description, double min, double max) {
        this.link = link;
        this.material = material;
        this.description = description;
        this.minmax = new double[] { min, max };
    }

    @Nonnull
    public double[] minmax() {
        return minmax;
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

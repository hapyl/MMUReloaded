package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.mmu3.Main;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import javax.annotation.Nonnull;

public class StandLoadout {

    private final String presetName;
    private final String customName;
    private final boolean isNameVisible;
    private final boolean hasSlots;
    private final boolean hasArms;
    private final boolean isVisible;
    private final boolean hasGravity;
    private final boolean isInvulnerable;
    private final boolean isGlowing;
    private final boolean isSmall;
    private final boolean isMarker;
    private final boolean hasBasePlate;
    private final EulerAngle headPos;
    private final EulerAngle bodyPos;
    private final EulerAngle leftArmPos;
    private final EulerAngle rightArmPos;
    private final EulerAngle leftLegPos;
    private final EulerAngle rightLegPos;
    private final float yaw;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack offHand;

    StandLoadout(StandEditorData data) {
        final ArmorStand stand = data.stand();

        this.presetName = data.getStandName();
        this.customName = stand.getCustomName();
        this.isNameVisible = stand.isCustomNameVisible();
        this.hasSlots = !Main.getStandEditor().isLocked(stand);
        this.hasArms = stand.hasArms();
        this.isVisible = stand.isVisible();
        this.hasGravity = stand.hasGravity();
        this.isInvulnerable = stand.isInvulnerable();
        this.isGlowing = stand.isGlowing();
        this.isSmall = stand.isSmall();
        this.isMarker = stand.isMarker();
        this.hasBasePlate = stand.hasBasePlate();

        final EntityEquipment equipment = stand.getEquipment();

        this.helmet = equipment.getHelmet();
        this.chestplate = equipment.getChestplate();
        this.leggings = equipment.getLeggings();
        this.boots = equipment.getBoots();
        this.mainHand = equipment.getItemInMainHand();
        this.offHand = equipment.getItemInOffHand();

        this.headPos = stand.getHeadPose();
        this.bodyPos = stand.getBodyPose();
        this.leftArmPos = stand.getLeftArmPose();
        this.rightArmPos = stand.getRightArmPose();
        this.leftLegPos = stand.getLeftLegPose();
        this.rightLegPos = stand.getRightLegPose();
        this.yaw = stand.getLocation().getYaw();
    }

    @Nonnull
    public String getName() {
        return presetName;
    }

    public void apply(ArmorStand stand) {
        stand.setCustomName(this.customName);
        stand.setCustomNameVisible(this.isNameVisible);
        Main.getStandEditor().setLock(stand, !this.hasSlots);
        stand.setVisible(this.isVisible);
        stand.setGravity(this.hasGravity);
        stand.setArms(this.hasArms);
        stand.setInvulnerable(this.isInvulnerable);
        stand.setGlowing(this.isGlowing);
        stand.setSmall(this.isSmall);
        stand.setMarker(this.isMarker);
        stand.setBasePlate(this.hasBasePlate);

        final EntityEquipment equipment = stand.getEquipment();

        equipment.setHelmet(this.helmet);
        equipment.setChestplate(this.chestplate);
        equipment.setLeggings(this.leggings);
        equipment.setBoots(this.boots);
        equipment.setItemInMainHand(this.mainHand);
        equipment.setItemInOffHand(this.offHand);

        stand.setHeadPose(this.headPos);
        stand.setBodyPose(this.bodyPos);
        stand.setLeftArmPose(this.leftArmPos);
        stand.setRightArmPose(this.rightArmPos);
        stand.setLeftLegPose(this.leftLegPos);
        stand.setRightLegPose(this.rightLegPos);

        final Location location = stand.getLocation();
        location.setYaw(yaw);

        stand.teleport(location);
    }

}

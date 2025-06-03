package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import me.hapyl.eterna.module.inventory.gui.CancelType;
import me.hapyl.eterna.module.inventory.gui.GUIEventListener;
import me.hapyl.eterna.module.inventory.gui.PlayerGUI;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.EulerAngle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static me.hapyl.mmu3.feature.standeditor.StandEditorConstants.*;

public class StandEditorGUI extends PlayerGUI implements GUIEventListener {

    private static final Map<EquipmentSlot, String> friendlySlotNameMap = Map.of(
            EquipmentSlot.HEAD, "Helmet",
            EquipmentSlot.CHEST, "Chestplate",
            EquipmentSlot.LEGS, "Leggings",
            EquipmentSlot.FEET, "Boots",
            EquipmentSlot.HAND, "Hand",
            EquipmentSlot.OFF_HAND, "Off Hand"
    );

    private final StandEditorData data;

    public StandEditorGUI(Player player, StandEditorData data) {
        super(player, "Editing " + data.getStandName(), 6);
        this.data = data;

        setCancelType(CancelType.GUI);

        openInventory();
    }

    @Override
    public void onClose(@Nonnull InventoryCloseEvent event) {
        if (data.await != null) {
            return;
        }

        Message.success(player, "Finished editing %s.", data.getStandName());

        // Make sure to call stopEditing() after referencing the stand, since it nullates it
        data.stopEditing();
    }

    @Override
    public void onUpdate() {
        final StandEditor editor = Main.getStandEditor();
        final ArmorStand stand = data.stand();

        setItem(
                31, colorItem(ITEM_DISABLED_SLOTS, editor.isLocked(stand)), player -> {
                    editor.setLock(stand, !editor.isLocked(stand));
                    playSoundAndUpdate();
                }
        );

        setItem(
                22, new ItemBuilder(Material.ENDER_PEARL)
                        .setName("Rotate")
                        .addTextBlockLore("""
                                Rotates this armor stand around itself.
                                """)
                        .addLore()
                        .addLore("&7Current Yaw: &f&l%.1f°".formatted(getYaw()))
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to rotate clockwise")
                        .addLore("&8◦ &6Right-Click to rotate counter-clockwise")
                        .addLore("&b&oShift-Click to rotate faster!")
                        .asIcon()
        );

        setItem(20, buildTagsItem());

        setItem(
                39, colorItem(ITEM_SHOW_ARMS, stand.hasArms()), player -> {
                    stand.setArms(!stand.hasArms());
                    playSoundAndUpdate();
                }
        );

        setItem(
                40, colorItem(ITEM_VISIBILITY, stand.isVisible()), player -> {
                    stand.setVisible(!stand.isVisible());
                    playSoundAndUpdate();
                }
        );

        setItem(
                41, colorItem(ITEM_GRAVITY, stand.hasGravity()), player -> {
                    stand.setGravity(!stand.hasGravity());
                    playSoundAndUpdate();
                }
        );

        setItem(
                47, colorItem(ITEM_INVULNERABLE, stand.isInvulnerable()), player -> {
                    stand.setInvulnerable(!stand.isInvulnerable());
                    playSoundAndUpdate();
                }
        );

        setItem(
                48, buildGlowingItem(stand.isGlowing()), player -> {
                    stand.setGlowing(!stand.isGlowing());
                    playSoundAndUpdate();
                }
        );

        // Team modify
        final Team team = getTeam();

        if (team != null) {
            setAction(
                    48, player -> {
                        data.await = StandEditorData.Await.TEAM_MODIFY;
                        new StandEditorModifyTeamColorGUI(player, data, team);

                    }, ClickType.RIGHT
            );
        }

        setItem(
                49, colorItem(ITEM_SMALL, stand.isSmall()), player -> {
                    stand.setSmall(!stand.isSmall());
                    playSoundAndUpdate();
                }
        );

        setItem(
                50, colorItem(ITEM_MARKER, stand.isMarker()), player -> {
                    stand.setMarker(!stand.isMarker());
                    playSoundAndUpdate();
                }
        );

        setItem(
                51, colorItem(ITEM_BASE_PLATE, stand.hasBasePlate()), player -> {
                    stand.setBasePlate(!stand.hasBasePlate());
                    playSoundAndUpdate();
                }
        );

        setItem(
                24, ITEM_MOVE_STAND, player -> {
                    data.enterMoveMode();
                    player.closeInventory();
                }
        );

        setItem(5, makeCustomNameItem());

        setAction(
                5, player -> {
                    data.await = StandEditorData.Await.NAME;
                    player.closeInventory();

                    Message.info(player, "Provide a name for this armor stand, or leave blank to cancel.");
                    Message.info(player, "Color codes (&) are supported!");

                    new SignGUI(player, "Enter Name") {
                        @Override
                        public void onResponse(Response response) {
                            final String string = response.getString(0);

                            if (string.isBlank()) {
                                Message.info(player, "Cancelled adding name.");
                            }
                            else {
                                final String newName = Chat.format((string + " " + response.getString(1)).trim());
                                stand.setCustomName(newName);

                                Message.info(player, "Set %s as new name.", newName);
                            }

                            runSync(() -> {
                                data.await = null;
                                new StandEditorGUI(player, data);
                            });
                        }
                    };
                }, ClickType.LEFT
        );

        setAction(
                5, player -> {
                    stand.setCustomName(null);

                    // Create another instance of the GUI so the name updates
                    new StandEditorGUI(player, data);
                }, ClickType.RIGHT
        );

        setItem(
                6, colorItem(ITEM_TOGGLE_NAME_VISIBILITY, stand.isCustomNameVisible()), player -> {
                    stand.setCustomNameVisible(!stand.isCustomNameVisible());
                    playSoundAndUpdate();
                }
        );

        setItem(0, getArmorItem(EquipmentSlot.HEAD));
        setItem(9, getArmorItem(EquipmentSlot.CHEST));
        setItem(18, getArmorItem(EquipmentSlot.LEGS));
        setItem(27, getArmorItem(EquipmentSlot.FEET));
        setItem(36, getArmorItem(EquipmentSlot.HAND));
        setItem(45, getArmorItem(EquipmentSlot.OFF_HAND));

        setItem(2, buildSaveLoadoutItem());
        setItem(3, buildLoadLoadoutItem());

        setItem(8, buildTuningItem(TunePart.HEAD));
        setItem(17, buildTuningItem(TunePart.BODY));
        setItem(26, buildTuningItem(TunePart.LEFT_ARM));
        setItem(35, buildTuningItem(TunePart.RIGHT_ARM));
        setItem(44, buildTuningItem(TunePart.LEFT_LEG));
        setItem(53, buildTuningItem(TunePart.RIGHT_LEG));
    }

    @Override
    public void onClick(int slot, @Nonnull InventoryClickEvent event) {
        final ClickType click = event.getClick();

        // Outside the menu
        if (slot == -999) {
            return;
        }

        final ArmorStand stand = data.stand();

        // Pose Tuning
        if (slot % 9 == 8 && slot <= getSize()) {
            // Switch axis mode
            if (click == ClickType.MIDDLE) {
                data.nextAxis();

                Message.info(player, "Switched Axis to %s.", Chat.capitalize(data.axis));
                Message.sound(player, Sound.UI_BUTTON_CLICK, 1.75f);
                openInventory();
                return;
            }

            // Reset vector
            if (click == ClickType.DROP || click == ClickType.CONTROL_DROP) {
                final EulerAngle zero = new EulerAngle(0.0d, 0.0d, 0.0d);
                switch (slot) {
                    case 8 -> stand.setHeadPose(zero);
                    case 17 -> stand.setBodyPose(zero);
                    case 26 -> stand.setLeftArmPose(zero);
                    case 35 -> stand.setRightArmPose(zero);
                    case 44 -> stand.setLeftLegPose(zero);
                    case 53 -> stand.setRightLegPose(zero);
                }

                Message.info(player, "Reset vectors.");
                Message.sound(player, Sound.UI_BUTTON_CLICK, 1.75f);
                openInventory();
                return;
            }

            final double[] scaled
                    = data.axis.scaleIfAxis(
                    click == ClickType.LEFT ? 1.0
                            : click == ClickType.SHIFT_LEFT ? 10.0
                            : click == ClickType.RIGHT ? -1.0
                            : click == ClickType.SHIFT_RIGHT ? -10.0
                            : 0.0
            );

            supplyPose(slot, Math.toRadians(scaled[0]), Math.toRadians(scaled[1]), Math.toRadians(scaled[2]));

            Message.sound(player, Sound.UI_BUTTON_CLICK, 1.25f);
            openInventory();
            return;
        }

        // Equipment
        if (slot % 9 == 0 && slot < getSize()) {
            final EntityEquipment equipment = stand.getEquipment();

            // Inside Click
            if (slot <= getSize()) {
                final EquipmentSlot equipmentSlot = bySlot(slot);
                final Inventory inventory = getInventory();
                final ItemStack itemOnSlot = inventory.getItem(slot);
                final ItemStack cursor = event.getCursor();

                // Take item from armor stand
                if (itemOnSlot != null && itemOnSlot.getType() != Material.GRAY_DYE) {
                    final PlayerInventory playerInventory = player.getInventory();
                    if (playerInventory.firstEmpty() == -1) {
                        Message.error(player, "You don't have enough space in your inventory!");
                        Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                        return;
                    }

                    final ItemStack currentItem = equipment.getItem(equipmentSlot);
                    playerInventory.addItem(currentItem);
                    setStandItem(equipmentSlot, new ItemStack(Material.AIR));

                    Message.sound(player, Sound.ENTITY_CHICKEN_EGG, 1.0f);
                    openInventory();
                    return;
                }

                // Put Item
                if (cursor.getType() != Material.AIR) {
                    setStandItem(equipmentSlot, cursor);
                    openInventory();
                }
            }
        }

        // Load outs
        if (click == ClickType.NUMBER_KEY) {
            final int hotbarButton = event.getHotbarButton();

            // Save
            if (slot == 2) {
                data.saveLoadout(hotbarButton);

                Message.success(player, "Successfully saved loadout to slot %s.", hotbarButton + 1);
                playSoundAndUpdate();
            }
            // Load
            else if (slot == 3) {
                final StandLoadout loadout = data.getLoadout(hotbarButton);

                if (loadout == null) {
                    Message.error(player, "There is nothing in this slot!");
                    Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                    return;
                }

                loadout.apply(data.stand());
                Message.success(player, "Successfully loaded loadout from slot %s.", hotbarButton + 1);

                playSoundAndUpdate();
            }
        }

        // Rotate
        if (slot == 22) {
            final boolean clockwise = click.isLeftClick();
            final double speed = click.isShiftClick() ? 10 : 1;

            final Location location = stand.getLocation();
            final float yaw = location.getYaw();

            location.setYaw((float) (clockwise ? yaw + speed : yaw - speed));

            stand.teleport(location);

            Message.success(player, "Rotated %s degrees %s.", speed, clockwise ? "clockwise" : "counter-clockwise");
            playSoundAndUpdate();
        }
    }

    private ItemStack makeCustomNameItem() {
        final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN)
                .setName("Custom Name")
                .addTextBlockLore("""
                        Changes the custom name of this armor stand.
                        """)
                .addLore()
                .addLore("Current Name: &f" + data.getStandName())
                .addLore();

        // Clarify what the numbers in parentheses mean
        if (data.stand().getCustomName() == null) {
            builder.addTextBlockLore("""
                            &8&o;;Numbers inside parentheses are displayed for unnamed armor stands to differentiate them.
                            """)
                    .addLore();
        }

        builder.addLore("&8◦ &eLeft-Click to rename").addLore("&8◦ &6Right-Click to clear name");

        return builder.asIcon();
    }

    private float getYaw() {
        final ArmorStand stand = data.stand();
        final float yaw = stand.getLocation().getYaw();

        if (yaw >= 360) {
            return yaw % 360;
        }

        return yaw;
    }

    private ItemStack buildTuningItem(TunePart part) {
        return new ItemBuilder(part.getMaterial())
                .setName(part.toString())
                .addSmartLore("Edits the %s position of this armor stand.".formatted(part.getName()))
                .addLore()
                .addLore("Current Values: %s".formatted(getCurrentTuningValues(part)))
                .addLore()
                .addLore("&8◦ &eLeft-Click to increment")
                .addLore("&8◦ &6Right-Click to decrement")
                .addLore("&b&oShift-Click to adjust in larger steps!")
                .addLore()
                .addLore("&8◦ &dMiddle-Click to switch Axis")
                .addLore("&8◦ &cDrop to reset vectors")
                .hideFlags()
                .toItemStack();
    }

    private String getCurrentTuningValues(TunePart part) {
        final ArmorStand stand = data.stand();

        return switch (part) {
            case HEAD -> toDegrees(stand.getHeadPose());
            case BODY -> toDegrees(stand.getBodyPose());
            case LEFT_ARM -> toDegrees(stand.getLeftArmPose());
            case RIGHT_ARM -> toDegrees(stand.getRightArmPose());
            case LEFT_LEG -> toDegrees(stand.getLeftLegPose());
            case RIGHT_LEG -> toDegrees(stand.getRightLegPose());
        };
    }

    private String toDegrees(EulerAngle angle) {
        return "%s%.1f&7°  %s%.1f&7°  %s%.1f&7°".formatted(
                (data.axis == TuneAxis.X ? "&c&l&n" : "&c&l"), Math.toDegrees(angle.getX()),
                (data.axis == TuneAxis.Y ? "&a&l&n" : "&a&l"), Math.toDegrees(angle.getY()),
                (data.axis == TuneAxis.Z ? "&9&l&n" : "&9&l"), Math.toDegrees(angle.getZ())
        );
    }

    private ItemStack buildLoadoutItem(Material material, String name, String lore) {
        final StandEditor editor = Main.getStandEditor();
        final StandEditorData data = editor.getData(player);
        final ItemBuilder builder = new ItemBuilder(material)
                .setName("" + name)
                .addTextBlockLore(lore);

        builder.addLore("");
        builder.addLore("&7Your Presets:");

        for (int i = 0; i < 9; ++i) {
            final StandLoadout loadout = data.getLoadout(i);

            builder.addLore(Chat.format("&8%s. &a%s".formatted(i + 1, loadout != null ? loadout.getName() : "&8None!")));
        }

        builder.addLore("");
        builder.addSmartLore("Keep in mind that loadouts aren't persistent on restarts!", "&c&o");
        return builder.toItemStack();
    }

    private ItemStack buildSaveLoadoutItem() {
        return buildLoadoutItem(
                Material.WRITABLE_BOOK,
                "Save Preset",
                """
                        Saves this armor stand as a preset.
                        
                        &e&o;;To save a preset, press a corresponding button on your keyboard (1-9).
                        """
        );
    }

    private ItemStack buildLoadLoadoutItem() {
        return buildLoadoutItem(
                Material.WRITTEN_BOOK,
                "Load Preset",
                """
                        Loads a preset to this armor stand.
                        
                        &e&o;;To load a preset, press a corresponding button on your keyboard (1-9).
                        """
        );
    }

    private void setStandItem(EquipmentSlot slot, ItemStack item) {
        final EntityEquipment equipment = data.stand().getEquipment();

        switch (slot) {
            case HEAD -> equipment.setHelmet(item);
            case CHEST -> equipment.setChestplate(item);
            case LEGS -> equipment.setLeggings(item);
            case FEET -> equipment.setBoots(item);
            case HAND -> equipment.setItemInMainHand(item);
            case OFF_HAND -> equipment.setItemInOffHand(item);
        }

        item.setAmount(0);
        Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
    }

    private EquipmentSlot bySlot(int slot) {
        return switch (slot) {
            case 9 -> EquipmentSlot.CHEST;
            case 18 -> EquipmentSlot.LEGS;
            case 27 -> EquipmentSlot.FEET;
            case 36 -> EquipmentSlot.HAND;
            case 45 -> EquipmentSlot.OFF_HAND;
            default -> EquipmentSlot.HEAD;
        };
    }

    private void supplyPose(int slot, double x, double y, double z) {
        final ArmorStand stand = data.stand();

        switch (slot) {
            case 8 -> stand.setHeadPose(stand.getHeadPose().add(x, y, z));
            case 17 -> stand.setBodyPose(stand.getBodyPose().add(x, y, z));
            case 26 -> stand.setLeftArmPose(stand.getLeftArmPose().add(x, y, z));
            case 35 -> stand.setRightArmPose(stand.getRightArmPose().add(x, y, z));
            case 44 -> stand.setLeftLegPose(stand.getLeftLegPose().add(x, y, z));
            case 53 -> stand.setRightLegPose(stand.getRightLegPose().add(x, y, z));
        }
    }

    private ItemStack getArmorItem(EquipmentSlot slot) {
        final EntityEquipment equipment = data.stand().getEquipment();

        switch (slot) {
            case HEAD -> {
                if (isItem(equipment.getHelmet())) {
                    return getArmorItemPreview(equipment.getHelmet());
                }
            }
            case CHEST -> {
                if (isItem(equipment.getChestplate())) {
                    return getArmorItemPreview(equipment.getChestplate());
                }
            }
            case LEGS -> {
                if (isItem(equipment.getLeggings())) {
                    return getArmorItemPreview(equipment.getLeggings());
                }
            }
            case FEET -> {
                if (isItem(equipment.getBoots())) {
                    return getArmorItemPreview(equipment.getBoots());
                }
            }
            case HAND -> {
                if (isItem(equipment.getItemInMainHand())) {
                    return getArmorItemPreview(equipment.getItemInMainHand());
                }
            }
            case OFF_HAND -> {
                if (isItem(equipment.getItemInOffHand())) {
                    return getArmorItemPreview(equipment.getItemInOffHand());
                }
            }
        }

        return getNoItem(slot);
    }

    private ItemStack getNoItem(EquipmentSlot slot) {
        final String slotName = Objects.requireNonNull(friendlySlotNameMap.get(slot), "Unsupported slot: " + slot);
        final ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                .setName("" + slotName)
                .addTextBlockLore("""
                        Place an item from your inventory here to equip &6%s&7.
                        """.formatted(slotName));

        // Notify that some slots may not render the item!
        switch (slot) {
            case CHEST, LEGS, FEET -> {
                builder.addTextBlockLore("""
                        
                        &8&lNote!
                        &7&o;;Only %s will properly render in this slot, other items will appear invisible.
                        """.formatted(slotName));
            }
        }

        return builder.asIcon();
    }

    private boolean isItem(ItemStack stack) {
        return stack != null && !stack.getType().isAir();
    }

    private ItemStack getArmorItemPreview(ItemStack stack) {
        return new ItemBuilder(stack)
                .addLore("&8&m ".repeat(42))
                .addTextBlockLore("""
                        &8&o;;This is a preview of the item!
                        
                        &8◦ &eLeft-Click to remove the item
                        """)
                .asIcon();
    }

    private ItemStack buildGlowingItem(boolean flag) {
        final ItemBuilder builder = new ItemBuilder(Material.SPECTRAL_ARROW)
                .setName("Glowing " + BukkitUtils.checkmark(flag))
                .addTextBlockLore("""
                        Toggles whether this armor stand has glowing effect.
                        
                        &8&o;;Glowing color is linked to the armor stand's scoreboard team, you'll have to manually add it to the team in order to change the color!
                        """)
                .addLore();

        @Nullable final Team team = getTeam();

        if (team != null) {
            final ChatColor teamColor = team.getColor();

            builder.addLore("Current Team: %s &8(%s&8)".formatted(teamColor + team.getName(), teamColor + Chat.capitalize(teamColor)));
        }
        else {
            builder.addLore("Current Team: &8No team!");
        }

        // Have to manually handle toggles because of change team color sub-gui
        builder.addLore().addLore("&8◦ &eLeft-Click to %s".formatted(flag ? "disable" : "enable"));

        // If team exists, add a sub-gui button to easily change the team color
        if (team != null) {
            builder.addLore("&8◦ &6Right-Click to modify team color");
        }

        return builder.asIcon();
    }

    @Nullable
    private Team getTeam() {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();

        return manager.getMainScoreboard().getEntryTeam(data.stand().getUniqueId().toString());
    }

    private ItemStack buildTagsItem() {
        final ItemBuilder builder = new ItemBuilder(Material.NAME_TAG)
                .setName("Scoreboard Tags")
                .addSmartLore("Displays a list of scoreboard tags.")
                .addLore();

        final Set<String> tags = data.stand().getScoreboardTags();

        if (tags.isEmpty()) {
            builder.addLore("&8There are no tags!");
        }
        else {
            for (String tag : tags) {
                builder.addLore("&8- &e%s".formatted(tag));
            }
        }

        return builder.toItemStack();
    }

    private void playSoundAndUpdate() {
        Message.sound(player, Sound.UI_BUTTON_CLICK, 2.0f);
        openInventory();
    }

    private ItemStack colorItem(ItemStack stack, boolean condition) {
        final ItemMeta meta = stack.getItemMeta();
        final ItemBuilder builder = new ItemBuilder(stack).predicate(condition, ItemBuilder::glow);

        if (meta != null) {
            final String name = meta.getDisplayName();

            builder.setName(
                    "%s %s".formatted("" + ChatColor.stripColor(name), BukkitUtils.checkmark(condition))
            );
        }

        builder.addLore().addLore("&8◦ &eLeft-Click to %s".formatted(condition ? "disable" : "enable"));

        return builder.toItemStack();
    }
}

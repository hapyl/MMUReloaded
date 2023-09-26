package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.trim.Editor;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import me.hapyl.spigotutils.module.inventory.gui.CancelType;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.EulerAngle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Set;

import static me.hapyl.mmu3.feature.standeditor.StandEditorConstants.*;

public class StandEditorGUI extends PlayerGUI implements Editor {

    private final Data data;

    public StandEditorGUI(Player player, Data data) {
        super(player, "Editing " + data.getStandName(), 6);
        this.data = data;
        data.setTaken(true);
        setCancelType(CancelType.GUI);
        setCloseEvent(p -> {
            if (!data.isTaken()) {
                return;
            }
            data.setTaken(false);
            Message.success(p, "Finished editing %s.", data.getStandName());
        });
        updateInventory();
        openInventory();
    }

    @Override
    public void showUsage(@Nonnull Player player) {
        Message.info(player, "&a&lYou've entered the Trim Editor!");
        Message.info(player, "&f&lMOVE AROUND&7 to move the armor stand.");
        Message.info(player, "&f&lJUMP&7 to move the armor stand up.");
        Message.info(player, "&f&lSNEAK&7 to move the armor stand down.");
        Message.info(player, "&f&lSWAP HANDS&7 &8(F)&7 to cycle moving speed.");
        Message.info(player, "&f&lLEFT CLICK&7 to leave the editor.");

        Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.75f);
    }

    private float getYaw() {
        final ArmorStand stand = data.getStand();
        final float yaw = stand.getLocation().getYaw();

        if (yaw >= 360) {
            return yaw % 360;
        }

        return yaw;
    }

    private void updateInventory() {
        final StandEditor editor = Main.getStandEditor();
        final ArmorStand stand = data.getStand();

        setItem(31, colorItem(ITEM_DISABLED_SLOTS, editor.isLocked(stand)), player -> {
            editor.setLock(stand, !editor.isLocked(stand));
            playSoundAndUpdate();
        });

        setItem(22, new ItemBuilder(Material.ENDER_PEARL)
                .setName("Rotate")
                .addSmartLore("Rotate armor stands around its axis.")
                .addLore()
                .addLore("&aCurrent Yaw: &f&l%.2f", getYaw())
                .addLore()
                .addLore("&eClick to rotate clockwise.")
                .addLore("&6Right Click to rotate counter-clockwise.")
                .addLore("&8Sneak to rotate faster!")
                .asIcon());

        setItem(20, buildTagsItem());

        setItem(39, colorItem(ITEM_SHOW_ARMS, stand.hasArms()), player -> {
            stand.setArms(!stand.hasArms());
            playSoundAndUpdate();
        });

        setItem(40, colorItem(ITEM_VISIBILITY, stand.isVisible()), player -> {
            stand.setVisible(!stand.isVisible());
            playSoundAndUpdate();
        });

        setItem(41, colorItem(ITEM_GRAVITY, stand.hasGravity()), player -> {
            stand.setGravity(!stand.hasGravity());
            playSoundAndUpdate();
        });

        setItem(47, colorItem(ITEM_INVULNERABLE, stand.isInvulnerable()), player -> {
            stand.setInvulnerable(!stand.isInvulnerable());
            playSoundAndUpdate();
        });

        setItem(48, buildGlowingItem(stand.isGlowing()), player -> {
            stand.setGlowing(!stand.isGlowing());
            playSoundAndUpdate();
        });

        setItem(49, colorItem(ITEM_SMALL, stand.isSmall()), player -> {
            stand.setSmall(!stand.isSmall());
            playSoundAndUpdate();
        });

        setItem(50, colorItem(ITEM_MARKER, stand.isMarker()), player -> {
            stand.setMarker(!stand.isMarker());
            playSoundAndUpdate();
        });

        setItem(51, colorItem(ITEM_BASE_PLATE, stand.hasBasePlate()), player -> {
            stand.setBasePlate(!stand.hasBasePlate());
            playSoundAndUpdate();
        });

        setItem(24, ITEM_MOVE_STAND, player -> {
            data.setWaitForMove(true);
            setCloseEvent(null);
            player.closeInventory();

            showUsage(player);
        });

        setItem(
                5,
                new ItemBuilder(Material.OAK_SIGN)
                        .setName("&aCustom Name")
                        .setSmartLore("Add or change a custom name of this armor stand.")
                        .addLore()
                        .addLore("Current Name: " + getStandName())
                        .addLore()
                        .addLore("&eLeft Click to change name.")
                        .addLore("&eRight Click to remove name.")
                        .build()
        );

        setClick(5, player -> {
            setCloseEvent(null);
            player.closeInventory();

            Message.info(player, "Provide a name for this armor stand, or &a'none' &7to cancel.");
            Message.info(player, "Color codes (&) are supported!");

            new SignGUI(player, "Enter Name") {
                @Override
                public void onResponse(Response response) {
                    final String string = response.getString(0);

                    if (string.contains("none") || string.isBlank()) {
                        Message.info(player, "Cancelled adding name.");
                    }
                    else {
                        final String newName = Chat.format((string + " " + response.getString(1)).trim());
                        stand.setCustomName(newName);
                        Message.info(player, "Set %s as new name.", newName);
                    }

                    runSync(() -> new StandEditorGUI(player, data));
                }
            };
        }, ClickType.LEFT);

        addClick(5, player -> {
            stand.setCustomName(null);
            playSoundAndUpdate();
        }, ClickType.RIGHT);

        setItem(6, colorItem(ITEM_TOGGLE_NAME_VISIBILITY, stand.isCustomNameVisible()), player -> {
            stand.setCustomNameVisible(!stand.isCustomNameVisible());
            playSoundAndUpdate();
        });

        setItem(0, getArmorItem(EquipmentSlot.HEAD));
        setItem(9, getArmorItem(EquipmentSlot.CHEST));
        setItem(18, getArmorItem(EquipmentSlot.LEGS));
        setItem(27, getArmorItem(EquipmentSlot.FEET));
        setItem(36, getArmorItem(EquipmentSlot.HAND));
        setItem(45, getArmorItem(EquipmentSlot.OFF_HAND));

        setItem(2, buildSaveLoadoutItem());
        setItem(3, buildLoadLoadoutItem());

        setItem(8, buildTuningItem(TuneData.Part.HEAD));
        setItem(17, buildTuningItem(TuneData.Part.BODY));
        setItem(26, buildTuningItem(TuneData.Part.LEFT_ARM));
        setItem(35, buildTuningItem(TuneData.Part.RIGHT_ARM));
        setItem(44, buildTuningItem(TuneData.Part.LEFT_LEG));
        setItem(53, buildTuningItem(TuneData.Part.RIGHT_LEG));

        setEventListener(((player, thiz, ev) -> {
            final int slot = ev.getRawSlot();
            final ClickType click = ev.getClick();
            if (slot == -999) {
                return;
            }

            // Pose Tuning
            if (slot % 9 == 8 && slot <= getSize()) {
                final TuneData.Axis axis = data.getAxis();

                // Switch axis mode
                if (click == ClickType.MIDDLE) {
                    data.nextAxis();
                    Message.info(player, "Switched Axis to %s.", Chat.capitalize(data.getAxis()));
                    Message.sound(player, Sound.UI_BUTTON_CLICK, 1.75f);
                    updateInventory();
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
                    updateInventory();
                    return;
                }

                final double[] scaled = axis.scaleIfAxis(click == ClickType.LEFT ? 1.0d : click == ClickType.SHIFT_LEFT ? 10.0d :
                        click == ClickType.RIGHT ? -1.0d : click == ClickType.SHIFT_RIGHT ? -10.0d : 0.0d);

                supplyPose(slot, Math.toRadians(scaled[0]), Math.toRadians(scaled[1]), Math.toRadians(scaled[2]));

                Message.sound(player, Sound.UI_BUTTON_CLICK, 1.25f);
                updateInventory();
                return;
            }

            // Equipment
            if (slot % 9 == 0 && slot < getSize()) {
                final EntityEquipment equipment = stand.getEquipment();
                if (equipment == null) {
                    return;
                }

                // Inside Click
                if (slot <= getSize()) {
                    final EquipmentSlot equipmentSlot = bySlot(slot);
                    final Inventory inventory = getInventory();
                    final ItemStack itemOnSlot = inventory.getItem(slot);
                    final ItemStack cursor = ev.getCursor();

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
                        updateInventory();
                        return;
                    }

                    // Put Item
                    if (cursor != null && cursor.getType() != Material.AIR) {
                        setStandItem(equipmentSlot, cursor);
                        updateInventory();
                    }
                }
            }

            // Load outs
            if (click == ClickType.NUMBER_KEY) {
                final int clickedSlot = ev.getHotbarButton() + 1;

                // Save
                if (slot == 2) {
                    editor.saveLoadout(clickedSlot, data);
                    Message.success(player, "Successfully saved loadout to slot %s.", clickedSlot);
                    playSoundAndUpdate();
                }
                // Load
                else if (slot == 3) {
                    final StandInfo loadout = editor.getLoadout(player, clickedSlot);
                    if (loadout == null) {
                        Message.error(player, "There is nothing in this slot!");
                        Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                        return;
                    }

                    loadout.applyToStand(data.getStand());
                    Message.success(player, "Successfully loaded loadout from slot %s.", clickedSlot);
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
        }));
    }

    private ItemStack buildTuningItem(TuneData.Part part) {
        return new ItemBuilder(part.getMaterial())
                .setName("&a" + part.getName())
                .addLore()
                .addLore("You are currently editing in &b&l%s &7vector.", data.getAxis())
                .addLore()
                .addLore("Current Values: &b&l%s", getCurrentTuningValues(part))
                .addLore()
                .addLore("&6[Middle] &eto switch vector")
                .addLore("&6[Drop] &eto reset vectors")
                .addLore()
                .addLore("&6[Left] &e+1&6, [Shift Left] &e+10")
                .addLore("&6[Right] &e-1&6, [Shift Right] &e-10")
                .hideFlags()
                .toItemStack();
    }

    private String getCurrentTuningValues(TuneData.Part part) {
        final ArmorStand stand = data.getStand();
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
        final DecimalFormat format = new DecimalFormat("##.##");
        final TuneData.Axis axis = data.getAxis();
        final String x = format.format(Math.toDegrees(angle.getX()));
        final String y = format.format(Math.toDegrees(angle.getY()));
        final String z = format.format(Math.toDegrees(angle.getZ()));

        return String.format(
                "%s&b, &b&l%s&b, &b&l%s",
                (axis == TuneData.Axis.X ? "&n" : "") + x,
                (axis == TuneData.Axis.Y ? "&n" : "") + y,
                (axis == TuneData.Axis.Z ? "&n" : "") + z
        );
    }

    private ItemStack buildLoadoutItem(Material material, String name, String lore) {
        final StandEditor editor = Main.getStandEditor();
        final ItemBuilder builder = new ItemBuilder(material).setName("&a" + name).addSmartLore(lore);
        builder.addLore("");
        builder.addLore("&7Your Loadouts:");

        for (int i = 0; i < 9; ++i) {
            builder.addLore(Chat.format("&8%s. %s", i + 1, editor.getLoadoutName(getPlayer(), i + 1)));
        }

        builder.addLore("");
        builder.addSmartLore("Keep in mind that loadouts aren't persistent on reloads!", "&c&o");
        return builder.toItemStack();
    }

    private ItemStack buildSaveLoadoutItem() {
        return buildLoadoutItem(
                Material.WRITABLE_BOOK,
                "Save Loadout",
                "To save this armor stand into loadout, press corresponding inventory slot."
        );
    }

    private ItemStack buildLoadLoadoutItem() {
        return buildLoadoutItem(
                Material.WRITTEN_BOOK,
                "Load Loadout",
                "To load a loadout to ths armor stand, press corresponding inventory slot."
        );
    }

    private void setStandItem(EquipmentSlot slot, ItemStack item) {
        final EntityEquipment equipment = data.getStand().getEquipment();
        if (equipment == null) {
            return;
        }
        switch (slot) {
            case HEAD -> equipment.setHelmet(item);
            case CHEST -> equipment.setChestplate(item);
            case LEGS -> equipment.setLeggings(item);
            case FEET -> equipment.setBoots(item);
            case HAND -> equipment.setItemInMainHand(item);
            case OFF_HAND -> equipment.setItemInOffHand(item);
        }

        item.setAmount(0);
        Message.sound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
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
        final ArmorStand stand = data.getStand();
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
        final EntityEquipment equipment = data.getStand().getEquipment();
        if (equipment == null) {
            return new ItemBuilder(Material.BARRIER).setName("&cError Loading Equipment").build();
        }

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
        final String text = Chat.capitalize(slot);
        return new ItemBuilder(Material.GRAY_DYE)
                .setName("&a" + text)
                .addLore("&8There is no item currently!")
                .addLore()
                .addSmartLore("Put an item from your inventory to this slot to set %s.".formatted(text))
                .build();
    }

    private boolean isItem(ItemStack stack) {
        return stack != null && !stack.getType().isAir();
    }

    private ItemStack getArmorItemPreview(ItemStack stack) {
        return new ItemBuilder(stack)
                .addLore()
                .addLore("&8&m----------------------------")
                .addSmartLore("This a preview of the item, click to remove it.", "&8")
                .toItemStack();
    }


    private String getStandName() {
        final String name = data.getStand().getCustomName();
        return name == null ? "&8None" : name;
    }

    private ItemStack buildGlowingItem(boolean flag) {
        final ItemBuilder builder = new ItemBuilder(Material.SPECTRAL_ARROW)
                .setName("Glowing")
                .addSmartLore("Toggles whenever armor stand has glowing effect.")
                .addLore()
                .addSmartLore("To change glowing color, you must create a team with that specific color and add armor stand to it.");

        final Team team = getTeam();
        builder.addLore("");

        if (team != null) {
            builder.addLore("Current Team: " + team.getColor() + team.getName());
            builder.addLore();
            builder.addLore("&e/team modify %s color <color>", team.getName());
            builder.addLore("To change team's color.");
        }
        else {
            builder.addLore("Current Team: &8No team.");
        }

        return colorItem(builder.build(), flag);
    }

    @Nullable
    private Team getTeam() {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) {
            return null;
        }

        return manager.getMainScoreboard().getEntryTeam(data.getStand().getUniqueId().toString());
    }

    private ItemStack buildTagsItem() {
        final ArmorStand stand = data.getStand();
        final ItemBuilder builder = new ItemBuilder(Material.NAME_TAG).setName("&aScoreboard Tags");
        final Set<String> tags = stand.getScoreboardTags();

        if (tags.isEmpty()) {
            builder.addLore("&8There are no tags.");
        }
        else {
            for (String tag : tags) {
                builder.addLore("&7- &e%s", tag);
            }
        }

        return builder.toItemStack();
    }

    private void playSoundAndUpdate() {
        Message.sound(getPlayer(), Sound.UI_BUTTON_CLICK, 2.0f);
        updateInventory();
    }

    private ItemStack colorItem(ItemStack stack, boolean condition) {
        final ItemMeta meta = stack.getItemMeta();
        final ItemBuilder builder = new ItemBuilder(stack).predicate(condition, ItemBuilder::glow);

        if (meta != null) {
            final String name = meta.getDisplayName();
            builder.setName((condition ? "&a" : "&c") + ChatColor.stripColor(name));
        }

        builder.addLore().addLore("&eClick to %s", condition ? "disable" : "enable");
        return builder.toItemStack();
    }
}

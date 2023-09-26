package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ColorSignGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.HexId;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.Enums;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

/**
 * Movement:
 * W -> Previous armor slot (up)
 * S -> Next armor slot (down)
 * A -> Previous trim material/pattern
 * D -> Next trim material/pattern
 * JUMP -> Switch between material/pattern
 */
public class TrimEditor implements Editor {

    private final Player player;
    private final Location location;
    private final TrimData[] trimData;

    private TrimMode trimMode;
    private int slot;
    private boolean lock;

    public TrimEditor(Player player) {
        this.player = player;
        this.location = player.getLocation();
        this.trimData = new TrimData[4];
        this.trimMode = TrimMode.PATTERN;
        this.slot = 1;

        // Prepare location
        location.add(0.0d, 0.1d, 0.0d);
        location.setYaw(0.0f);
        location.setPitch(0.0f);

        // Prepare player
        player.teleport(location);
        player.setAllowFlight(true);
        player.setFlying(true);

        // Prepare stands
        createArmorStands();

        showUsage(player);
    }

    public boolean inOnCooldown() {
        return player.hasCooldown(Material.ARMOR_STAND);
    }

    public void startCooldown() {
        player.setCooldown(Material.ARMOR_STAND, 5);
    }

    public void nextEntry() {
        if (checkLockAndNotify()) {
            return;
        }

        final TrimData data = getData();

        trimMode.next(data);
        update();

        sendInfo("Current %s ➤ %s.", trimMode.name(), trimMode.current(data));
        sendSound(Sound.ITEM_ARMOR_EQUIP_LEATHER);
    }

    public void previousEntry() {
        if (checkLockAndNotify()) {
            return;
        }

        final TrimData data = getData();

        trimMode.previous(data);
        update();

        sendInfo("Current %s ➤ %s.", trimMode.name(), trimMode.current(data));
        sendSound(Sound.ITEM_ARMOR_EQUIP_LEATHER);
    }

    public void switchMode() {
        trimMode = trimMode == TrimMode.MATERIAL ? TrimMode.PATTERN : TrimMode.MATERIAL;

        sendInfo("Switched to %s mode.", trimMode.name());
        sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
    }

    @Nonnull
    public ItemStack getCurrentItem() {
        final TrimData data = getData();
        final ItemStack item = data.getItem();

        return item == null ? new ItemStack(Material.AIR) : item;
    }

    public void setCurrentItem(@Nonnull ItemStack item) {
        if (checkLockAndNotify()) {
            return;
        }

        final TrimData data = getData();
        data.setItem(item);

        update();

        // Fx
        sendInfo("Swapped item to %s", Chat.capitalize(item.getType()));
        sendSound(Sound.ITEM_ARMOR_EQUIP_CHAIN);
    }

    public void update() {
        for (int i = 0; i < trimData.length; i++) {
            final TrimData data = trimData[i];
            data.setCurrent(slot == i);
            data.update();

            final ArmorStand stand = data.getStand();
            stand.setGlowing(false);

            if (data.isCurrent()) {
                stand.setGlowing(true);
            }
        }
    }

    public void nextSlot() {
        if (checkLockAndNotify()) {
            return;
        }

        slot = slot + 1 >= trimData.length ? 0 : slot + 1;
        update();

        // Fx
        sendSound(Sound.UI_BUTTON_CLICK, 1.0f);
    }

    public void previousSlot() {
        if (checkLockAndNotify()) {
            return;
        }

        slot = slot - 1 < 0 ? trimData.length - 1 : slot - 1;
        update();

        // Fx
        sendSound(Sound.UI_BUTTON_CLICK, 0.75f);
    }

    @Nonnull
    public final CachedTrimData remove() {
        final ItemStack[] itemStack = new ItemStack[4];

        for (int i = 0; i < trimData.length; i++) {
            final TrimData data = trimData[i];
            final ArmorStand stand = data.getStand();

            itemStack[i] = data.getItem();
            stand.remove();
        }

        final TrimManager trimManager = Main.getRegistry().trimManager;
        final CachedTrimData cachedTrimData = new CachedTrimData(itemStack);
        final HexId hexId = cachedTrimData.getHexId();

        Message.success(player, "Finished trim editing!");
        Message.success(player, "Use &e/trim " + hexId + " &ato get your items!");
        Message.clickHover(
                player,
                LazyEvent.runCommand("/trim " + hexId),
                LazyEvent.showText("&7Click to get items for %s!", hexId),
                "&6&lOR CLICK HERE!"
        );
        trimManager.setTrim(cachedTrimData);

        return cachedTrimData;
    }

    public void randomize() {
        if (checkLockAndNotify()) {
            return;
        }

        final int shuffleTime = ThreadRandom.nextInt(6, 12);
        final TrimData data = getData();

        lock = true;

        for (int i = 0; i < shuffleTime; i++) {
            final int finalI = i;
            final boolean isLast = i == shuffleTime - 1;

            new BukkitRunnable() {
                @Override
                public void run() {
                    final EnumTrimPattern randomPattern = Enums.getRandomValue(EnumTrimPattern.class);
                    final EnumTrimMaterial randomMaterial = Enums.getRandomValue(EnumTrimMaterial.class);

                    data.setPattern(randomPattern);
                    data.setMaterial(randomMaterial);

                    update();

                    // Fx
                    final float pitch = 0.5f + (1.0f / shuffleTime * finalI);

                    sendSound(Sound.BLOCK_NOTE_BLOCK_BASEDRUM, pitch);
                    sendSound(Sound.BLOCK_NOTE_BLOCK_BASS, pitch);

                    if (isLast) {
                        lock = false;
                        sendInfo("Randomized~!");
                        sendSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f);
                        sendSound(Sound.ENTITY_VILLAGER_YES, 0.75f);
                    }
                }
            }.runTaskLater(Main.getPlugin(), 2L * (i + 1));
        }
    }

    public void tryColor() {
        if (checkLockAndNotify()) {
            return;
        }

        final TrimData data = getData();

        if (!data.isLeatherArmor()) {
            sendInfo(ChatColor.DARK_RED + "Cannot dye this armor!");
            sendSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        new ColorSignGUI(player) {
            @Override
            public void onResponse(Color color) {
                runSync(() -> {
                    data.setColor(color);
                    update();

                    final String stringColor = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

                    sendInfo("Applied %s color!", stringColor);
                    sendSound(Sound.ENTITY_VILLAGER_YES);
                });
            }
        };
    }

    @Override
    public void showUsage(@Nonnull Player player) {
        Message.info(player, "&a&lYou've entered the Trim Editor!");
        Message.info(player, "&f&lW &8& &f&lS &7to cycle between armor slots.");
        Message.info(player, "&f&lA &8& &f&lD &7to cycle between pattern/material.");
        Message.info(player, "&f&lSPACE&7 to switch between pattern/material.");
        Message.info(player, "&f&lRIGHT CLICK&7 with an item to replace editing item.");
        Message.info(player, "&f&lDROP&7 &8(Q)&7 to randomize the trim.");
        Message.info(player, "&f&lSWAP HANDS&7 &8(F)&&7 to color armor.");
        Message.info(player, "&f&lSNEAK&7 to leave the editor.");
    }

    private boolean checkLockAndNotify() {
        if (lock) {
            sendInfo(ChatColor.DARK_RED + "Cannot modify yet!");
            return true;
        }

        return false;
    }

    private void sendInfo(Object info, Object... format) {
        Chat.sendActionbar(
                player,
                ChatColor.GREEN + String.valueOf(info),
                Message.colorReplacements("&a", "&f&l", format)
        );
    }

    private void sendSound(Sound sound) {
        sendSound(sound, 1.0f);
    }

    private void sendSound(Sound sound, float pitch) {
        PlayerLib.playSound(player, sound, pitch);
    }

    @Nonnull
    private TrimData getData() {
        return trimData[slot];
    }

    private void createArmorStands() {
        location.add(location.getDirection().normalize().setY(0.0d).multiply(2.5d));
        location.setYaw(-180.0f);

        for (int i = 0; i < trimData.length; i++) {
            final TrimData data = new TrimData(TrimType.values()[i], Entities.ARMOR_STAND_MARKER.spawn(
                    location,
                    self -> {
                        self.setVisible(false);
                        self.setSilent(true);
                    }
            ));

            trimData[i] = data;
        }

        update();
    }
}

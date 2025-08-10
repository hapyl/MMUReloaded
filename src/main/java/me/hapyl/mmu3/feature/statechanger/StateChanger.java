package me.hapyl.mmu3.feature.statechanger;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.Gradient;
import me.hapyl.eterna.module.chat.gradient.Interpolators;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.feature.statechanger.adapter.*;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class StateChanger extends Feature {

    public static final Key ITEM_KEY = Key.ofString("state_changer");

    private final Map<Class<?>, Adapter<?>> adapters;

    private final ItemStack baseItem = new ItemBuilder(Material.SHEARS, ITEM_KEY)
            .setName("&aState Changer")
            .addSmartLore(
                    "What looks like a normal pair or shears is actually an ancient item, used to modify shapes and forms of blocks. The unknown origin of this item makes it even more mysterious.",
                    "&8&o"
            )
            .glow()
            .build();

    public StateChanger(Main main) {
        super(main);

        setDescription("Gives player a state changer item.");
        adapters = Maps.newLinkedHashMap();

        registerAdapters();
    }

    @Nonnull
    public Collection<Adapter<?>> getAdapters() {
        return adapters.values();
    }

    public void openEditor(Player player, Block block) {
        final String blockName = Chat.capitalize(block.getType());

        if (isBannedBlock(block.getType())) {
            Message.error(player, "%s is not allowed!", blockName);
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (!isAllowedBlock(block)) {
            Message.error(player, "%s doesn't have any block data!", blockName);
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        final StringBuilder builder = new StringBuilder("Editing ");
        if (blockName.length() >= 22) {
            builder.append(blockName, 0, 22);
            builder.append("...");
        }
        else {
            builder.append(blockName);
        }

        new StateChangerGUI(player, builder.toString(), new StateChangerData(player, block));
    }

    public void giveItem(Player player) {
        final PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            Message.error(player, "Not enough space in inventory!");
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        inventory.addItem(new ItemBuilder(baseItem)
                .addLore()
                .addLore("Created by almighty %s&7.".formatted(randomColorName(player)))
                .toItemStack());

        Message.success(player, "Success!");
    }

    public boolean isAllowedBlock(Block block) {
        final BlockData data = block.getBlockData();

        for (Class<?> clazz : adapters.keySet()) {
            if (clazz.isInstance(data)) {
                return true;
            }
        }

        return false;
    }

    // Hard-coded banned blocks
    public boolean isBannedBlock(Material material) {
        return switch (material) {
            case BEDROCK -> true;
            default -> false;
        };
    }

    private void registerAdapters() {
        registerAdapter(new AdapterWaterlogged()); // Yes
        registerAdapter(new AdapterFence()); // yes
        registerAdapter(new AdapterWall()); // Yes
        registerAdapter(new AdapterSlab()); // Yes
        registerAdapter(new AdapterBisected()); // Yes
        registerAdapter(new AdapterDirectional()); // Yes
        registerAdapter(new AdapterStairs()); // Yes
        registerAdapter(new AdapterMultipleFacing()); // Yes
        registerAdapter(new AdapterAgeable()); // Yes
        registerAdapter(new AdapterBamboo()); // Yes
        registerAdapter(new AdapterBed()); // Yes
        registerAdapter(new AdapterRail()); // This has an issue with ascending shape
        registerAdapter(new AdapterAnaloguePowerable()); // Yes
        registerAdapter(new AdapterBeehive()); // Yes
        registerAdapter(new AdapterBell()); // Yes
        registerAdapter(new AdapterBigDripleaf()); // Yes
        registerAdapter(new AdapterCake()); // Yes
        registerAdapter(new AdapterCaveVines()); // Yes
        registerAdapter(new AdapterFarmland()); // Yes
        registerAdapter(new AdapterGate()); // Yes
        registerAdapter(new AdapterLevelled()); // Yes
        registerAdapter(new AdapterPiston()); // Yes
        registerAdapter(new AdapterPistonHead()); // Yes
        registerAdapter(new AdapterPointedDripstone()); // Yes
        registerAdapter(new AdapterRedstoneWire()); // Yes
        registerAdapter(new AdapterRotatable()); // Yes
        registerAdapter(new AdapterScaffolding()); // Yes
        registerAdapter(new AdapterSculkSensor()); // Yes
        registerAdapter(new AdapterSnow()); // Yes
        registerAdapter(new AdapterSnowable()); // Yes
        registerAdapter(new AdapterPinkPetals()); // Yes
        registerAdapter(new AdapterChiseledBookshelf()); // Yeppers

        // No fucking idea why the comments are about, probably 'tests' ¯\_(ツ)_/¯
        registerAdapter(new AdapterFaceAttachable());
        registerAdapter(new AdapterOpenable());
        registerAdapter(new AdapterCandle());
        registerAdapter(new AdapterLit());
        registerAdapter(new AdapterOrientable());
        registerAdapter(new AdapterMossCarpet());

        registerAdapter(new AdapterSegmentable());
    }

    private void registerAdapter(@Nonnull Adapter<?> adapter) {
        final Class<?> clazz = adapter.getDataClass();

        if (adapters.containsKey(clazz)) {
            throw new IllegalArgumentException("Adapter for %s is already registered by %s!".formatted(
                    clazz.getSimpleName(),
                    adapters.get(clazz).getClass().getSimpleName()
            ));
        }

        adapters.put(clazz, adapter);
    }

    private static String randomColorName(Player player) {
        final Gradient gradient = new Gradient(player.getName()).makeBold();
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        return gradient.rgb(
                new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)),
                new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)),
                Interpolators.LINEAR
        );
    }

}

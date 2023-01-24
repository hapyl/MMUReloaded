package me.hapyl.mmu3;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.config.Config;
import me.hapyl.spigotutils.module.config.DataField;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class PersistentPlayerData extends Config {

    private final static Map<UUID, PersistentPlayerData> persistentData = Maps.newHashMap();

    private final UUID playerUUID;
    @DataField(path = "query.item_creator") private String lastItemCreatorQuery;
    @DataField(path = "feature.entity_removal") private boolean entityRemoval;
    @DataField(path = "feature.command_preview") private boolean commandPreview;
    @DataField(path = "feature.quick_undo") private boolean quickUndo;
    @DataField(path = "game.cookie") private int cookieClicks;
    @DataField(path = "game.ultrasequencer") private int ultraSequencer;

    private PersistentPlayerData(UUID uuid) {
        super(Main.getInstance(), "/players", uuid.toString());
        playerUUID = uuid;
    }

    private PersistentPlayerData(Player player) {
        this(player.getUniqueId());
        loadData();
        persistentData.put(player.getUniqueId(), this);
    }

    public boolean isQuickUndo() {
        return quickUndo;
    }

    public void setQuickUndo(boolean quickUndo) {
        this.quickUndo = quickUndo;
    }

    public boolean isCommandPreview() {
        return commandPreview;
    }

    public void setCommandPreview(boolean commandPreview) {
        this.commandPreview = commandPreview;
    }

    public int getCookieClicks() {
        return cookieClicks;
    }

    public void setCookieClicks(int cookieClicks) {
        this.cookieClicks = cookieClicks;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public boolean isEntityRemoval() {
        return entityRemoval;
    }

    public void setEntityRemoval(boolean entityRemoval) {
        this.entityRemoval = entityRemoval;
    }

    public String getLastItemCreatorQuery() {
        return lastItemCreatorQuery;
    }

    public void setLastItemCreatorQuery(String lastItemCreatorQuery) {
        this.lastItemCreatorQuery = lastItemCreatorQuery;
    }

    public void loadData() {
        loadDataFields();
    }

    public void saveData() {
        saveDataFields();
        save();
    }

    // static members
    public static void createData(Player player) {
        if (hasData(player)) {
            return;
        }

        new PersistentPlayerData(player);
    }

    @Nonnull
    public static PersistentPlayerData getData(Player player) {
        if (!hasData(player)) {
            new PersistentPlayerData(player);
        }
        return persistentData.get(player.getUniqueId());
    }

    public static boolean hasData(Player player) {
        return persistentData.containsKey(player.getUniqueId());
    }

    public void setUltrasequencerRound(int round) {
        if (round > ultraSequencer) {
            this.ultraSequencer = round;
        }
    }
}

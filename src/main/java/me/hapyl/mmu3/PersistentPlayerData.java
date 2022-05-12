package me.hapyl.mmu3;

import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.module.config.Config;
import kz.hapyl.spigotutils.module.config.DataField;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PersistentPlayerData extends Config {

    private final static Map<UUID, PersistentPlayerData> persistentData = Maps.newHashMap();

    private final UUID playerUUID;
    @DataField(path = "query.item_creator") private String lastItemCreatorQuery;
    @DataField(path = "feature.entity_removal") private boolean entityRemoval;
    @DataField(path = "game.cookie") private int cookieClicks;

    private PersistentPlayerData(UUID uuid) {
        super(Main.getInstance(), "/players", uuid.toString());
        playerUUID = uuid;
    }

    private PersistentPlayerData(Player player) {
        this(player.getUniqueId());
        loadData();
        persistentData.put(player.getUniqueId(), this);
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

    public static PersistentPlayerData getData(Player player) {
        if (!hasData(player)) {
            new PersistentPlayerData(player);
        }
        return persistentData.get(player.getUniqueId());
    }

    public static boolean hasData(Player player) {
        return persistentData.containsKey(player.getUniqueId());
    }

}

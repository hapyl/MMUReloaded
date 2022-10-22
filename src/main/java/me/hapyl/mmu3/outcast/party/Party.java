package me.hapyl.mmu3.outcast.party;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Party {

    protected static final Map<UUID, Party> byUuid = Maps.newConcurrentMap();

    private final UUID uuid;
    private final Set<OfflinePlayer> members;
    private OfflinePlayer leader;

    public Party(Player player) {
        this.leader = player;
        this.uuid = UUID.randomUUID();
        this.members = Sets.newConcurrentHashSet();

        byUuid.put(uuid, this);
    }

    public void setLeader(OfflinePlayer newLeader) {
        if (this.leader == newLeader) {
            return;
        }

        addMember(this.leader);
        this.leader = newLeader;
    }

    public void addMember(OfflinePlayer player) {
        members.add(player);
    }

    public void removeMember(OfflinePlayer player) {
        members.remove(player);
    }

    public boolean isMember(OfflinePlayer player) {
        return members.contains(player);
    }

    public OfflinePlayer getLeader() {
        return leader;
    }

    public UUID getUuid() {
        return uuid;
    }
}

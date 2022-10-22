package me.hapyl.mmu3.feature.vote;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

public class Vote {

    private final String topic;
    private final UUID uuid;
    private final Map<Integer, VoteOption> votes;
    private boolean isOpen;

    public Vote(String topic) {
        this.topic = topic;
        this.uuid = UUID.randomUUID();
        this.votes = Maps.newHashMap();
        this.isOpen = true;
    }

    public void addVote(String topic) {
        votes.put(votes.size(), new VoteOption(topic));
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }
}

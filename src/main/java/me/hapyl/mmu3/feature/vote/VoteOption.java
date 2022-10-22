package me.hapyl.mmu3.feature.vote;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.UUID;

public class VoteOption implements Comparable<VoteOption> {

    private final String topic;
    private final Set<UUID> voted;

    public VoteOption(String topic) {
        this.topic = topic;
        this.voted = Sets.newHashSet();
    }

    public Set<UUID> getVoted() {
        return voted;
    }

    public String getTopic() {
        return topic;
    }

    public int getTimesVoted() {
        return voted.size();
    }

    @Override
    public int compareTo(VoteOption other) {
        final int otherTimesVoted = other.getTimesVoted();
        final int timesVoted = getTimesVoted();

        return Integer.compare(otherTimesVoted, timesVoted);
    }
}

package me.hapyl.mmu3.outcast.chatgame;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public abstract class ChatGameInstance {

    public final ChatGame reference;

    private final Map<UUID, Long> answeredPlayers;
    protected final String correctAnswer;

    private final long startTime;

    protected ChatGameInstance(ChatGame reference, String correctAnswer) {
        this.reference = reference;
        this.correctAnswer = correctAnswer;
        this.answeredPlayers = Maps.newLinkedHashMap();
        this.startTime = System.currentTimeMillis();
    }

    public void onCorrectGuess(Player player) {
        reference.log(
                "&2%s&a answered correctly in &2%s&a!",
                player.getName(),
                Chat.formatTimeString(getTimeTookToAnswer(player))
        );
    }

    public boolean hasAnswered(Player player) {
        return answeredPlayers.containsKey(player.getUniqueId());
    }

    public long getAnsweredAt(Player player) {
        return answeredPlayers.getOrDefault(player.getUniqueId(), 0L);
    }

    public long getTimeTookToAnswer(Player player) {
        return getAnsweredAt(player) - startTime;
    }

    public void addAnswered(Player player) {
        final long timeMillis = System.currentTimeMillis();

        answeredPlayers.put(player.getUniqueId(), timeMillis);

        onCorrectGuess(player);
    }


}

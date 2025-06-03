package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.util.Enums;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.input.InputListener;
import me.hapyl.mmu3.util.input.InputType;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StandEditorData {

    private final Player player;
    private final StandLoadout[] loadouts;

    @Nullable protected Await await;
    @Nonnull protected TuneAxis axis;

    @Nullable private ArmorStand stand;
    private double speed;
    private long enterMoveAt;

    StandEditorData(Player player) {
        this.player = player;
        this.stand = null;
        this.await = null;
        this.axis = TuneAxis.X;
        this.speed = 0.1;
        this.loadouts = new StandLoadout[9];
    }

    public void edit(@Nullable ArmorStand stand) {
        this.stand = stand;
    }

    public boolean isEditing() {
        return stand != null;
    }

    @Nonnull
    public Player player() {
        return player;
    }

    @Nonnull
    public ArmorStand stand() {
        if (stand == null) {
            throw new IllegalStateException("Not editing armor stand!");
        }

        return stand;
    }

    @Nullable
    public ArmorStand standOrNull() {
        return stand;
    }

    public boolean canLeaveMoveMode() {
        return await == Await.MOVE && (System.currentTimeMillis() - enterMoveAt) >= 100;
    }

    public String getStandName() {
        final ArmorStand stand = stand();
        final String customName = stand.getCustomName();
        final String uuid = stand.getUniqueId().toString();

        return customName == null ? "Armor Stand &8(%s)".formatted(uuid.substring(0, uuid.indexOf("-"))) : customName;
    }

    public void exitMoveMode() {
        this.await = null;
        this.enterMoveAt = 0;
    }

    public void enterMoveMode() {
        this.await = Await.MOVE;
        this.enterMoveAt = System.currentTimeMillis();

        new InputListener() {
            @Nonnull
            @Override
            public String name() {
                return "Move Mode";
            }

            @Override
            public void showUsage(@Nonnull Player player) {
                InputListener.showUsage(player, "move armor stand.", InputType.W, InputType.S, InputType.A, InputType.D);
                InputListener.showUsage(player, "ascend armor stand.", InputType.SPACE);
                InputListener.showUsage(player, "descend armor stand.", InputType.SNEAK);
                InputListener.showUsage(player, "cycle moving speed.", InputType.F);
                InputListener.showUsage(player, "to leave the editor", InputType.LEFT, InputType.RIGHT);
            }

            @Override
            public void listen(@Nonnull Player player, @Nonnull InputType input) {
                // Null safe guard
                if (stand == null) {
                    return;
                }

                switch (input) {
                    case W, A, S, D -> {
                        final BlockFace facing = player.getFacing();
                        final Vector forward = facing.getDirection().setY(0).normalize();
                        final Vector right = forward.clone().rotateAroundY(Math.PI / 2);

                        final Vector vector = switch (input) {
                            case W -> forward;
                            case S -> forward.clone().multiply(-1);
                            case A -> right;
                            case D -> right.clone().multiply(-1);
                            default -> throw new IllegalStateException("Unexpected input: " + input);
                        };

                        stand.teleport(stand.getLocation().add(vector.multiply(speed)));
                    }
                    case SPACE -> stand.teleport(stand.getLocation().add(new Vector(0, speed, 0)));
                    case SNEAK -> stand.teleport(stand.getLocation().add(new Vector(0, -speed, 0)));
                    case LEFT, RIGHT -> {
                        if (canLeaveMoveMode()) {
                            exitMoveMode();

                            stopListening(player);

                            // Re-open the GUI
                            new StandEditorGUI(player, StandEditorData.this);
                        }
                    }
                    case F -> {
                        speed = speed == 0.1 ? 0.01 : (speed == 0.01 ? 1.0 : 0.1);

                        Chat.sendTitle(
                                player,
                                "&aᴄᴜʀʀᴇɴᴛ ꜱᴘᴇᴇᴅ",
                                "%s[0.1] %s[0.01] %s[1.0]".formatted(
                                        speed == 0.1 ? "&b&l" : "&8",
                                        speed == 0.01 ? "&b&l" : "&8",
                                        speed == 1.0 ? "&b&l" : "&8"
                                ),
                                0,
                                20,
                                0
                        );

                        Message.sound(player, Sound.UI_BUTTON_CLICK, 1.0F);
                    }
                    case START_LISTENING -> Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.75f);
                }
            }
        }.startListening(player);
    }

    public void nextAxis() {
        axis = Enums.getNextValue(TuneAxis.class, axis);
    }

    public void saveLoadout(@Range(from = 0, to = 8) int slot) {
        loadouts[slot] = new StandLoadout(this);
    }

    @Nullable
    public StandLoadout getLoadout(@Range(from = 0, to = 8) int slot) {
        return loadouts[slot];
    }

    public void stopEditing() {
        stand = null;
    }

    public enum Await {
        MOVE,
        NAME,
        TEAM_MODIFY
    }
}

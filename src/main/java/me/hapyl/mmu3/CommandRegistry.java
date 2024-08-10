package me.hapyl.mmu3;

import me.hapyl.mmu3.command.*;
import me.hapyl.mmu3.command.brush.BrushCommand;
import me.hapyl.eterna.module.command.CommandProcessor;
import me.hapyl.eterna.module.command.SimpleCommand;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class CommandRegistry {

    private final CommandProcessor processor;

    public CommandRegistry(Main plugin) {
        this.processor = new CommandProcessor(plugin);
        registerCommands();
    }

    private void registerCommands() {
        register(new StateChangerCommand("stateChanger"));
        register(new EditStandCommand("editStand"));
        register(new SpecialBlocksCommand("specialBlocks"));
        register(new CalculateCommand("calculate"));
        register(new BackCommand("back"));
        register(new CenterCommand("center"));
        register(new ItemCreatorCommand("itemCreator"));
        register(new EntityRemovalCommand("entityRemoval"));
        register(new GameModeCommand("gm"));
        register(new OpenInventoryCommand("openInventory"));
        register(new RollCommand("roll"));
        register(new PersonalTimeCommandCommand("personalTime"));
        register(new PersonalWeatherCommand("personalWeather"));
        register(new ItemCommand("getItem"));
        register(new GameCommand("game"));
        register(new SightBlockCommand("sightBlock"));
        register(new RawCommand("raw"));
        register(new LockCommand("lock"));
        register(new HatCommand("hat"));
        register(new SpeedCommand("speed"));
        register(new CandleCommand("candle"));
        register(new ActionCommand("action"));
        register(new PacketCommand("playerpacket"));
        register(new CalculateRelative("relative"));
        register(new PingCommand("ping"));
        register(new BrushCommand("mmuBrush"));
        register(new SelfTeleportCommand("self"));
        register(new DesignerCommand("designer"));
        register(new SoundCommand("sound"));
        register(new NumericIdCommand("id"));
        register(new BukkitTagCommand("tags"));
        register(new CommandBlockPreviewCommand("commandBlockPreview"));
        register(new ParticleCommand("particles"));
        register(new FindEmptyCommandBlocksCommand("findEmptyCommandBlocks"));
        register(new TeleportShortcutCommand(">"));
        register(new NightVisionCommand("nightvision"));
        register(new WatcherCommand("watcher"));
        register(new WarpCommand("warp"));
        register(new LineOfSightCommand("los"));
        register(new ActivityCommand("suppressBlockUpdates"));
        register(new BoundingBoxCommand("boundingBox"));
        register(new SpawnCommand("spawn"));
        register(new DeleteCommand("delete"));
        register(new FillNearCommand("fillNear"));
        register(new ColorCommand("color"));
        register(new MmuUndoCommand("mmuUndo"));
        register(new FixTreeOrientationCommand("fixTreeOrientation"));
        register(new SearchCommand("search"));
        register(new TrimCommand("trim"));
        register(new WaterlogCommand("waterlog"));
        register(new WikiCommand("mmuWiki"));
        register(new BannerEditorCommand("bannerEditor"));
        register(new SpawnDisplaySkullCommand("spawnSkullDisplay"));
    }

    private void registerTestCommand(String command, BiConsumer<Player, String[]> consumer) {
        register(new SimplePlayerAdminCommand("_test" + command) {
            @Override
            protected void execute(Player player, String[] strings) {
                consumer.accept(player, strings);
            }
        });
    }

    private void register(SimpleCommand command) {
        processor.registerCommand(command);
    }

}

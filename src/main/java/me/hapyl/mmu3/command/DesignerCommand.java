package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.designer.Designer;
import me.hapyl.mmu3.feature.designer.DesignerGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DesignerCommand extends SimplePlayerAdminCommand {
    public DesignerCommand(String name) {
        super(name);
        setDescription("Allows to create and use a GUI designer.");
        setAliases("ds");
    }

    @Override
    protected void execute(Player player, String[] args) {
        //
        // designer - Open current designer
        // designer (Name) - Open someone elses designer
        // designer (Int) - Create new designer with provided size
        final Designer manager = Main.getRegistry().designer;

        if (args.length == 0) {
            final DesignerGUI designer = manager.getDesigner(player);
            if (designer == null) {
                Message.error(player, "You don't have a designer! Create one using &e/designer (Size[1-6])");
                return;
            }

            designer.open(player);
            return;
        }

        if (Validate.isInt(args[0])) {
            final int size = Numbers.clamp(Numbers.getInt(args[0], 0), 1, 6);
            final DesignerGUI designer = new DesignerGUI(player, size);
            manager.setDesigner(player, designer);
            Message.success(player, "Creating new designer with size of %s.", size);
            designer.open(player);
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            Message.PLAYER_NOT_ONLINE.send(player, args[0]);
            return;
        }

        final DesignerGUI designer = manager.getDesigner(target);
        if (designer == null) {
            Message.error(player, "%s doesn't have a designer!", target.getName());
            return;
        }

        designer.open(player);
        Message.info(player, "Opening %s designer...", target.getName());
    }
}

package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import me.hapyl.spigotutils.module.util.Wrap;
import org.bukkit.Keyed;
import org.bukkit.Tag;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class BukkitTagCommand extends SimplePlayerAdminCommand {

    private final Map<String, Tag<?>> byName = Maps.newHashMap();

    public BukkitTagCommand(String name) {
        super(name);
        setDescription("Shows all materials in existing tag.");
        populateTags();

        addCompleterValues(1, byName.keySet().toArray(new String[] {}));
    }

    private void populateTags() {
        for (Field field : Tag.class.getFields()) {
            if (field.getType() != Tag.class) {
                continue;
            }

            try {
                final Tag<?> tag = (Tag<?>) field.get(null);
                if (tag == null) {
                    continue;
                }
                byName.put(tag.getKey().getKey().toLowerCase(Locale.ROOT), tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void execute(Player player, String[] args) {
        //
        // bukkittag (Tag)
        // bukkittag (Tag) (value)
        //
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final Tag<?> tag = byName.get(args[0].toLowerCase(Locale.ROOT));
        if (tag == null) {
            Message.error(player, "%s is invalid tag!", args[0]);
            return;
        }

        final String tagName = tag.getKey().getKey();

        if (args.length == 1) {
            Message.success(player, "%s contains: %s", tagName, CollectionUtils.wrapToString(tag.getValues(), Wrap.DEFAULT));
            return;
        }

        final String str = args[1].toLowerCase();
        final boolean present = isPresent(tag, str);

        // FIXME: 006, Nov 6, 2022 - Incorrect isPresent check
        if (present) {
            Message.error(player, "%s is not present in %s tag!", str, tagName);
        }
        else {
            Message.success(player, "%s is present in %s tag!", str, tagName);
        }

    }

    private boolean isPresent(Tag<?> tag, String str) {
        for (Keyed value : tag.getValues()) {
            final String key = value.getKey().getKey();
            if (key.contains(str)) {
                return true;
            }
        }
        return false;
    }

}

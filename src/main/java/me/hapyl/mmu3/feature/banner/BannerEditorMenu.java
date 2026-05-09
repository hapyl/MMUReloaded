package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.data.PlayerData;
import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.EncodedString;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.menu.Menu;
import me.hapyl.mmu3.util.menu.MenuAction;
import me.hapyl.mmu3.util.menu.MenuIcons;
import me.hapyl.mmu3.util.menu.Size;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class BannerEditorMenu extends Menu {
    
    private static final int[] SLOTS_LAYERS = new int[] {
            28, 29, 30, 31, 32, 33, 34
    };
    
    private static final int ITEMS_PER_PAGE = SLOTS_LAYERS.length;
    private static final int MOVE_INDEX_NOT_MOVING = -1;
    
    private final BannerData data;
    private int page;
    private int moveIndex;
    
    public BannerEditorMenu(@NotNull Player player, int page) {
        super(player, Component.text("Banner Editor"), Size.SIZE_5);
        
        this.data = PlayerData.ofPlayer(player).requestData(BannerData.class);
        this.page = page;
        this.moveIndex = MOVE_INDEX_NOT_MOVING;
        
        this.openMenu();
    }
    
    @Override
    public void updateMenu() {
        // Set banner preview
        setItem(
                13,
                data.createPreviewBuilder()
                    .addLore(Component.text("This is a preview of the banner!", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC))
                    .addLore()
                    .addLore(ButtonComponents.left("get the item"))
                    .addLore(ButtonComponents.right("change base color"))
                    .build(),
                MenuAction.builder()
                          .left(player -> {
                              final PlayerInventory inventory = player.getInventory();
                              final SerializedItemStack serializedItemStack = data.createFinalItem();
                              
                              inventory.addItem(serializedItemStack.itemStack());
                              player.closeInventory();
                              
                              MMULogger.success(player, Component.text("There is your banner!"));
                              
                              // Serialize
                              final EncodedString encodedString = serializedItemStack.encodedString();
                              
                              MMULogger.success(
                                      player,
                                      Component.empty()
                                               .append(Component.text("Use "))
                                               .append(Component.text("/banner %s".formatted(encodedString.toString()), NamedTextColor.YELLOW))
                                               .append(Component.text(" to share your banner!"))
                                               .hoverEvent(
                                                       HoverEvent.showText(Component.text("Click to copy the code!", NamedTextColor.YELLOW))
                                               )
                                               .clickEvent(ClickEvent.suggestCommand(encodedString.toString()))
                              );
                          })
                          .right(player -> {
                              new BannerEditorBaseColorMenu(player, data);
                          })
        );
        
        // Load layers
        final int startIndex = (page - 1) * ITEMS_PER_PAGE;
        
        for (int i = 0; i < SLOTS_LAYERS.length; i++) {
            final int index = startIndex - i;
            final int layer = index + 1;
            
            final int slot = SLOTS_LAYERS[i];
            
            // Max patterns
            if (index >= BannerEditor.MAX_PATTERNS) {
                setItem(slot, makeMaxLayersItem(layer));
                continue;
            }
            
            // Check for moving
            if (moveIndex != MOVE_INDEX_NOT_MOVING) {
                // Cannot move to the same index or outside the layers
                if (moveIndex == index || index >= data.patternSize()) {
                    final boolean isSameIndex = moveIndex == index;
                    
                    setItem(
                            slot,
                            new ItemBuilder(isSameIndex ? Material.YELLOW_DYE : Material.RED_DYE)
                                    .setAmount(layer)
                                    .setName(Component.text("Layer %s".formatted(layer), NamedTextColor.RED))
                                    .addLore()
                                    .addLore(Component.text("Cannot move here!", NamedTextColor.RED))
                                    .addLore(
                                            isSameIndex
                                            ? Component.text("It's the same place!", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC)
                                            : Component.text("There is no layer here!", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC)
                                    )
                                    .addLore()
                                    .addLore(ButtonComponents.left("cancel"))
                                    .predicate(isSameIndex, ItemBuilder::glow)
                                    .build(),
                            MenuAction.builder()
                                      .left(player -> {
                                          moveIndex = MOVE_INDEX_NOT_MOVING;
                                          this.openMenu();
                                      })
                    );
                }
                // Otherwise allow movement
                else {
                    setItem(
                            slot,
                            new ItemBuilder(Material.LIME_DYE)
                                    .setAmount(layer)
                                    .setName(Component.text("Layer %s".formatted(layer)))
                                    .addLore()
                                    .addLore(ButtonComponents.left("move here"))
                                    .build(),
                            MenuAction.builder()
                                      .left(player -> {
                                          data.move(moveIndex, index);
                                          moveIndex = MOVE_INDEX_NOT_MOVING;
                                          
                                          this.openMenu();
                                      })
                    );
                }
            }
            // Otherwise, display the pattern
            else {
                // Not pattern yet
                if (index >= data.patternSize()) {
                    setItem(
                            slot,
                            new ItemBuilder(Material.GRAY_DYE)
                                    .setAmount(layer)
                                    .setName(Component.text("Layer %s".formatted(layer)))
                                    .addLore(Component.text("This layer is empty!", NamedTextColor.DARK_GRAY))
                                    .addLore()
                                    .addLore(Component.text("Click the button below to add a layer!"))
                                    .build()
                    );
                }
                else {
                    final ItemBuilder builder = data.createLayerBuilder(startIndex, layer, true);
                    
                    builder.setName(Component.text("Layer %s".formatted(layer)));
                    
                    builder.addLore();
                    builder.addLore(ButtonComponents.left("modify layer"));
                    builder.addLore(ButtonComponents.right("remove layer"));
                    builder.addLore(ButtonComponents.middle("move layer"));
                    
                    setItem(
                            slot,
                            builder.build(),
                            MenuAction.builder()
                                      .left(player -> {
                                          new BannerEditorLayerMenu(player, BannerEditorLayerMenu.Op.editLayer(index), this);
                                      })
                                      .right(player -> {
                                          data.removePattern(index);
                                          this.openMenu();
                                      })
                                      .middle(player -> {
                                          moveIndex = index;
                                          this.openMenu();
                                      })
                    );
                }
            }
        }
        
        // Set page arrows
        if (page > 1) {
            setItem(
                    27,
                    MenuIcons.PAGE_PREVIOUS,
                    MenuAction.builder().left(player -> {
                        this.page--;
                        this.openMenu();
                    })
            );
        }
        
        if (page * ITEMS_PER_PAGE <= BannerEditor.MAX_PATTERNS) {
            setItem(
                    45,
                    MenuIcons.PAGE_NEXT,
                    MenuAction.builder().left(player -> {
                        this.page++;
                        this.openMenu();
                    })
            );
        }
        
        // Set add pattern button only if we're not in moving mode
        if (moveIndex == MOVE_INDEX_NOT_MOVING) {
            if (data.patternSize() == BannerEditor.MAX_PATTERNS) {
                setItem(40, makeMaxLayersItem(1));
            }
            else {
                setItem(
                        40,
                        new ItemBuilder(Material.LIME_DYE)
                                .setName(Component.text("Add Layer"))
                                .addLore()
                                .addLore(Component.text("Adds a new layer to the banner."))
                                .addLore()
                                .addLore(ButtonComponents.left("add a layer"))
                                .build(),
                        MenuAction.builder()
                                  .left(player -> new BannerEditorLayerMenu(player, BannerEditorLayerMenu.Op.newLayer(), this))
                );
            }
        }
        
        // Reset banner button
        setPanelItem(
                6,
                new ItemBuilder(Material.WATER_BUCKET)
                        .setName(Component.text("Reset Editor", NamedTextColor.RED))
                        .addLore()
                        .addLore(ComponentHelper.wrap("Resets the banner editor to the default state."))
                        .addLore()
                        .addLore(Component.text("WARNING", NamedTextColor.RED, TextDecoration.BOLD))
                        .addLore(Component.text("This action cannot be undone!"))
                        .addLore()
                        .addLore(ButtonComponents.left("reset the editor"))
                        .build(),
                MenuAction.builder()
                          .left(player -> {
                              data.reset();
                              
                              MMULogger.success(player, Component.text("Reset banner editor!"));
                              MMULogger.sound(player, Sound.ENTITY_VILLAGER_WORK_LEATHERWORKER, 2.0f);
                              
                              this.openMenu();
                          })
        );
    }
    
    public int getPage() {
        return page;
    }
    
    @NotNull
    private ItemStack makeMaxLayersItem(int layer) {
        return new ItemBuilder(Material.RED_DYE)
                .setAmount(layer)
                .setName(Component.text("Max Layers Reached!", NamedTextColor.RED))
                .addLore()
                .addLore(Component.text("You cannot add any more layers!"))
                .build();
    }
}

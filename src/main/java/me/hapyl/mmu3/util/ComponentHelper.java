package me.hapyl.mmu3.util;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ComponentHelper {
    
    private static final int DEFAULT_WRAP_LIMIT = 35;
    
    private static final Component CHECKMARK_TRUE = Component.text("✔", NamedTextColor.GREEN);
    private static final Component CHECKMARK_FALSE = Component.text("✘", NamedTextColor.RED);
    
    private ComponentHelper() {
    }
    
    @NotNull
    public static Component capitalize(@NotNull Object object) {
        final String[] words = object.toString().toLowerCase().split(" ");
        final StringBuilder builder = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
            
            builder.append(" ");
        }
        
        return Component.text(builder.toString().trim());
    }
    
    @NotNull
    public static Component capitalize(@NotNull Enum<?> enumObject) {
        return capitalize(enumObject.name().replace("_", " "));
    }
    
    @NotNull
    public static Component applyDefaultStyle(@NotNull Component component, @NotNull Style style) {
        final TextColor supplementaryColor = style.color();
        
        if (component.color() == null && supplementaryColor != null) {
            component = component.color(supplementaryColor);
        }
        
        for (TextDecoration textDecoration : TextDecoration.values()) {
            final TextDecoration.State supplementaryDecoration = style.decoration(textDecoration);
            
            component = component.decorationIfAbsent(
                    textDecoration,
                    supplementaryDecoration != TextDecoration.State.NOT_SET
                    ? supplementaryDecoration
                    : TextDecoration.State.FALSE
            );
        }
        
        return component;
    }
    
    @NotNull
    public static Component join(@NotNull Component delimiter, @NotNull Component... components) {
        if (components.length == 0) {
            return Component.empty();
        }
        
        final TextComponent.Builder builder = Component.text();
        
        for (int i = 0; i < components.length; i++) {
            final Component component = components[i];
            
            if (i != 0) {
                builder.append(delimiter);
            }
            
            builder.append(component);
        }
        
        return builder.build();
    }
    
    @NotNull
    public static List<? extends Component> wrap(@NotNull String string, int maxLength) {
        if (string.isEmpty()) {
            return List.of();
        }
        
        final List<Component> components = Lists.newArrayList();
        final String[] words = string.split(" ");
        
        StringBuilder builder = new StringBuilder();
        
        for (String word : words) {
            final int length = word.length();
            
            if ((builder.length() + length + 1) >= maxLength) {
                components.add(Component.text(builder.toString()));
                builder = new StringBuilder();
            }
            
            if (length > 0) {
                builder.append(" ");
            }
            
            builder.append(word);
        }
        
        if (!builder.isEmpty()) {
            components.add(Component.text(builder.toString()));
        }
        
        return components;
    }
    
    @NotNull
    public static List<? extends Component> wrap(@NotNull String string) {
        return wrap(string, DEFAULT_WRAP_LIMIT);
    }
    
    @NotNull
    public static Component checkmark(@NotNull Component component, @Nullable Boolean condition) {
        if (condition == null) {
            return component;
        }
        
        return component.appendSpace().append(condition ? CHECKMARK_TRUE : CHECKMARK_FALSE);
    }
    
}
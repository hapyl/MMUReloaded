package me.hapyl.mmu3.util.menu;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MenuPattern {
    
    public static final MenuPattern DEFAULT;
    public static final MenuPattern INNER_LEFT_TO_RIGHT;
    
    public static final int CONTAINER_LENGTH = 9;
    
    static {
        DEFAULT = compile(new byte[][] {
                { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
                { 0, 0, 1, 1, 0, 1, 1, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 1, 0, 0 },
                { 0, 1, 1, 1, 0, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 0 }
        });
        
        INNER_LEFT_TO_RIGHT = compile(new byte[][] {
                { 0, 1, 0, 0, 0, 0, 0, 0, 0, },
                { 0, 1, 1, 0, 0, 0, 0, 0, 0, },
                { 0, 1, 1, 1, 0, 0, 0, 0, 0, },
                { 0, 1, 1, 1, 1, 0, 0, 0, 0, },
                { 0, 1, 1, 1, 1, 1, 0, 0, 0, },
                { 0, 1, 1, 1, 1, 1, 1, 0, 0, },
                { 0, 1, 1, 1, 1, 1, 1, 1, 0, }
        });
    }
    
    private final TreeMap<Integer, byte[]> compiled;
    
    MenuPattern(@NotNull TreeMap<Integer, byte[]> compiled) {
        this.compiled = compiled;
    }
    
    public int maxWidth() {
        return compiled.lastKey();
    }
    
    public byte[] patternFor(int width) {
        final Map.Entry<Integer, byte[]> entry = compiled.floorEntry(width);
        
        if (entry == null) {
            throw new IllegalArgumentException("Cannot determine pattern for size %s!".formatted(width));
        }
        
        return entry.getValue();
    }
    
    @NotNull
    public static MenuPattern compile(byte[][] patterns) {
        final TreeMap<Integer, byte[]> compiled = Maps.newTreeMap();
        
        for (byte[] pattern : patterns) {
            final int index = countOnes(pattern);
            
            if (index == 0) {
                throw new IllegalArgumentException("Pattern cannot be empty! %s".formatted(Arrays.toString(pattern)));
            }
            else if (pattern.length != CONTAINER_LENGTH) {
                throw new IllegalArgumentException("Pattern length must be of length %s!".formatted(CONTAINER_LENGTH));
            }
            
            compiled.put(index, pattern);
        }
        
        return new MenuPattern(compiled);
    }
    
    private static int countOnes(byte[] pattern) {
        int count = 0;
        
        for (byte b : pattern) {
            if (b == 1) {
                count++;
            }
        }
        
        return count;
    }
    
}
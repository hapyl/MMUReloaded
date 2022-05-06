package me.hapyl.mmu3.feature.standeditor;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class TuneData {

    private Part part;
    private Axis axis;

    public TuneData() {
        part = Part.HEAD;
        axis = Axis.X;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Axis getAxis() {
        return axis;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }

    public boolean isClean() {
        return part == null && axis == null;
    }

    public enum Axis {
        X(Material.RED_TERRACOTTA, ChatColor.RED),
        Y(Material.GREEN_TERRACOTTA, ChatColor.GREEN),
        Z(Material.BLUE_TERRACOTTA, ChatColor.BLUE);

        private final Material material;
        private final ChatColor color;

        Axis(Material material, ChatColor color) {
            this.material = material;
            this.color = color;
        }

        public Material getMaterial() {
            return this.material;
        }

        public ChatColor getColor() {
            return this.color;
        }

        public double[] scaleIfAxis(double amount) {
            return new double[] { this == X ? amount : 0.0, this == Y ? amount : 0.0, this == Z ? amount : 0.0 };
        }

    }

    public enum Part {
        HEAD(Material.LEATHER_HELMET, "Head"),
        BODY(Material.LEATHER_CHESTPLATE, "Body"),
        LEFT_ARM(Material.IRON_SWORD, "Left Arm"),
        RIGHT_ARM(Material.DIAMOND_SWORD, "Right Arm"),
        LEFT_LEG(Material.IRON_LEGGINGS, "Left Leg"),
        RIGHT_LEG(Material.DIAMOND_LEGGINGS, "Right Leg");

        private final Material material;
        private final String name;

        Part(Material material, String name) {
            this.material = material;
            this.name = name;
        }

        public Material getMaterial() {
            return this.material;
        }

        public String getName() {
            return ChatColor.GREEN + this.name + " Position";
        }
    }
}

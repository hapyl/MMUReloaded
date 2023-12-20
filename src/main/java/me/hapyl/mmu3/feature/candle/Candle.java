package me.hapyl.mmu3.feature.candle;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public enum Candle {

    WHITE("787d835b543ed1b02415701ca7b3f8c8a0a112af1318f9cec5a5e91e4814a249"),
    BLACK("dafc4b045983e6895c5f40028e993d516732e936de1583ecba3f32fe15fcf03f"),
    BLUE("6d96477f4052afc36bd88086643961de79879f2480d23df182620a6d0c7bf3c0"),
    BROWN("31409f6d1df55fa59d627ec65cea0aedb43b982416d5d99b712e0f460ff73d38"),
    CYAN("39dc26ebaa256cfa024c8ffbd41dcc8edd9bf17a12837906967b7af194c77d32"),
    GREEN("d352fd535d6faa9199dcad1ac81fe1e91adbf393c2a031e2c908ff8daa86ec1d"),
    GRAY("da664adbf375b58e98549469e9cb045d1d363feeea56086c844ed3ca59fc3636"),
    LIGHT_BLUE("c38f7b89dd5b40df02c35deed41e85cd66ef7f2437e7396ca71fee0857dc98d0"),
    LIME("9297e86550fc2dc91c4617a33220fb6e14027d31de7f7b29bdd72f8154738568"),
    LIGHT_GRAY("1e77ad525fba81434733ad586d956a7f3b6c619dc51b174341510353fba50115"),
    MAGENTA("e23f8a65639f6e26466c3518a1e818504b835afcbf6fd7096ee3bd977133ec01"),
    ORANGE("b6c0b317bdef00fabb904a990c75c4772b3beb16465c4fcec69a5d6b33ab96b0"),
    PINK("9f90ad608ab5f79bdb517ae634b4dc443df6fb6d0b176c11242bf8847b4eb8c"),
    PURPLE("53b47ba70c9c36aa04d45904ebf41b1fa94cc7401a676a86b25f1434edc368d8"),
    RED("a1388248ac1a905acfec801729293ce5eba78eb89a703a2b9d87c1e5ac110474"),
    YELLOW("4e6ef891f3d249fd207f71ef5e29bab72b04c30967c8aeb5c29737f030826c10");

    private final ItemStack item;

    Candle(String texture) {
        this.item = ItemBuilder.playerHeadUrl(texture).setName("&aCandle (%s)", Chat.capitalize(name())).build();
    }

    public ItemStack getItem() {
        return item;
    }
}

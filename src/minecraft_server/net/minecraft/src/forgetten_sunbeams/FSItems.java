package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Item;
import net.minecraft.src.forgetten_sunbeams.items.Quiver;

public class FSItems {
    public static Item quiver;

    public static void init() {
        quiver = new Quiver(300).setItemName("quiver");
        Item.itemsList[300] = quiver;
    }
}
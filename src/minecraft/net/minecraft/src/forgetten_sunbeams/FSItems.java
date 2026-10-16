package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Item;
import net.minecraft.src.forgetten_sunbeams.items.*;

public class FSItems {
    private static boolean initialized = false;

    public static Item quiver;
    public static Item ruby;
    public static Item dynyaSeeds;
    public static Item dynyaSlice;
    public static Item glassDoorItem;

    public static void init() {
        if (initialized) return;
        initialized = true;

        quiver = new Quiver(300);
        Item.itemsList[300] = quiver;

        dynyaSeeds = new CantaloupeSeeds(301, FSBlocks.dynyaStem.blockID);
        Item.itemsList[301] = dynyaSeeds;

        dynyaSlice = new CantaloupeSlice(302, 1, false);
        Item.itemsList[302] = dynyaSlice;
        
        ruby = new Ruby(303);
        Item.itemsList[303] = ruby;
        
    }
}
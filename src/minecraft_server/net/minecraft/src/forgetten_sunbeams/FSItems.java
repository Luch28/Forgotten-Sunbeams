package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Item;
import net.minecraft.src.forgetten_sunbeams.items.*;

public class FSItems {
    public static Item quiver;
    public static Item dynyaSeeds;
    public static Item dynyaSlice;

    public static void init() {
        quiver = new Quiver(300).setItemName("quiver");
        Item.itemsList[300] = quiver;
        
        dynyaSeeds = new DynyaSeeds(301, FSBlocks.dynyaStem.blockID);
        Item.itemsList[301] = dynyaSeeds;
        
        dynyaSlice = new DynyaSlice(302, 1, false);
        Item.itemsList[302] = dynyaSlice;
    }
}
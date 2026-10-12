package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import net.minecraft.src.forgetten_sunbeams.blocks.GlassTrapDoor;

public class FSBlocks {
    public static Block glassTrapDoor;
    
    public static void init() {
        glassTrapDoor = new GlassTrapDoor(200, Material.glass).setBlockName("glassTrapDoor");
        Block.blocksList[200] = glassTrapDoor;
        Item.itemsList[200] = new ItemBlock(200 - 256);
        
        System.out.println("Block registered: glassTrapDoor with ID 200");
    }
}
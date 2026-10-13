package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import net.minecraft.src.forgetten_sunbeams.blocks.*;
import net.minecraft.src.forgetten_sunbeams.plants.*;

public class FSBlocks {
    public static Block glassTrapDoor;
    public static Block dynyaStem;
    public static Block dynya;
    public static Block glassDoor;
    
    public static void init() {
        glassTrapDoor = new GlassTrapDoor(200, Material.glass).setBlockName("glassTrapDoor");
        Block.blocksList[200] = glassTrapDoor;
        Item.itemsList[200] = new ItemBlock(200 - 256);
        
        dynya = new BlockDynya(201, 138);
        Block.blocksList[201] = dynya;
        Item.itemsList[201] = new ItemBlock(201 - 256);
        
        glassDoor = new GlassDoor(202, Material.glass).setBlockName("glassDoor");
        Block.blocksList[202] = glassDoor;
        Item.itemsList[202] = new ItemBlock(202 - 256);
        
    	dynyaStem = new BlockDynyaStem(250, 125)
        .setBlockName("dynyaStem")
        .setHardness(0.0F);
        Block.blocksList[250] = dynyaStem;
        Item.itemsList[250] = new ItemBlock(250 - 256);
        
    }
}
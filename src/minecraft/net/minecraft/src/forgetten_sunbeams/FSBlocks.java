package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import net.minecraft.src.forgetten_sunbeams.blocks.*;
import net.minecraft.src.forgetten_sunbeams.items.*;
import net.minecraft.src.forgetten_sunbeams.plants.*;

public class FSBlocks {
    private static boolean initialized = false;

    public static Block glassTrapDoor;
    public static Block dynyaStem;
    public static Block stonecutter;
    public static Block glowingObsidian;
    public static Block netherReactor;
    public static Block blueRose;
    public static Block rubyOre;
    public static Block dynya;
    public static Block gear;
    public static Block glassDoor;
    public static Block snowyLeaves;

    public static void init() {
        if (initialized) return;
        initialized = true;

        glassTrapDoor = new GlassTrapDoor(200, Material.glass).setBlockName("glassTrapDoor");
        Block.blocksList[200] = glassTrapDoor;
        Item.itemsList[200] = new ItemBlock(200 - 256);

        dynya = new Cantaloupe(201, 138);
        Block.blocksList[201] = dynya;
        Item.itemsList[201] = new ItemBlock(201 - 256);

        glassDoor = new GlassDoor(202, Material.glass).setBlockName("glassDoor");
        Block.blocksList[202] = glassDoor;
        Item.itemsList[202] = new ItemGlassDoor(202 - 256);

        glowingObsidian = new GlowingObsidian(203, 127).setBlockName("glowingObsidian");
        Block.blocksList[203] = glowingObsidian;
        Item.itemsList[203] = new ItemBlock(203 - 256);

        netherReactor = new NetherReactor(204, Material.rock).setBlockName("netherReactor");
        Block.blocksList[204] = netherReactor;
        Item.itemsList[204] = new ItemBlock(204 - 256);

        rubyOre = new RubyOre(205, 111).setBlockName("rubyOre");
        Block.blocksList[205] = rubyOre;
        Item.itemsList[205] = new ItemBlock(205 - 256);

        gear = new Gear(206, Material.iron).setBlockName("gear");
        Block.blocksList[206] = gear;
        Item.itemsList[206] = new ItemGear(206 - 256);

        stonecutter = new Stonecutter(207).setBlockName("stonecutter");
        Block.blocksList[207] = stonecutter;
        Item.itemsList[207] = new ItemBlock(207 - 256);
        
        blueRose = new BlueRose(208, 30).setBlockName("blueRose");
        Block.blocksList[208] = blueRose;
        Item.itemsList[208] = new ItemBlock(208 - 256);

        dynyaStem = new CantaloupeStem(250, 125)
            .setBlockName("dynyaStem")
            .setHardness(0.0F);
        Block.blocksList[250] = dynyaStem;
        Item.itemsList[250] = new ItemBlock(250 - 256);
    }
}
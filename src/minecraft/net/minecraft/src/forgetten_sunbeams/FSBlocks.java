package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.Block;
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
    public static Block glowingObsidian;
    public static Block netherReactor;
    public static Block dynya;
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

        glowingObsidian = new GlowingObsidian(160, 127).setBlockName("glowingObsidian");
        Block.blocksList[160] = glowingObsidian;
        Item.itemsList[160] = new ItemBlock(160 - 256);

        netherReactor = new NetherReactor(161, Material.rock).setBlockName("netherReactor");
        Block.blocksList[161] = netherReactor;
        Item.itemsList[161] = new ItemBlock(161 - 256) {
            public int getMetadata(int damage) {
                return 0;
            }
        };
        
        dynyaStem = new CantaloupeStem(250, 125)
            .setBlockName("dynyaStem")
            .setHardness(0.0F);
        Block.blocksList[250] = dynyaStem;
        Item.itemsList[250] = new ItemBlock(250 - 256);
    }
}
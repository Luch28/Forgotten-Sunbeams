package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forgetten_sunbeams.blocks.*;

public class mod_main extends BaseMod {

    public static final Block glassTrapDoor = new GlassTrapDoor(160, Material.glass)
            .setBlockName("glassTrapDoor");

    @Override
    public void ModsLoaded() {
        ModLoader.RegisterBlock(glassTrapDoor);
        
        ModLoader.AddName(glassTrapDoor, "Glass Trapdoor");
        
        ModLoader.AddRecipe(new ItemStack(glassTrapDoor, 2), new Object[] { 
            "GGG", 
            "GGG", 
            Character.valueOf('G'), Block.glass 
        });
    }

    @Override
    public String Version() {
        return "0.0.0.1";
    }
}

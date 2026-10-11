package net.minecraft.src;

import net.minecraft.src.forgetten_sunbeams.blocks.*;

public class mod_main extends BaseModMp {

    public static final Block glassTrapDoor = new GlassTrapDoor(160, Material.glass)
            .setBlockName("glassTrapDoor");

    @Override
    public void ModsLoaded() {
        ModLoader.RegisterBlock(glassTrapDoor);
        
        ModLoader.AddRecipe(new ItemStack(glassTrapDoor, 2), new Object[] { 
            "GGG", 
            "GGG", 
            Character.valueOf('G'), Block.glass 
        });

        ModLoaderMp.RegisterEntityTrackerEntry(GlassTrapDoor.class, 160);
        ModLoaderMp.RegisterEntityTracker(GlassTrapDoor.class, 160, 5);
    }

    @Override
    public String Version() {
        return "0.0.0.1";
    }
}


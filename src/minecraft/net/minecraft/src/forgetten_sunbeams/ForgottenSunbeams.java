package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.event.*;
import net.minecraft.src.forgetten_sunbeams.utils.StonecutterRecipes;
import net.minecraft.src.forgetten_sunbeams.utils.TileEntityStonecutter;

public class ForgottenSunbeams {
    public static final String VERSION = "0.0.0.2";

    public static void init() {
        TileEntity.addMapping(TileEntityStonecutter.class, "Stonecutter");

        FSBlocks.init();
        FSItems.init();
        FSRecipes.init();
        StonecutterRecipes.registerDefaults();
        System.out.println("Forgotten Sunbeams");
    }

    public static void onWorldTick(World world) {
        NetherReactorAltar.onWorldTick(world);
    }
}
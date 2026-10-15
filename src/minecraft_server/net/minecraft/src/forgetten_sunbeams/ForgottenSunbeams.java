package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.event.*;

public class ForgottenSunbeams {
    public static final String VERSION = "0.0.0.2";

    public static void init() {
        FSBlocks.init();
        FSItems.init();
        FSRecipes.init();
        System.out.println("Forgotten Sunbeams");
    }

    public static void onWorldTick(World world) {
        SnowyLeavesHandler.onWorldTick(world);
        NetherReactorAltar.onWorldTick(world);
    }
}
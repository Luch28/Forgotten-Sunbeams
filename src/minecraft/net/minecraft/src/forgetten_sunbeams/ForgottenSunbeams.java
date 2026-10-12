package net.minecraft.src.forgetten_sunbeams;

public class ForgottenSunbeams {
    public static final String VERSION = "0.0.0.2";
    
    public static void init() {
        FSBlocks.init();
        FSItems.init();
        FSRecipes.init();
        System.out.println("Forgotten Sunbeams");
    }
}
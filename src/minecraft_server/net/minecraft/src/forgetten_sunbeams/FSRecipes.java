package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Block;
import net.minecraft.src.Item;

public class FSRecipes {
    public static void init() {
        CraftingManager.getInstance().addRecipe(
            new ItemStack(FSBlocks.glassTrapDoor, 2), 
            new Object[] { 
                "GGG", 
                "GGG", 
                Character.valueOf('G'), Block.glass 
            }
        );
        
        CraftingManager.getInstance().addRecipe(
        	    new ItemStack(FSBlocks.glassDoor, 1),
        	    new Object[] {
        	        "GG ",
        	        "GG ",
        	        "GG ",
        	        Character.valueOf('g'), Block.glass
        	    }
        	);
        
        CraftingManager.getInstance().addRecipe(
            new ItemStack(FSItems.quiver, 1), 
            new Object[] { 
                "  S", 
                "L L", 
                "SLL", 
                Character.valueOf('L'), Item.leather,
                Character.valueOf('S'), Item.silk
            }
        );
        
        CraftingManager.getInstance().addRecipe(
        	    new ItemStack(FSBlocks.dynya, 1),
        	    new Object[] {
        	        "DDD",
        	        "DDD",
        	        "DDD",
        	        Character.valueOf('D'), FSItems.dynyaSlice
        	    }
        	);
        
        System.out.println("Recipes registered!");
    }
}
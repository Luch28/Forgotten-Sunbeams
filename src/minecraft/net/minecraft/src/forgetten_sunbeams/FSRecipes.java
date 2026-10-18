package net.minecraft.src.forgetten_sunbeams;

import net.minecraft.src.BlockFlower;
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
            new ItemStack(FSBlocks.glassDoor, 3),
            new Object[] {
                "GG ",
                "GG ",
                "GG ",
                Character.valueOf('G'), Block.glass
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
        
        CraftingManager.getInstance().addRecipe(
                new ItemStack(FSBlocks.netherReactor, 1),
                new Object[] {
                    "IDI",
                    "IGI",
                    "IDI",
                    Character.valueOf('I'), Item.ingotIron,
                    Character.valueOf('D'), Item.diamond,
                    Character.valueOf('G'), Item.ingotGold,
                }
            );
        CraftingManager.getInstance().addRecipe(
                new ItemStack(FSItems.dynyaSeeds, 1),
                new Object[] {
                    "S",
                    Character.valueOf('S'), FSItems.dynyaSlice
                }
            );
        
        CraftingManager.getInstance().addRecipe(
                new ItemStack(FSBlocks.stonecutter, 1),
                new Object[] {
                    "CC",
                    "SS",
                    Character.valueOf('C'), Block.cobblestone,
                    Character.valueOf('S'), Block.stone
                }
            );
        
        CraftingManager.getInstance().addRecipe(
                new ItemStack(FSBlocks.dynya, 1),
                new Object[] {
                    "RRR",
                    "RRR",
                    "RRR",
                    Character.valueOf('R'), FSItems.ruby                }
            );
        
        CraftingManager.getInstance().addRecipe(
                new ItemStack(FSBlocks.blueRose, 1),
                new Object[] {
                    "B",
                    Character.valueOf('F'), Block.dirt
                }
            );

        System.out.println("Recipes registered!");
    }
}
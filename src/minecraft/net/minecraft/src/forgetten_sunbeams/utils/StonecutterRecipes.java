package net.minecraft.src.forgetten_sunbeams.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Block;

public class StonecutterRecipes {

    public static class Recipe {
        public final ItemStack input;
        public final ItemStack[] outputs;

        public Recipe(ItemStack input, ItemStack... outputs) {
            this.input = input;
            this.outputs = outputs;
        }
    }

    private static final List<Recipe> recipes = new ArrayList<Recipe>();

    public static void addRecipe(ItemStack input, ItemStack... outputs) {
        recipes.add(new Recipe(input, outputs));
    }

    public static Recipe findRecipe(ItemStack input) {
        if (input == null) return null;
        for (Recipe r : recipes) {
            if (r.input.itemID == input.itemID && r.input.getItemDamage() == input.getItemDamage()) {
                return r;
            }
        }
        return null;
    }

    public static void registerDefaults() {
        addRecipe(new ItemStack(Block.cobblestone, 1),
                new ItemStack(Block.stairSingle, 1, 0),
                new ItemStack(Block.stone, 1));

        addRecipe(new ItemStack(Block.stone, 1),
                new ItemStack(Block.stairSingle, 1, 3),
                new ItemStack(Block.stairSingle, 1, 3),
                new ItemStack(Block.stairSingle, 1, 3),
                new ItemStack(Block.stairSingle, 1, 3),
                new ItemStack(Block.stairSingle, 1, 3),
                new ItemStack(Block.stairSingle, 1, 3));
    }
}
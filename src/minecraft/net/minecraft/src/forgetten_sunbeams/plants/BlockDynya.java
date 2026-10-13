package net.minecraft.src.forgetten_sunbeams.plants;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class BlockDynya extends Block {

    public BlockDynya(int id, int textureIndex) {
        super(id, textureIndex, Material.pumpkin);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundWoodFootstep);
        this.setBlockName("dynya");
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if (side == 0 || side == 1) {
            return 136;
        }
        return 137;
    }

    public int idDropped(int meta, Random random, int fortune) {
        return FSItems.dynyaSeeds.shiftedIndex;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1 + random.nextInt(3);
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }
}
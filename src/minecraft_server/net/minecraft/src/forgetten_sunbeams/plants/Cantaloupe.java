// Cantaloupe.java
package net.minecraft.src.forgetten_sunbeams.plants;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.FSBlocks;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class Cantaloupe extends Block {

    public Cantaloupe(int id, int textureIndex) {
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

    public int idDropped(int meta, Random random) {
        return FSItems.dynyaSlice.shiftedIndex;
    }

    @Override
    public int quantityDropped(Random random) {
        return 3 + random.nextInt(5);
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }
                int bx = x + dx;
                int bz = z + dz;
                if (world.getBlockId(bx, y, bz) == FSBlocks.dynyaStem.blockID) {
                    int stemMeta = world.getBlockMetadata(bx, y, bz);
                    if (stemMeta >= 6) {
                        world.setBlockMetadataWithNotify(bx, y, bz, 5);
                    }
                }
            }
        }
        super.onBlockRemoval(world, x, y, z);
    }
}
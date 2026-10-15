package net.minecraft.src.forgetten_sunbeams.blocks;

import java.util.Random;

import net.minecraft.src.BlockDoor;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.FSBlocks;

public class GlassDoor extends BlockDoor {

    public GlassDoor(int id, Material material) {
        super(id, material);
        this.setHardness(0.3F);
        this.setResistance(0.5F);
        this.setStepSound(soundGlassFootstep);
        this.setBlockName("glassDoor");
        this.setLightOpacity(0);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        return super.blockActivated(world, x, y, z, player);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if ((meta & 8) != 0) {
            return 85;
        }
        return 101;
    }

    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockAndMetadataWithNotify(x, y + 1, z, this.blockID, meta | 8);
    }

    public int idDropped(int meta, Random random, int fortune) {
        return 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if ((meta & 8) == 0 && !world.singleplayerWorld) {
            float f = 0.7F;
            double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d, (double)y + d1, (double)z + d2, new ItemStack(FSBlocks.glassDoor));
            entityitem.delayBeforeCanPickup = 10;
            world.entityJoinedWorld(entityitem);
        }
        super.onBlockRemoval(world, x, y, z);
    }
}
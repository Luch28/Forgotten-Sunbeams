package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.BlockDoor;
import net.minecraft.src.BlockTrapDoor;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class GlassDoor extends BlockDoor {

    public GlassDoor(int id, Material material) {
        super(id, material);
        this.setHardness(0.3F);
        this.setResistance(0.5F);
        this.setStepSound(soundGlassFootstep);
    }
    
    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        boolean activated = super.blockActivated(world, x, y, z, player);
        if (activated) {
            int meta = world.getBlockMetadata(x, y, z);
            if ((meta & 8) == 0) {
                world.markBlockNeedsUpdate(x, y + 1, z);
            } else {
                world.markBlockNeedsUpdate(x, y - 1, z);
            }
            world.markBlockNeedsUpdate(x, y, z);
        }
        return activated;
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
            return 49;
        }
        return 48;
    }

    public int getRenderBlockPass() {
        return 1;
    }
}


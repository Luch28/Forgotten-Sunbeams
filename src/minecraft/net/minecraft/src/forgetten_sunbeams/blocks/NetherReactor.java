package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.forgetten_sunbeams.event.NetherReactorAltar;

public class NetherReactor extends Block {
    public static final int STATE_1 = 0;
    public static final int STATE_2 = 1;
    public static final int STATE_3 = 2;

    public NetherReactor(int id, Material material) {
        super(id, material);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setStepSound(soundMetalFootstep);
        this.setBlockName("netherReactor");
        this.setLightOpacity(0);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.getBlockMetadata(x, y, z) == STATE_1) {
            NetherReactorAltar.tryActivate(world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        return 141;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        switch (meta) {
            case STATE_2: return 142;
            case STATE_3: return 143;
            default: return 141;
        }
    }

    @Override
    public int damageDropped(int meta) {
        return STATE_1;
    }

    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta == STATE_2 ? 12 : 0;
    }
}

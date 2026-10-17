package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.forgetten_sunbeams.gui.GuiStonecutter;
import net.minecraft.src.forgetten_sunbeams.utils.TileEntityStonecutter;

public class Stonecutter extends BlockContainer {

    public Stonecutter(int id) {
        super(id, Material.rock);
        this.setHardness(3.5F);
        this.setResistance(3.5F);
        this.setStepSound(soundStoneFootstep);
        this.setBlockName("Stonecutter");
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) return 175;
        if (side == 0) return 62;
        if (side == 3) return 174;
        return 45;
    }

    @Override
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        if (side == 1) return 175;
        if (side == 0) return 62;
        int metadata = world.getBlockMetadata(x, y, z);
        if (side == metadata) return 174;
        return 45;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2);
        if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5);
        if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3);
        if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4);
    }

    @Override
    public TileEntity getBlockEntity() {
        return new TileEntityStonecutter();
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.multiplayerWorld) {
            TileEntityStonecutter tile = (TileEntityStonecutter) world.getBlockTileEntity(x, y, z);
            if (tile != null) {
                try {
                    java.lang.reflect.Field f = Minecraft.class.getDeclaredField("theMinecraft");
                    f.setAccessible(true);
                    Minecraft mc = (Minecraft) f.get(null);
                    mc.displayGuiScreen(new GuiStonecutter(player.inventory, tile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntityStonecutter tile = (TileEntityStonecutter) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
            ItemStack stack = tile.getStackInSlot(0);
            if (stack != null) {
                float fx = world.rand.nextFloat() * 0.8F + 0.1F;
                float fy = world.rand.nextFloat() * 0.8F + 0.1F;
                float fz = world.rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem = new EntityItem(world,
                    (double)((float)x + fx),
                    (double)((float)y + fy),
                    (double)((float)z + fz),
                    stack);
                float f3 = 0.05F;
                entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
                world.entityJoinedWorld(entityitem);
            }
        }
        super.onBlockRemoval(world, x, y, z);
    }
}
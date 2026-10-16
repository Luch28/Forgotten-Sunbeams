package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.gear.TileEntityGear;

public class Gear extends BlockContainer {
    public Gear(int id, Material mat) {
        super(id, mat);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundMetalFootstep);
        this.setBlockName("gear");
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    public static boolean isCorner(int meta) {
        return meta >= 6 && meta <= 9;
    }

    private AxisAlignedBB getGearBounds(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        float t = 0.0625F;
        float f = 0.5F;

        if (isCorner(meta)) {
            return AxisAlignedBB.getBoundingBox(
                x + 0.0F, y + 0.0F, z + 0.0F,
                x + 1.0F, y + 1.0F, z + 1.0F
            );
        }

        if (meta == 2) return AxisAlignedBB.getBoundingBox(x + 0.5F - f, y + 0.5F - f, z + 1.0F - t, x + 0.5F + f, y + 0.5F + f, z + 1.0F);
        if (meta == 3) return AxisAlignedBB.getBoundingBox(x + 0.5F - f, y + 0.5F - f, z, x + 0.5F + f, y + 0.5F + f, z + t);
        if (meta == 4) return AxisAlignedBB.getBoundingBox(x + 1.0F - t, y + 0.5F - f, z + 0.5F - f, x + 1.0F, y + 0.5F + f, z + 0.5F + f);
        if (meta == 5) return AxisAlignedBB.getBoundingBox(x, y + 0.5F - f, z + 0.5F - f, x + t, y + 0.5F + f, z + 0.5F + f);
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0F, y + 1.0F, z + 1.0F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return getGearBounds(world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isBlockNormalCube(x - 1, y, z)
            || world.isBlockNormalCube(x + 1, y, z)
            || world.isBlockNormalCube(x, y, z - 1)
            || world.isBlockNormalCube(x, y, z + 1);
    }

    public int determineMetadata(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        boolean wallNorth = world.isBlockNormalCube(x, y, z - 1); // z-
        boolean wallSouth = world.isBlockNormalCube(x, y, z + 1); // z+
        boolean wallWest  = world.isBlockNormalCube(x - 1, y, z); // x-
        boolean wallEast  = world.isBlockNormalCube(x + 1, y, z); // x+

        if (side == 2 && wallSouth) return 2;
        if (side == 3 && wallNorth) return 3;
        if (side == 4 && wallEast)  return 4;
        if (side == 5 && wallWest)  return 5;

        boolean westSide  = hitX < 0.5F;
        boolean eastSide  = hitX >= 0.5F;
        boolean northSide = hitZ < 0.5F;
        boolean southSide = hitZ >= 0.5F;

        boolean canNW = wallNorth && wallWest;
        boolean canNE = wallNorth && wallEast;
        boolean canSW = wallSouth && wallWest;
        boolean canSE = wallSouth && wallEast;

        if (canNW && northSide && westSide) return 6;
        if (canNE && northSide && eastSide) return 7;
        if (canSW && southSide && westSide) return 8;
        if (canSE && southSide && eastSide) return 9;

        if (canNW) return 6;
        if (canNE) return 7;
        if (canSW) return 8;
        if (canSE) return 9;

        if (wallSouth) return 2;
        if (wallNorth) return 3;
        if (wallEast)  return 4;
        if (wallWest)  return 5;

        return -1;
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta < 6) {
            if (side == 2 && world.isBlockNormalCube(x, y, z + 1)) meta = 2;
            if (side == 3 && world.isBlockNormalCube(x, y, z - 1)) meta = 3;
            if (side == 4 && world.isBlockNormalCube(x + 1, y, z)) meta = 4;
            if (side == 5 && world.isBlockNormalCube(x - 1, y, z)) meta = 5;
            world.setBlockMetadataWithNotify(x, y, z, meta);
        }
        updateGearNetwork(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborId) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean ok = false;

        if (meta == 2 && world.isBlockNormalCube(x, y, z + 1)) ok = true;
        if (meta == 3 && world.isBlockNormalCube(x, y, z - 1)) ok = true;
        if (meta == 4 && world.isBlockNormalCube(x + 1, y, z)) ok = true;
        if (meta == 5 && world.isBlockNormalCube(x - 1, y, z)) ok = true;

        if (isCorner(meta)) {
            boolean wallNorth = world.isBlockNormalCube(x, y, z - 1);
            boolean wallSouth = world.isBlockNormalCube(x, y, z + 1);
            boolean wallWest  = world.isBlockNormalCube(x - 1, y, z);
            boolean wallEast  = world.isBlockNormalCube(x + 1, y, z);
            if (meta == 6 && wallNorth && wallWest) ok = true;
            if (meta == 7 && wallNorth && wallEast) ok = true;
            if (meta == 8 && wallSouth && wallWest) ok = true;
            if (meta == 9 && wallSouth && wallEast) ok = true;
        }

        if (!ok) {
            this.dropBlockAsItem(world, x, y, z, meta);
            world.setBlockWithNotify(x, y, z, 0);
            return;
        }
        updateGearNetwork(world, x, y, z);
    }

    private void updateGearNetwork(World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (!(te instanceof TileEntityGear)) return;
        TileEntityGear gear = (TileEntityGear) te;
        gear.direction = 1;

        TileEntityGear neighbor = findMeshingGear(world, x, y, z);
        if (neighbor != null) {
            gear.direction = -neighbor.direction;
        }
    }

    private TileEntityGear findMeshingGear(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);

        if (isCorner(meta)) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        int nx = x + dx, ny = y + dy, nz = z + dz;
                        if (world.getBlockId(nx, ny, nz) == this.blockID) {
                            TileEntity nte = world.getBlockTileEntity(nx, ny, nz);
                            if (nte instanceof TileEntityGear) return (TileEntityGear) nte;
                        }
                    }
                }
            }
            return null;
        }

        if (meta == 2 || meta == 3) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = x + dx, ny = y + dy, nz = z;
                    if (world.getBlockId(nx, ny, nz) == this.blockID) {
                        TileEntity nte = world.getBlockTileEntity(nx, ny, nz);
                        if (nte instanceof TileEntityGear) return (TileEntityGear) nte;
                    }
                }
            }
        } else if (meta == 4 || meta == 5) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dz == 0 && dy == 0) continue;
                    int nx = x, ny = y + dy, nz = z + dz;
                    if (world.getBlockId(nx, ny, nz) == this.blockID) {
                        TileEntity nte = world.getBlockTileEntity(nx, ny, nz);
                        if (nte instanceof TileEntityGear) return (TileEntityGear) nte;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public TileEntity getBlockEntity() {
        return new TileEntityGear();
    }
}
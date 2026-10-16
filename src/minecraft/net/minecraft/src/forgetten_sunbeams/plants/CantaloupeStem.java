package net.minecraft.src.forgetten_sunbeams.plants;

import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.forgetten_sunbeams.FSBlocks;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class CantaloupeStem extends BlockCrops {

    private static final String LOG = "[DynyaStem] ";

    public CantaloupeStem(int id, int textureIndex) {
        super(id, textureIndex);
        this.setStepSound(Block.soundGrassFootstep);
        this.setBlockName("dynyaStem");
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if (meta >= 6) {
            return 159;
        }
        return 153 + meta;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);

        int meta = world.getBlockMetadata(x, y, z);

        int light = world.getBlockLightValue(x, y + 1, z);
        if (light < 9) {
            return;
        }

        if (meta < 6) {
            float growthRate = this.getGrowthRate(world, x, y, z);
            if (random.nextInt((int)(2500.0F / growthRate) + 1) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1);
            }
            return;
        }

        if (random.nextInt(5) != 0) {
            return;
        }

        tryGrowFruit(world, x, y, z, random);
    }

    private boolean tryGrowFruit(World world, int x, int y, int z, Random random) {
        int[] directions = {2, 3, 4, 5};

        for (int i = 0; i < 4; i++) {
            int dir = directions[i];
            int dx = x;
            int dz = z;

            switch (dir) {
                case 2: dz--; break;
                case 3: dz++; break;
                case 4: dx--; break;
                case 5: dx++; break;
            }

            if (!world.isAirBlock(dx, y, dz)) {
                continue;
            }

            int groundId = world.getBlockId(dx, y - 1, dz);
            if (groundId != Block.dirt.blockID
                    && groundId != Block.grass.blockID
                    && groundId != Block.tilledField.blockID) {
                continue;
            }

            world.setBlockWithNotify(dx, y, dz, FSBlocks.dynya.blockID);
            world.setBlockMetadataWithNotify(x, y, z, 6 | (dir << 3));
            System.out.println(LOG + "FRUIT PLACED at " + dx + "," + y + "," + dz
                    + " dir=" + dir + " stem meta=" + (6 | (dir << 3)));
            return true;
        }

        return false;
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        ItemStack held = player.inventory.getCurrentItem();
        if (held == null || held.itemID != Item.dyePowder.shiftedIndex) {
            return false;
        }
        if (held.getItemDamage() != 15) {
            return false;
        }

        int meta = world.getBlockMetadata(x, y, z);
        int stage = meta & 7;

        if (stage >= 6) {
            return false;
        }

        int add = 1 + world.rand.nextInt(2);
        int newStage = Math.min(6, stage + add);
        int newMeta = (meta & ~7) | newStage;
        world.setBlockMetadataWithNotify(x, y, z, newMeta);

        if (newStage >= 6) {
            tryGrowFruit(world, x, y, z, world.rand);
        }

        world.playSoundAtEntity(player, "random.pop", 0.3F, 1.2F);
        for (int i = 0; i < 6; i++) {
            world.spawnParticle("happyVillager",
                x + world.rand.nextDouble(),
                y + 0.5D + world.rand.nextDouble() * 0.5D,
                z + world.rand.nextDouble(),
                0.0D, 0.0D, 0.0D);
        }

        held.stackSize--;
        return true;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int idDropped(int meta, Random random) {
        return FSItems.dynyaSeeds.shiftedIndex;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int groundId = world.getBlockId(x, y - 1, z);
        return groundId == Block.tilledField.blockID;
    }

    private float getGrowthRate(World world, int x, int y, int z) {
        float f = 1.0F;
        int i = world.getBlockId(x, y, z - 1);
        int j = world.getBlockId(x, y, z + 1);
        int k = world.getBlockId(x - 1, y, z);
        int l = world.getBlockId(x + 1, y, z);
        int i1 = world.getBlockId(x - 1, y, z - 1);
        int j1 = world.getBlockId(x + 1, y, z - 1);
        int k1 = world.getBlockId(x - 1, y, z + 1);
        int l1 = world.getBlockId(x + 1, y, z + 1);

        boolean flag = k == this.blockID || l == this.blockID;
        boolean flag1 = i == this.blockID || j == this.blockID;
        boolean flag2 = i1 == this.blockID || j1 == this.blockID
                || k1 == this.blockID || l1 == this.blockID;

        for (int xo = -1; xo <= 1; ++xo) {
            for (int zo = -1; zo <= 1; ++zo) {
                int blockId = world.getBlockId(x + xo, y - 1, z + zo);
                float f1 = 0.0F;

                if (blockId == Block.tilledField.blockID) {
                    f1 = 1.0F;
                    if (world.getBlockMetadata(x + xo, y - 1, z + zo) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (xo != 0 || zo != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || (flag && flag1)) {
            f /= 2.0F;
        }

        return f;
    }

    public int colorMultiplier(World world, int x, int y, int z) {
        return ColorizerGrass.getGrassColor(0.7, 0.8);
    }
}
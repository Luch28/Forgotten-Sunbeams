package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.BlockLeaves;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class SnowyLeaves extends BlockLeaves {

    public SnowyLeaves(int id, int textureIndex) {
        super(id, textureIndex);
    }

    @Override
    public int colorMultiplier(IBlockAccess iba, int x, int y, int z) {
        if (iba instanceof World) {
            World world = (World) iba;
            if (world.getWorldInfo().getRaining()) {
                float snowProgress = getSnowProgress(world);
                if (snowProgress >= 1.0F) {
                    return 16777215;
                }
                int baseColor = super.colorMultiplier(iba, x, y, z);
                return lerpColor(baseColor, 16777215, snowProgress);
            }
        }
        return super.colorMultiplier(iba, x, y, z);
    }

    private float getSnowProgress(World world) {
        long time = world.getWorldTime() % 24000L;
        if (time >= 12000L && time < 13000L) {
            return (float)(time - 12000L) / 1000.0F;
        }
        if (time >= 13000L && time < 23000L) {
            return 1.0F;
        }
        if (time >= 23000L && time < 24000L) {
            return 1.0F - (float)(time - 23000L) / 1000.0F;
        }
        return 0.0F;
    }

    private int lerpColor(int from, int to, float t) {
        int fr = (from >> 16) & 0xFF;
        int fg = (from >> 8) & 0xFF;
        int fb = from & 0xFF;
        int tr = (to >> 16) & 0xFF;
        int tg = (to >> 8) & 0xFF;
        int tb = to & 0xFF;

        int r = (int)(fr + (tr - fr) * t);
        int g = (int)(fg + (tg - fg) * t);
        int b = (int)(fb + (tb - fb) * t);

        return (r << 16) | (g << 8) | b;
    }
}
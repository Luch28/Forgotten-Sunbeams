package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.BlockLeaves;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.BiomeGenBase;

public class SnowyLeaves extends BlockLeaves {

    public SnowyLeaves(int id, int textureIndex) {
        super(id, textureIndex);
    }

    @Override
    public int colorMultiplier(IBlockAccess iba, int x, int y, int z) {
        if (iba instanceof World) {
            World world = (World) iba;
            WorldInfo info = world.getWorldInfo();
            if (info.getRaining()) {
                BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(x, z);
                if (biome != null && biome.enableSnow) {
                    float t = getSnowProgress(info);
                    t = Math.round(t * 4.0F) / 4.0F;
                    if (t > 0.0F) {
                        int base = super.colorMultiplier(iba, x, y, z);
                        return lerpColor(base, 0xFFFFFF, t);
                    }
                }
            }
        }
        return super.colorMultiplier(iba, x, y, z);
    }

    private float getSnowProgress(WorldInfo info) {
        int rainTime = info.getRainTime();
        float t = 1.0F - Math.min(rainTime, 6000) / 6000.0F;
        if (t < 0.0F) t = 0.0F;
        if (t > 1.0F) t = 1.0F;
        return t;
    }

    private int lerpColor(int from, int to, float t) {
        int fr = (from >> 16) & 0xFF, fg = (from >> 8) & 0xFF, fb = from & 0xFF;
        int tr = (to >> 16) & 0xFF, tg = (to >> 8) & 0xFF, tb = to & 0xFF;
        int r = (int)(fr + (tr - fr) * t);
        int g = (int)(fg + (tg - fg) * t);
        int b = (int)(fb + (tb - fb) * t);
        return (r << 16) | (g << 8) | b;
    }
}
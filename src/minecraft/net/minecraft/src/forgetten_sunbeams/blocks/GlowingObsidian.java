package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.BlockObsidian;
import net.minecraft.src.Material;

public class GlowingObsidian extends BlockObsidian{

	public GlowingObsidian(int id, int material) {
        super(id, material);
        this.setHardness(50.0F);
        this.setResistance(24000.0F);
        this.setStepSound(soundStoneFootstep);
        this.setBlockName("glowingObsidian");
        this.setLightOpacity(1);
    }
	
	@Override
    public int getBlockTextureFromSide(int side) {
        return 127; 
    }

}

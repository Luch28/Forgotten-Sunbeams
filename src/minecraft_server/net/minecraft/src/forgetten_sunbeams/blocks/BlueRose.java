package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.BlockFlower;

public class BlueRose extends BlockFlower{

	public BlueRose(int i, int j) {
		super(i, j);
		this.setHardness(0.0F);
        this.setResistance(0.0F);
        this.setStepSound(soundGrassFootstep);
        this.setBlockName("blueRose");
	}
	
	public int getMetadata(int damage) {
        return 30;
    }

}

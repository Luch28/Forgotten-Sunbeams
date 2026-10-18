package net.minecraft.src.forgetten_sunbeams.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

public class Ruby extends Block{

	public Ruby(int id, Material material) {
		super(id, material);
		this.setHardness(1.0F);
        this.setResistance(3.0F);
        this.setStepSound(soundStoneFootstep);
        this.setBlockName("ruby");
	}
	
	@Override
    public int getBlockTextureFromSide(int side) {
        return 126; 
    }
	
	@Override
    public int quantityDropped(Random random) {
        return 1;
    }
}

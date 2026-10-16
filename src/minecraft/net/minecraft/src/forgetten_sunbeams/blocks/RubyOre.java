package net.minecraft.src.forgetten_sunbeams.blocks;

import java.util.Random;

import net.minecraft.src.BlockOre;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class RubyOre extends BlockOre{

	public RubyOre(int id, int material) {
		super(id, material);
		this.setHardness(2.0F);
        this.setResistance(4.0F);
        this.setStepSound(soundStoneFootstep);
        this.setBlockName("rubyOre");
	}
	
	@Override
    public int getBlockTextureFromSide(int side) {
        return 111; 
    }
	
	@Override
    public int quantityDropped(Random random) {
        return 1;
    }
	
	public int idDropped(int meta, Random random) {
        return FSItems.ruby.shiftedIndex;
    }

}

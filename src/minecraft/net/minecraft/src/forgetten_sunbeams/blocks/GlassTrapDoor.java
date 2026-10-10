package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.Material;

public class GlassTrapDoor {
	protected void BlockCustomTrapDoor(int id, Material material) {
        super(id, material);
        
        setHardness(3.0F);
        setResistance(5.0F);
		setStepSound(soundWoodFootstep);
        setBlockName("Glass Trap Door");
    }

	public int getBlockTextureFromSide(int side) {
        return 49; 
    }
}

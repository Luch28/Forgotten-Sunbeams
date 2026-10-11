package net.minecraft.src.forgetten_sunbeams.blocks;

import net.minecraft.src.BlockTrapDoor;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class GlassTrapDoor extends BlockTrapDoor {

    public GlassTrapDoor(int id, Material material) {
        super(id, material);
        this.setHardness(0.3F);
        this.setResistance(0.5F);
        this.setStepSound(soundGlassFootstep); 
    }
    
    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        boolean activated = super.blockActivated(world, x, y, z, player);
        if (activated) {
            world.markBlockNeedsUpdate(x, y, z); 
        }
        return activated;
    }
}

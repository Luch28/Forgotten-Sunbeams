package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.*;
import net.minecraft.src.forgetten_sunbeams.FSBlocks;

public class CantaloupeSeeds extends Item {

    private int plantedBlockId;

    public CantaloupeSeeds(int id, int plantedBlockId) {
        super(id);
        this.plantedBlockId = plantedBlockId;
        this.setMaxStackSize(64);
        this.setItemName("seedsDynya");
        this.setIconCoord(12, 8);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
                             int x, int y, int z, int side) {
        if (side != 1) {
            return false;
        }

        int groundId = world.getBlockId(x, y, z);
        if (groundId != Block.tilledField.blockID) {
            return false;
        }

        if (!world.isAirBlock(x, y + 1, z)) {
            return false;
        }

        world.setBlockWithNotify(x, y + 1, z, this.plantedBlockId);
        stack.stackSize--;
        return true;
    }
}
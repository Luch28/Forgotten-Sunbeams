package net.minecraft.src.forgetten_sunbeams.plants;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class Cantaloupe extends Block {

    public Cantaloupe(int id, int textureIndex) {
        super(id, textureIndex, Material.pumpkin);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundWoodFootstep);
        this.setBlockName("dynya");
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if (side == 0 || side == 1) {
            return 136;
        }
        return 137;
    }

    public int idDropped(int meta, Random random) {
        return FSItems.dynyaSlice.shiftedIndex;
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 3 + random.nextInt(5);
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
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

        int dropCount = 3 + world.rand.nextInt(5);
        for (int i = 0; i < dropCount; i++) {
            ItemStack slice = new ItemStack(FSItems.dynyaSlice, 1);
            if (!player.inventory.addItemStackToInventory(slice)) {
                player.dropPlayerItem(slice);
            }
        }

        for (int i = 0; i < 8; i++) {
            world.spawnParticle("happyVillager",
                x + world.rand.nextDouble(),
                y + 0.5D + world.rand.nextDouble() * 0.5D,
                z + world.rand.nextDouble(),
                0.0D, 0.0D, 0.0D);
        }

        world.playSoundAtEntity(player, "random.pop", 0.3F, 1.2F);

        held.stackSize--;

        return true;
    }
}
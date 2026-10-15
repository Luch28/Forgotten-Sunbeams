package net.minecraft.src;

import net.minecraft.src.forgetten_sunbeams.items.Quiver;

public class ItemBow extends Item {

    public ItemBow(int i) {
        super(i);
        this.maxStackSize = 1;
        this.setItemName("bow");
        this.setIconIndex(5);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer == null || entityplayer.inventory == null) return itemstack;

        if (!world.singleplayerWorld) {
            boolean hasQuiverArrow = false;
            boolean hasRegularArrow = false;

            if (Quiver.getArrows(Quiver.getQuiver(entityplayer)) > 0) {
                if (Quiver.consumeArrow(entityplayer)) {
                    hasQuiverArrow = true;
                }
            }

            if (!hasQuiverArrow) {
                hasRegularArrow = entityplayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex);
            }

            if (hasQuiverArrow || hasRegularArrow) {
                world.playSoundAtEntity(entityplayer, "random.bow", 1.0F,
                    1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                world.entityJoinedWorld(new EntityArrow(world, entityplayer));
            }
        } else {
            boolean hasQuiverArrow = Quiver.getArrows(Quiver.getQuiver(entityplayer)) > 0;
            boolean hasRegularArrow = hasArrowInInventory(entityplayer);

            if (hasQuiverArrow || hasRegularArrow) {
                world.playSoundAtEntity(entityplayer, "random.bow", 1.0F,
                    1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
            }
        }

        return itemstack;
    }

    private static boolean hasArrowInInventory(EntityPlayer player) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && stack.itemID == Item.arrow.shiftedIndex) {
                return true;
            }
        }
        return false;
    }
}
package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.*;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class Quiver extends Item {

    public static final int MAX_CAPACITY = 32;

    public Quiver(int id) {
        super(id);
        this.setMaxStackSize(1);
        this.setMaxDamage(MAX_CAPACITY);
        this.setIconIndex(38);
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving attacker) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer == null || entityplayer.inventory == null) return itemstack;

        ItemStack heldItem = entityplayer.inventory.getCurrentItem();
        boolean holdingArrows = heldItem != null && heldItem.itemID == Item.arrow.shiftedIndex;

        if (holdingArrows) {
            int arrowsInQuiver = MAX_CAPACITY - itemstack.getItemDamage();
            int spaceLeft = MAX_CAPACITY - arrowsInQuiver;

            if (spaceLeft > 0) {
                int arrowsInHand = heldItem.stackSize;
                int amountToTransfer = entityplayer.isSneaking() ? Math.min(spaceLeft, arrowsInHand) : 1;

                if (amountToTransfer > 0) {
                    int newDamage = itemstack.getItemDamage() - amountToTransfer;
                    if (newDamage < 0) newDamage = 0;

                    itemstack.setItemDamage(newDamage);
                    heldItem.stackSize -= amountToTransfer;

                    if (heldItem.stackSize <= 0) {
                        entityplayer.inventory.setInventorySlotContents(
                            entityplayer.inventory.currentItem, null);
                    }
                    entityplayer.inventory.onInventoryChanged();
                    world.playSoundAtEntity(entityplayer, "random.pop", 0.2F, 1.0F);
                }
            }
            return itemstack;
        }

        if (entityplayer.isSneaking()) {
            int arrowsInQuiver = MAX_CAPACITY - itemstack.getItemDamage();
            if (arrowsInQuiver > 0) {
                ItemStack arrowStack = new ItemStack(Item.arrow, arrowsInQuiver);
                if (!entityplayer.inventory.addItemStackToInventory(arrowStack)) {
                    entityplayer.dropPlayerItem(arrowStack);
                }
                itemstack.setItemDamage(MAX_CAPACITY);
                entityplayer.inventory.onInventoryChanged();
                world.playSoundAtEntity(entityplayer, "random.pop", 0.2F, 1.0F);
            }
            return itemstack;
        }

        int chestSlot = 2;
        ItemStack currentChest = entityplayer.inventory.armorInventory[chestSlot];
        if (currentChest == null) {
            entityplayer.inventory.armorInventory[chestSlot] = itemstack.copy();
            entityplayer.inventory.setInventorySlotContents(
                entityplayer.inventory.currentItem, null);
            entityplayer.inventory.onInventoryChanged();
            world.playSoundAtEntity(entityplayer, "random.cloth", 0.2F, 1.0F);
            return null;
        }

        return itemstack;
    }

    public static ItemStack getQuiver(EntityPlayer player) {
        if (player == null || player.inventory == null) return null;

        if (player.inventory.armorInventory[2] != null
                && player.inventory.armorInventory[2].itemID == FSItems.quiver.shiftedIndex) {
            return player.inventory.armorInventory[2];
        }
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            if (player.inventory.mainInventory[i] != null
                    && player.inventory.mainInventory[i].itemID == FSItems.quiver.shiftedIndex) {
                return player.inventory.mainInventory[i];
            }
        }
        return null;
    }

    public static int getArrows(ItemStack quiver) {
        if (quiver == null) return 0;
        return MAX_CAPACITY - quiver.getItemDamage();
    }

    public static boolean consumeArrow(EntityPlayer player) {
        ItemStack quiver = getQuiver(player);
        if (quiver != null) {
            int arrows = MAX_CAPACITY - quiver.getItemDamage();
            if (arrows > 0) {
                quiver.setItemDamage(quiver.getItemDamage() + 1);
                if (player.inventory != null) {
                    player.inventory.onInventoryChanged();
                }
                return true;
            }
        }
        return false;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, java.util.List list, boolean advanced) {
        list.add(getArrows(stack) + " / " + MAX_CAPACITY);
    }
}
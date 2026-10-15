package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.*;
import net.minecraft.src.forgetten_sunbeams.FSItems;

public class Quiver extends ItemArmor {

    public static final int MAX_CAPACITY = 32;

    public Quiver(int id) {
        super(id, 0, 0, 1);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setIconIndex(38);
        this.setItemName("quiver");
    }

    public static int getArrows(ItemStack quiver) {
        if (quiver == null) return 0;
        int dmg = quiver.getItemDamage();
        int arrows = MAX_CAPACITY - dmg;
        if (arrows < 0) arrows = 0;
        if (arrows > MAX_CAPACITY) arrows = MAX_CAPACITY;
        return arrows;
    }

    public static void setArrows(ItemStack quiver, int amount) {
        if (quiver == null) return;
        if (amount < 0) amount = 0;
        if (amount > MAX_CAPACITY) amount = MAX_CAPACITY;
        quiver.setItemDamage(MAX_CAPACITY - amount);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer == null || entityplayer.inventory == null) return itemstack;
        if (entityplayer.isSneaking()) {
            int arrows = getArrows(itemstack);
            if (arrows > 0) {
                ItemStack arrowStack = new ItemStack(Item.arrow, arrows);
                if (!entityplayer.inventory.addItemStackToInventory(arrowStack)) {
                    entityplayer.dropPlayerItem(arrowStack);
                }
                setArrows(itemstack, 0);
                entityplayer.inventory.onInventoryChanged();
                world.playSoundAtEntity(entityplayer, "random.pop", 0.2F, 1.0F);
            }
            return itemstack;
        }

        int arrowsInQuiver = getArrows(itemstack);
        int spaceLeft = MAX_CAPACITY - arrowsInQuiver;

        if (spaceLeft > 0) {
            for (int i = 0; i < entityplayer.inventory.mainInventory.length; i++) {
                ItemStack slot = entityplayer.inventory.mainInventory[i];
                if (slot != null && slot.itemID == Item.arrow.shiftedIndex) {
                    slot.stackSize -= 1;
                    setArrows(itemstack, arrowsInQuiver + 1);
                    if (slot.stackSize <= 0) {
                        entityplayer.inventory.mainInventory[i] = null;
                    }
                    entityplayer.inventory.onInventoryChanged();
                    world.playSoundAtEntity(entityplayer, "random.pop", 0.2F, 1.0F);
                    return itemstack;
                }
            }
        }

        ItemStack currentChest = entityplayer.inventory.armorInventory[2];
        if (currentChest == null) {
            entityplayer.inventory.armorInventory[2] = itemstack.copy();
            entityplayer.inventory.setInventorySlotContents(
                entityplayer.inventory.currentItem, null);
            entityplayer.inventory.onInventoryChanged();
            world.playSoundAtEntity(entityplayer, "random.cloth", 0.2F, 1.0F);
            return itemstack;
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

    public static boolean consumeArrow(EntityPlayer player) {
        if (player == null || player.inventory == null) return false;

        ItemStack quiver = getQuiver(player);
        if (quiver != null) {
            int arrows = getArrows(quiver);
            if (arrows > 0) {
                setArrows(quiver, arrows - 1);
                player.inventory.onInventoryChanged();
                return true;
            }
            return false;
        }

        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack slot = player.inventory.mainInventory[i];
            if (slot != null && slot.itemID == Item.arrow.shiftedIndex) {
                slot.stackSize--;
                if (slot.stackSize <= 0) {
                    player.inventory.mainInventory[i] = null;
                }
                player.inventory.onInventoryChanged();
                return true;
            }
        }

        return false;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, java.util.List list, boolean advanced) {
        list.add(getArrows(stack) + " / " + MAX_CAPACITY);
    }
}
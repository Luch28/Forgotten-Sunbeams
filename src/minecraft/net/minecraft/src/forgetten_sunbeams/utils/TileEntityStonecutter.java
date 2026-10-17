package net.minecraft.src.forgetten_sunbeams.utils;

import net.minecraft.src.*;

public class TileEntityStonecutter extends TileEntity implements IInventory {

    private ItemStack[] inventory = new ItemStack[17];

    @Override
    public int getSizeInventory() { return inventory.length; }

    @Override
    public ItemStack getStackInSlot(int i) { return inventory[i]; }

    @Override
    public ItemStack decrStackSize(int i, int count) {
        if (i == 0) {
            if (inventory[0] == null) return null;
            ItemStack stack;
            if (inventory[0].stackSize <= count) {
                stack = inventory[0];
                inventory[0] = null;
            } else {
                stack = inventory[0].splitStack(count);
                if (inventory[0].stackSize == 0) inventory[0] = null;
            }
            onInventoryChanged();
            return stack;
        }

        if (i == 16) {
            if (inventory[16] == null) return null;
            if (inventory[0] == null) return null;
            ItemStack out = inventory[16].copy();
            inventory[16] = null;
            inventory[0].stackSize--;
            if (inventory[0].stackSize <= 0) inventory[0] = null;
            onInventoryChanged();
            return out;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack) {
        if (i != 0) return;
        inventory[0] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
        onInventoryChanged();
    }

    @Override
    public String getInvName() { return "Stonecutter"; }

    @Override
    public int getInventoryStackLimit() { return 64; }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
            && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64.0;
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        updateResults();
    }

    private void updateResults() {
        ItemStack selected = inventory[16];
        for (int i = 1; i < 16; i++) inventory[i] = null;

        ItemStack input = inventory[0];
        if (input == null) {
            inventory[16] = null;
            return;
        }

        StonecutterRecipes.Recipe r = StonecutterRecipes.findRecipe(input);
        if (r == null) {
            inventory[16] = null;
            return;
        }

        for (int i = 0; i < r.outputs.length && i < 15; i++) {
            inventory[1 + i] = r.outputs[i].copy();
        }

        if (selected != null) {
            boolean stillValid = false;
            for (int i = 0; i < r.outputs.length && i < 15; i++) {
                if (r.outputs[i].itemID == selected.itemID &&
                    r.outputs[i].getItemDamage() == selected.getItemDamage()) {
                    stillValid = true;
                    break;
                }
            }
            inventory[16] = stillValid ? selected : null;
        } else {
            inventory[16] = null;
        }
    }

    public void selectRecipe(int previewIndex) {
        if (previewIndex < 1 || previewIndex > 15) return;
        ItemStack preview = inventory[previewIndex];
        if (preview == null) return;
        inventory[16] = preview.copy();
        onInventoryChanged();
    }
}
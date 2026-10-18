package net.minecraft.src.forgetten_sunbeams.gear;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityGear extends TileEntity {
    public int rotation = 0;
    public int direction = 1;

    @Override
    public void updateEntity() {
        this.rotation = (this.rotation + this.direction) & 63;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.rotation = tag.getByte("rot") & 63;
        this.direction = tag.getByte("dir");
        if (this.direction == 0) {
            this.direction = 1;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("rot", (byte)this.rotation);
        tag.setByte("dir", (byte)this.direction);
    }
}
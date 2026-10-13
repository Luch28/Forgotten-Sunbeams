package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.ItemFood;

public class DynyaSlice extends ItemFood {

	public DynyaSlice(int id, int healAmount, boolean isWolfsFavorite) {
	    super(id, healAmount, isWolfsFavorite);
        this.setItemName("dynyaSlice");
        this.setMaxStackSize(64);
        this.setIconCoord(12, 9);
    }
}
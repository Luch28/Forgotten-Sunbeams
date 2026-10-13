package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.ItemFood;

public class CantaloupeSlice extends ItemFood {

	public CantaloupeSlice(int id, int healAmount, boolean isWolfsFavorite) {
	    super(id, healAmount, isWolfsFavorite);
        this.setItemName("dynyaSlice");
        this.setMaxStackSize(64);
        this.setIconCoord(12, 9);
    }
}
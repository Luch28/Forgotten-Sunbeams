package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.ItemCoal;

public class Ruby extends ItemCoal{

	public Ruby(int id) {
		super(id);
		this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setIconIndex(188);
        this.setItemName("ruby");
	}

}

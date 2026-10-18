package net.minecraft.src.forgetten_sunbeams.items;

import net.minecraft.src.*;

public class ItemGear extends ItemBlock {
    public ItemGear(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.iconIndex = 191;
    }

    public int getMetadata(int damage) {
        return 191;
    }

    public int getIconIndex(ItemStack stack) {
        return this.iconIndex;
    }

    @Override
    public String getItemName() {
        return "gear";
    }
}
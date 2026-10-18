package net.minecraft.src.forgetten_sunbeams.gui;

import net.minecraft.src.*;
import net.minecraft.src.forgetten_sunbeams.utils.TileEntityStonecutter;

import org.lwjgl.opengl.GL11;

class ContainerStonecutter extends Container {

    private TileEntityStonecutter tile;

    public ContainerStonecutter(InventoryPlayer playerInv, TileEntityStonecutter tile) {
        this.tile = tile;

        this.addSlot(new Slot(tile, 0, 15, 35));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                int index = 1 + row * 5 + col;
                this.addSlot(new Slot(tile, index, 44 + col * 18, 17 + row * 18) {
                    @Override
                    public boolean isItemValid(ItemStack stack) { return false; }
                    @SuppressWarnings("unused")
					public boolean canTakeStack(EntityPlayer player) { return false; }
                });
            }
        }

        this.addSlot(new Slot(tile, 16, 145, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) { return false; }
        });

        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));

        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return tile.canInteractWith(player);
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer player) {
        super.onCraftGuiClosed(player);
        ItemStack input = tile.getStackInSlot(0);
        if (input != null) {
            tile.setInventorySlotContents(0, null);
            if (!player.inventory.addItemStackToInventory(input)) {
                player.dropPlayerItem(input);
            }
        }
    }
}

public class GuiStonecutter extends GuiContainer {

    private TileEntityStonecutter tile;

    public GuiStonecutter(InventoryPlayer playerInv, TileEntityStonecutter tile) {
        super(new ContainerStonecutter(playerInv, tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Stonecutter", 62, 6, 0x404040);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks) {
        int tex = this.mc.renderEngine.getTexture("/gui/stonecutter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(tex);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        int slotU = 176;
        int slotV = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                int index = 1 + row * 5 + col;
                if (this.tile.getStackInSlot(index) == null) continue;

                int sx = x + 44 + col * 18 - 1;
                int sy = y + 17 + row * 18 - 1;
                this.drawTexturedModalRect(sx, sy, slotU, slotV, 18, 18);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (button != 0) return;

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                int index = 1 + row * 5 + col;
                int sx = x + 44 + col * 18;
                int sy = y + 17 + row * 18;

                if (mouseX >= sx && mouseX < sx + 16 && mouseY >= sy && mouseY < sy + 16) {
                    if (tile.getStackInSlot(index) != null) {
                        tile.selectRecipe(index);
                    }
                    return;
                }
            }
        }
    }
}
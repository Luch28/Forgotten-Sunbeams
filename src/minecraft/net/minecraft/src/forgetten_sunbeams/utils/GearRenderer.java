package net.minecraft.src.forgetten_sunbeams.utils;

import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import net.minecraft.src.forgetten_sunbeams.blocks.Gear;
import net.minecraft.src.forgetten_sunbeams.gear.TileEntityGear;

import org.lwjgl.opengl.GL11;

public class GearRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
        TileEntityGear gear = (TileEntityGear) te;
        World world = te.worldObj;
        int meta = world.getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);

        if (Gear.isCorner(meta)) {
            renderCornerGear(gear, world, te, x, y, z, partialTick, meta);
            return;
        }

        double offX = 0.0;
        double offZ = 0.0;
        float rotY = 0.0F;

        if (meta == 2) { offZ =  0.42; rotY = 180.0F; }
        if (meta == 3) { offZ = -0.42; rotY =   0.0F; }
        if (meta == 4) { offX =  0.42; rotY = -90.0F; }
        if (meta == 5) { offX = -0.42; rotY =  90.0F; }

        renderGearPlane(gear, world, te, x + 0.5 + offX, y + 0.5, z + 0.5 + offZ, rotY, partialTick);
    }

    private void renderCornerGear(TileEntityGear gear, World world, TileEntity te,
                                  double x, double y, double z, float partialTick, int meta) {
        float rotY1, rotY2;
        double offX1, offZ1, offX2, offZ2;

        switch (meta) {
            case 6:
                offX1 = 0; offZ1 = -0.42; rotY1 = 0.0F;
                offX2 = -0.42; offZ2 = 0; rotY2 = 90.0F;
                break;
            case 7:
                offX1 = 0; offZ1 = -0.42; rotY1 = 0.0F;
                offX2 = 0.42; offZ2 = 0; rotY2 = -90.0F;
                break;
            case 8:
                offX1 = 0; offZ1 = 0.42; rotY1 = 180.0F;
                offX2 = -0.42; offZ2 = 0; rotY2 = 90.0F;
                break;
            case 9:
                offX1 = 0; offZ1 = 0.42; rotY1 = 180.0F;
                offX2 = 0.42; offZ2 = 0; rotY2 = -90.0F;
                break;
            default:
                return;
        }

        renderGearPlane(gear, world, te, x + 0.5 + offX1, y + 0.5, z + 0.5 + offZ1, rotY1, partialTick);
        renderGearPlane(gear, world, te, x + 0.5 + offX2, y + 0.5, z + 0.5 + offZ2, rotY2, partialTick);
    }

    private void renderGearPlane(TileEntityGear gear, World world, TileEntity te,
                                 double px, double py, double pz, float rotY, float partialTick) {
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

        float light = world.getLightBrightness(te.xCoord, te.yCoord, te.zCoord);
        GL11.glColor3f(light, light, light);

        GL11.glTranslated(px, py, pz);
        GL11.glRotatef(rotY, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(0.6F, 0.6F, 0.6F);

        float angle = (gear.rotation + partialTick * gear.direction) / 64.0F * (float)Math.PI * 2.0F;
        float cos = (float)Math.cos(-angle);
        float sin = (float)Math.sin(-angle);

        Tessellator tess = Tessellator.instance;

        this.bindTextureByName("/misc/gear.png");

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        float px2 = 0.5F / 16.0F;
        float qx = 2.0F / 16.0F;
        float half = px2 * 0.5F;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                float cx = (i + 0.5F) / 16.0F - 0.5F;
                float cy = (j + 0.5F) / 16.0F - 0.5F;

                float rx = cos * cx - sin * cy;
                float ry = sin * cx + cos * cy;

                float ucRaw = rx + 0.5F;
                float vcRaw = ry + 0.5F;

                if (ucRaw < 0.0F || ucRaw > 1.0F || vcRaw < 0.0F || vcRaw > 1.0F) {
                    continue;
                }

                float uc = (float)Math.floor(rx * 32.0F + 16.0F) / 32.0F + half;
                float vc = (float)Math.floor(ry * 32.0F + 16.0F) / 32.0F + half;

                float u0 = uc - half;
                float u1 = uc + half;
                float v0 = vc - half;
                float v1 = vc + half;

                if (u0 < 0.0F || u1 > 1.0F || v0 < 0.0F || v1 > 1.0F) {
                    continue;
                }

                float x0 = -1.0F + i * qx;
                float x1 = x0 + qx;
                float y0 = -1.0F + j * qx;
                float y1 = y0 + qx;

                tess.startDrawingQuads();
                tess.addVertexWithUV(x0, y0, 0.0, u0, v1);
                tess.addVertexWithUV(x1, y0, 0.0, u1, v1);
                tess.addVertexWithUV(x1, y1, 0.0, u1, v0);
                tess.addVertexWithUV(x0, y1, 0.0, u0, v0);
                tess.draw();
            }
        }

        this.bindTextureByName("/misc/gear.png");
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                float cx = (i + 0.5F) / 16.0F - 0.5F;
                float cy = (j + 0.5F) / 16.0F - 0.5F;

                float rx = cos * cx - sin * cy;
                float ry = sin * cx + cos * cy;

                float ucRaw = rx + 0.5F;
                float vcRaw = ry + 0.5F;

                if (ucRaw < 0.0F || ucRaw > 1.0F || vcRaw < 0.0F || vcRaw > 1.0F) {
                    continue;
                }

                float uc = (float)Math.floor(rx * 32.0F + 16.0F) / 32.0F + half;
                float vc = (float)Math.floor(ry * 32.0F + 16.0F) / 32.0F + half;

                float u0 = uc - half;
                float u1 = uc + half;
                float v0 = vc - half;
                float v1 = vc + half;

                if (u0 < 0.0F || u1 > 1.0F || v0 < 0.0F || v1 > 1.0F) {
                    continue;
                }

                float x0 = -1.0F + i * qx;
                float x1 = x0 + qx;
                float y0 = -1.0F + j * qx;
                float y1 = y0 + qx;

                tess.startDrawingQuads();
                tess.addVertexWithUV(x0, y0, -0.01, u1, v1);
                tess.addVertexWithUV(x1, y0, -0.01, u0, v1);
                tess.addVertexWithUV(x1, y1, -0.01, u0, v0);
                tess.addVertexWithUV(x0, y1, -0.01, u1, v0);
                tess.draw();
            }
        }

        this.bindTextureByName("/misc/gearmiddle.png");
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        tess.startDrawingQuads();
        tess.addVertexWithUV(-1.0, -1.0, 0.01, 0.0, 1.0);
        tess.addVertexWithUV( 1.0, -1.0, 0.01, 1.0, 1.0);
        tess.addVertexWithUV( 1.0,  1.0, 0.01, 1.0, 0.0);
        tess.addVertexWithUV(-1.0,  1.0, 0.01, 0.0, 0.0);
        tess.draw();

        tess.startDrawingQuads();
        tess.addVertexWithUV(-1.0, -1.0, -0.02, 1.0, 1.0);
        tess.addVertexWithUV( 1.0, -1.0, -0.02, 0.0, 1.0);
        tess.addVertexWithUV( 1.0,  1.0, -0.02, 0.0, 0.0);
        tess.addVertexWithUV(-1.0,  1.0, -0.02, 1.0, 0.0);
        tess.draw();

        GL11.glPopMatrix();
    }
}
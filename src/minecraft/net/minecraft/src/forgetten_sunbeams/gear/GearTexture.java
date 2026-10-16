package net.minecraft.src.forgetten_sunbeams.gear;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.src.TextureFX;
import net.minecraft.src.forgetten_sunbeams.FSBlocks;

public class GearTexture extends TextureFX {
    private int rotation = 0;
    private int[] gearPixels = new int[1024];
    private int[] middlePixels = new int[1024];
    private int direction;

    public GearTexture(int variant) {
        super(FSBlocks.gear.blockIndexInTexture + variant);
        this.direction = variant * 2 - 1;
        this.rotation = 2;
        try {
            BufferedImage gear = ImageIO.read(GearTexture.class.getResource("/misc/gear.png"));
            gear.getRGB(0, 0, 32, 32, this.gearPixels, 0, 32);
            BufferedImage middle = ImageIO.read(GearTexture.class.getResource("/misc/gearmiddle.png"));
            middle.getRGB(0, 0, 16, 16, this.middlePixels, 0, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        this.rotation = this.rotation + this.direction & 63;
        float sin = (float)Math.sin((float)this.rotation / 64.0F * (float)Math.PI * 2.0F);
        float cos = (float)Math.cos((float)this.rotation / 64.0F * (float)Math.PI * 2.0F);

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                float fx = ((float)x / 15.0F - 0.5F) * 31.0F;
                float fy = ((float)y / 15.0F - 0.5F) * 31.0F;
                float rx = cos * fx - sin * fy;
                float ry = cos * fy + sin * fx;
                int px = (int)(rx + 16.0F);
                int py = (int)(ry + 16.0F);
                int color = 0;
                if (px >= 0 && py >= 0 && px < 32 && py < 32) {
                    color = this.gearPixels[px + py * 32];
                    int mid = this.middlePixels[x + y * 16];
                    if ((mid >> 24 & 255) > 128) color = mid;
                }
                int r = color >> 16 & 255;
                int g = color >> 8 & 255;
                int b = color & 255;
                int a = (color >> 24 & 255) > 128 ? 255 : 0;
                int idx = x + y * 16;
                this.imageData[idx * 4 + 0] = (byte)r;
                this.imageData[idx * 4 + 1] = (byte)g;
                this.imageData[idx * 4 + 2] = (byte)b;
                this.imageData[idx * 4 + 3] = (byte)a;
            }
        }
    }
}
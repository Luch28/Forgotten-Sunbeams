package net.minecraft.src.forgetten_sunbeams.serverlist;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;

public class GuiDirectConnect extends GuiScreen {
    private GuiScreen parentScreen;
    private GuiTextField ipField;

    public GuiDirectConnect(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @SuppressWarnings("unchecked")
	public void initGui() {
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, "Connect"));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, "Cancel"));
        ipField = new GuiTextField(parentScreen, fontRenderer, width / 2 - 100, height / 4 + 50, 200, 20, null);
        ipField.setText("");
        ipField.setMaxStringLength(128);
        ipField.setFocused(true);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) return;
        
        if (guibutton.id == 0) {
            String ip = ipField.getText().trim();
            if (ip.length() > 0) {
                String[] parts = ip.split(":");
                String address = parts[0];
                int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 25565;
                mc.displayGuiScreen(new GuiConnecting(mc, address, port));
            }
        } else if (guibutton.id == 1) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Direct Connect", width / 2, 20, 0xFFFFFF);
        drawString(fontRenderer, "Server Address:", width / 2 - 100, height / 4 + 38, 0xA0A0A0);
        ipField.drawTextBox();
        super.drawScreen(i, j, f);
    }

    protected void keyTyped(char c, int i) {
        ipField.textboxKeyTyped(c, i);
        if (c == '\r') {
            actionPerformed((GuiButton) controlList.get(0));
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        ipField.mouseClicked(i, j, k);
    }

    public void updateScreen() {
        ipField.updateCursorCounter();
    }
}
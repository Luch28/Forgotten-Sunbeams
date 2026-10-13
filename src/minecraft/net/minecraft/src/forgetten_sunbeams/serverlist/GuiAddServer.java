package net.minecraft.src.forgetten_sunbeams.serverlist;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;

public class GuiAddServer extends GuiScreen {
    private GuiScreen parentScreen;
    private GuiTextField nameField;
    private GuiTextField ipField;

    public GuiAddServer(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @SuppressWarnings("unchecked")
	public void initGui() {
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, "Add"));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, "Cancel"));
        nameField = new GuiTextField(parentScreen, fontRenderer, width / 2 - 100, height / 4 + 20, 200, 20, null);
        nameField.setMaxStringLength(32);
        nameField.setFocused(true);
        ipField = new GuiTextField(parentScreen, fontRenderer, width / 2 - 100, height / 4 + 70, 200, 20, null);
        ipField.setMaxStringLength(128);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 0) {
            String name = nameField.getText().trim();
            String ip = ipField.getText().trim();
            if (name.length() > 0 && ip.length() > 0) {
                ServerList serverList = new ServerList((net.minecraft.client.Minecraft) mc);
                serverList.addServer(name, ip);
                mc.displayGuiScreen(parentScreen);
            }
        } else if (guibutton.id == 1) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Add Server", width / 2, height / 4 + 10, 0xFFFFFF);
        drawString(fontRenderer, "Server Name:", width / 2 - 100, height / 4 + 8, 0xA0A0A0);
        drawString(fontRenderer, "Server Address:", width / 2 - 100, height / 4 + 58, 0xA0A0A0);
        nameField.drawTextBox();
        ipField.drawTextBox();
        super.drawScreen(i, j, f);
    }

    protected void keyTyped(char c, int i) {
        nameField.textboxKeyTyped(c, i);
        ipField.textboxKeyTyped(c, i);
        if (c == '\t') {
            nameField.setFocused(!nameField.getIsFocused());
            ipField.setFocused(!ipField.getIsFocused());
        }
        if (c == '\r') actionPerformed((GuiButton) controlList.get(0));
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        nameField.mouseClicked(i, j, k);
        ipField.mouseClicked(i, j, k);
    }

    public void updateScreen() {
        nameField.updateCursorCounter();
        ipField.updateCursorCounter();
    }
}
package net.minecraft.src.forgetten_sunbeams.serverlist;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;

public class GuiServerEdit extends GuiScreen {
    private GuiScreen parentScreen;
    private GuiTextField nameField;
    private GuiTextField ipField;
    private ServerList.ServerData serverData;
    private int serverIndex;
    private boolean isEditMode;

    public GuiServerEdit(GuiScreen parentScreen, ServerList.ServerData data, int index) {
        this.parentScreen = parentScreen;
        this.serverData = data;
        this.serverIndex = index;
        this.isEditMode = (data != null);
    }

    public void initGui() {
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, isEditMode ? "Save" : "Add"));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, "Cancel"));
        
        nameField = new GuiTextField(parentScreen, fontRenderer, width / 2 - 100, height / 4 + 30, 200, 20, null);
        nameField.setText("");
        nameField.setMaxStringLength(32);
        nameField.setFocused(true);
        
        ipField = new GuiTextField(parentScreen, fontRenderer, width / 2 - 100, height / 4 + 60, 200, 20, null);
        ipField.setText("");
        ipField.setMaxStringLength(128);
        
        if (isEditMode && serverData != null) {
            nameField.setText(serverData.name != null ? serverData.name : "");
            ipField.setText(serverData.ip != null ? serverData.ip : "");
        } else {
            nameField.setText("Minecraft Server");
        }
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) return;
        
        if (guibutton.id == 0) {
            String name = nameField.getText().trim();
            String ip = ipField.getText().trim();
            if (name.length() > 0 && ip.length() > 0) {
                ServerList serverList = new ServerList(mc);
                if (isEditMode) {
                    serverList.updateServer(serverIndex, name, ip);
                } else {
                    serverList.addServer(name, ip);
                }
                mc.displayGuiScreen(parentScreen);
            }
        } else if (guibutton.id == 1) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, isEditMode ? "Edit Server" : "Add Server", width / 2, 20, 0xFFFFFF);
        drawString(fontRenderer, "Server Name:", width / 2 - 100, height / 4 + 18, 0xA0A0A0);
        drawString(fontRenderer, "Server Address:", width / 2 - 100, height / 4 + 48, 0xA0A0A0);
        nameField.drawTextBox();
        ipField.drawTextBox();
        super.drawScreen(i, j, f);
    }

    protected void keyTyped(char c, int i) {
        nameField.textboxKeyTyped(c, i);
        ipField.textboxKeyTyped(c, i);
        if (c == '\t') {
            if (nameField.getIsFocused()) {
                nameField.setFocused(false);
                ipField.setFocused(true);
            } else {
                nameField.setFocused(true);
                ipField.setFocused(false);
            }
        }
        if (c == '\r') {
            actionPerformed((GuiButton) controlList.get(0));
        }
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
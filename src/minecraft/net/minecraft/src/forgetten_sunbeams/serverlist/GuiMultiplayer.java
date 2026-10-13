package net.minecraft.src.forgetten_sunbeams.serverlist;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiMultiplayer extends GuiScreen {
    @SuppressWarnings("unused")
	private GuiScreen parentScreen;
    private ServerList serverList;
    private int selectedServer = -1;
    private GuiButton buttonEdit;
    private GuiButton buttonDelete;
    private GuiButton buttonJoin;
    
    private long lastClickTime = 0;
    private int lastClickedServer = -1;
    private static final long DOUBLE_CLICK_DELAY = 300;

    public GuiMultiplayer(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @SuppressWarnings("unchecked")
	public void initGui() {
        serverList = new ServerList(mc);
        serverList.loadServerList();
        controlList.clear();
        
        buttonEdit = new GuiButton(2, width / 2 - 154, height - 28, 70, 20, "Edit");
        buttonDelete = new GuiButton(3, width / 2 - 74, height - 28, 70, 20, "Delete");
        buttonJoin = new GuiButton(4, width / 2 + 4, height - 28, 70, 20, "Join");
        
        controlList.add(new GuiButton(0, width / 2 - 154, height - 52, 150, 20, "Add Server"));
        controlList.add(new GuiButton(1, width / 2 + 4, height - 52, 150, 20, "Direct Connect"));
        controlList.add(buttonEdit);
        controlList.add(buttonDelete);
        controlList.add(buttonJoin);
        
        updateButtons();
    }

    private void updateButtons() {
        boolean hasSelection = selectedServer >= 0;
        buttonEdit.enabled = hasSelection;
        buttonDelete.enabled = hasSelection;
        buttonJoin.enabled = hasSelection;
    }

    private void joinSelectedServer() {
        if (selectedServer >= 0) {
            ServerList.ServerData server = serverList.getServer(selectedServer);
            server.lastPlayed = System.currentTimeMillis();
            serverList.saveServerList();
            
            String[] parts = server.ip.split(":");
            String address = parts[0];
            int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 25565;
            mc.displayGuiScreen(new GuiConnecting(mc, address, port));
        }
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) return;
        
        if (guibutton.id == 0) {
            mc.displayGuiScreen(new GuiServerEdit(this, null, -1));
        } else if (guibutton.id == 1) {
            mc.displayGuiScreen(new GuiDirectConnect(this));
        } else if (guibutton.id == 2) {
            if (selectedServer >= 0) {
                mc.displayGuiScreen(new GuiServerEdit(this, serverList.getServer(selectedServer), selectedServer));
            }
        } else if (guibutton.id == 3) {
            if (selectedServer >= 0) {
                serverList.removeServer(selectedServer);
                selectedServer = -1;
                lastClickedServer = -1;
                updateButtons();
            }
        } else if (guibutton.id == 4) {
            joinSelectedServer();
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int listStartY = 32;
        int listEndY = height - 60;
        int entryHeight = 32;
        
        if (j >= listStartY && j < listEndY) {
            int index = (j - listStartY) / entryHeight;
            if (index >= 0 && index < serverList.getServerCount()) {
                long currentTime = System.currentTimeMillis();
                
                if (index == lastClickedServer && (currentTime - lastClickTime) < DOUBLE_CLICK_DELAY) {
                    selectedServer = index;
                    joinSelectedServer();
                    lastClickTime = 0;
                    lastClickedServer = -1;
                } else {
                    selectedServer = index;
                    updateButtons();
                    lastClickTime = currentTime;
                    lastClickedServer = index;
                }
            } else {
                selectedServer = -1;
                lastClickedServer = -1;
                updateButtons();
            }
        } else {
            selectedServer = -1;
            lastClickedServer = -1;
            updateButtons();
        }
    }

    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Select Server", width / 2, 16, 0xFFFFFF);
        
        int listStartY = 32;
        int listEndY = height - 60;
        int entryHeight = 32;
        
        drawRect(width / 2 - 155, listStartY - 1, width / 2 + 155, listEndY + 1, 0xA0000000);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
        int hoveredIndex = -1;
        if (j >= listStartY && j < listEndY) {
            hoveredIndex = (j - listStartY) / entryHeight;
        }
        
        for (int idx = 0; idx < serverList.getServerCount(); idx++) {
            int y = listStartY + idx * entryHeight;
            if (y >= listEndY) break;
            
            ServerList.ServerData server = serverList.getServer(idx);
            
            boolean isHovered = (idx == hoveredIndex && hoveredIndex < serverList.getServerCount());
            boolean isSelected = (idx == selectedServer);
            
            if (isHovered || isSelected) {
                drawRect(width / 2 - 153, y, width / 2 + 153, y + entryHeight - 1, 0x80FFFFFF);
            }
            
            String name = server.name != null ? server.name : "Unknown Server";
            String ip = server.ip != null ? server.ip : "0.0.0.0";
            String lastPlayed = server.lastPlayed > 0 ? sdf.format(new Date(server.lastPlayed)) : "Never";
            
            drawString(fontRenderer, name, width / 2 - 150, y + 4, 0xFFFFFF);
            drawString(fontRenderer, ip, width / 2 - 150, y + 18, 0x808080);
            
            int lpWidth = fontRenderer.getStringWidth(lastPlayed);
            drawString(fontRenderer, lastPlayed, width / 2 + 150 - lpWidth, y + 4, 0x808080);
        }
        super.drawScreen(i, j, f);
    }
}
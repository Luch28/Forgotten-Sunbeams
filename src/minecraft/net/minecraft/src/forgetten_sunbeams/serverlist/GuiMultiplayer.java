package net.minecraft.src.forgetten_sunbeams.serverlist;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

public class GuiMultiplayer extends GuiScreen {
    private GuiScreen parentScreen;
    private ServerList serverList;
    private ServerSlot serverSlot;
    private int selectedServer = -1;
    private GuiButton buttonAdd;
    private GuiButton buttonEdit;
    private GuiButton buttonDelete;
    private GuiButton buttonJoin;

    public GuiMultiplayer(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @SuppressWarnings("unchecked")
    public void initGui() {
        serverList = new ServerList(mc);
        serverList.loadServerList();
        controlList.clear();

        serverSlot = new ServerSlot(this);

        // Верхние кнопки
        buttonAdd = new GuiButton(0, width / 2 - 154, height - 52, 150, 20, "Add Server");
        controlList.add(buttonAdd);
        controlList.add(new GuiButton(1, width / 2 + 4, height - 52, 150, 20, "Direct Connect"));

        // Нижние кнопки
        buttonEdit   = new GuiButton(2, width / 2 - 154, height - 28, 70, 20, "Edit");
        buttonDelete = new GuiButton(3, width / 2 - 74,  height - 28, 70, 20, "Delete");
        buttonJoin   = new GuiButton(4, width / 2 + 4,   height - 28, 70, 20, "Join");
        controlList.add(buttonEdit);
        controlList.add(buttonDelete);
        controlList.add(buttonJoin);

        updateButtons();
    }

    private void updateButtons() {
        boolean hasSelection = selectedServer >= 0 && selectedServer < serverList.getServerCount();
        buttonEdit.enabled   = hasSelection;
        buttonDelete.enabled = hasSelection;
        buttonJoin.enabled   = hasSelection;
        // Показываем, что добавить больше нельзя
        buttonAdd.enabled = !serverList.isFull();
        buttonAdd.displayString = serverList.isFull()
                ? "Add Server (5/5)"
                : "Add Server (" + serverList.getServerCount() + "/" + ServerList.MAX_SERVERS + ")";
    }

    private void joinSelectedServer() {
        if (selectedServer < 0 || selectedServer >= serverList.getServerCount()) return;
        ServerList.ServerData server = serverList.getServer(selectedServer);
        server.lastPlayed = System.currentTimeMillis();
        serverList.saveServerList();

        String[] parts = server.ip.split(":");
        String address = parts[0];
        int port = 25565;
        if (parts.length > 1) {
            try { port = Integer.parseInt(parts[1]); } catch (NumberFormatException ignored) {}
        }
        mc.displayGuiScreen(new GuiConnecting(mc, address, port));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) return;

        switch (guibutton.id) {
            case 0:
                if (serverList.isFull()) return;
                mc.displayGuiScreen(new GuiServerEdit(this, null, -1));
                break;
            case 1:
                mc.displayGuiScreen(new GuiDirectConnect(this));
                break;
            case 2:
                if (selectedServer >= 0)
                    mc.displayGuiScreen(new GuiServerEdit(this, serverList.getServer(selectedServer), selectedServer));
                break;
            case 3:
                if (selectedServer >= 0) {
                    serverList.removeServer(selectedServer);
                    selectedServer = -1;
                    serverSlot.updateSelection(); // пересчитать выделение
                    updateButtons();
                }
                break;
            case 4:
                joinSelectedServer();
                break;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Select Server", width / 2, 16, 0xFFFFFF);
        serverSlot.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void handleMouseInput() {
        super.handleMouseInput();
    }

    class ServerSlot extends GuiSlot {
        private final GuiMultiplayer parent;

        public ServerSlot(GuiMultiplayer parent) {
            super(parent.mc, parent.width, parent.height, 32, parent.height - 60, 36);
            this.parent = parent;
        }

        protected int getSize() {
            return parent.serverList.getServerCount();
        }

        protected void elementClicked(int index, boolean doubleClicked) {
            parent.selectedServer = index;
            parent.updateButtons();

            if (doubleClicked && index >= 0) {
                parent.joinSelectedServer();
            }
        }

        protected boolean isSelected(int index) {
            return index == parent.selectedServer;
        }

        protected void drawBackground() {
        }


        public void updateSelection() {
            if (parent.selectedServer >= getSize()) {
                parent.selectedServer = getSize() - 1;
            }
        }

        protected void drawSlot(int index, int x, int y, int slotHeight, Tessellator tessellator) {
            if (index < 0 || index >= parent.serverList.getServerCount()) return;

            ServerList.ServerData server = parent.serverList.getServer(index);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            String name = server.name != null ? server.name : "Unknown Server";
            String ip   = server.ip   != null ? server.ip   : "0.0.0.0";
            String lastPlayed = server.lastPlayed > 0
                    ? sdf.format(new Date(server.lastPlayed))
                    : "Never";

            parent.drawString(parent.fontRenderer, name, x + 3, y + 2, 0xFFFFFF);
            parent.drawString(parent.fontRenderer, ip,   x + 3, y + 16, 0x808080);

            int lpWidth = parent.fontRenderer.getStringWidth(lastPlayed);
            parent.drawString(parent.fontRenderer, lastPlayed,
                    x + 220 - lpWidth, y + 2, 0x808080);
        }
    }
}
package net.minecraft.src.forgetten_sunbeams.serverlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ServerList {
    private List<ServerData> servers = new ArrayList<ServerData>();
    private File serverListFile;

    @SuppressWarnings("static-access")
	public ServerList(net.minecraft.client.Minecraft mc) {
        this.serverListFile = new File(mc.getMinecraftDir(), "servers.txt");
        loadServerList();
    }

    public void loadServerList() {
        servers.clear();
        if (!serverListFile.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(serverListFile));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 2) {
                    String name = parts[0];
                    String ip = parts[1];
                    long lastPlayed = 0L;
                    
                    if (parts.length >= 3) {
                        try {
                            lastPlayed = Long.parseLong(parts[2].trim());
                        } catch (NumberFormatException e) {
                            lastPlayed = 0L;
                        }
                    }
                    servers.add(new ServerData(name, ip, lastPlayed));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveServerList() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(serverListFile));
            for (ServerData server : servers) {
                writer.println(server.name + "|" + server.ip + "|" + server.lastPlayed);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addServer(String name, String ip) {
        servers.add(new ServerData(name, ip, 0L));
        saveServerList();
    }

    public void updateServer(int index, String name, String ip) {
        if (index >= 0 && index < servers.size()) {
            ServerData data = servers.get(index);
            data.name = name;
            data.ip = ip;
            saveServerList();
        }
    }

    public void removeServer(int index) {
        if (index >= 0 && index < servers.size()) {
            servers.remove(index);
            saveServerList();
        }
    }

    public ServerData getServer(int index) {
        if (index >= 0 && index < servers.size()) {
            return servers.get(index);
        }
        return null;
    }

    public int getServerCount() {
        return servers.size();
    }

    public List<ServerData> getServers() {
        return servers;
    }

    public static class ServerData {
        public String name;
        public String ip;
        public long lastPlayed;

        public ServerData(String name, String ip, long lastPlayed) {
            this.name = name;
            this.ip = ip;
            this.lastPlayed = lastPlayed;
        }
    }
}
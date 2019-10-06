package World16.Utils;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DiscordBot implements Runnable {

    private Main plugin;
    private CustomConfigManager customConfigManager;

    private PrintWriter out;
    private BufferedReader in;
    private Scanner inSc;

    private Socket socket;

    private boolean isOn;
    private boolean tryToReconnect;

    public DiscordBot(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        tryToReconnect = false;
    }

    public boolean setup() {
        try {
            socket = new Socket("76.182.18.245", 2020);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inSc = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            if (tryToReconnect) {
                this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.EMERGENCY_TAG + " &cDiscord bot server socket failed to reconnect."));
            }
            this.isOn = false;
            return false;
        }

        if (tryToReconnect) {
            tryToReconnect = false;
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.USELESS_TAG + " &aDiscord bot Socket has successfully reconnected."));
        }

        this.isOn = true;
        this.sendServerStartMessage();
        return true;
    }

    public void run() {
        while (inSc.hasNextLine()) {
            String line = inSc.nextLine();
            switch (line) {
                case "0":
                    out.println("1");
                    ourCheckError();
                    break;
            }
        }
    }

    public void sendJoinMessage(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerJoin");
        jsonObject.put("Player", player.getDisplayName());
        jsonPrintOut(jsonObject);
    }

    public void sendLeaveMessage(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerQuit");
        jsonObject.put("Player", player.getDisplayName());
        jsonPrintOut(jsonObject);
    }

    public void sendEasyBackupEvent(me.forseth11.easybackup.api.Event event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "EasyBackup");
        jsonObject.put("EasyBackupTYPE", event.getType().name());
        jsonObject.put("Message", event.getMessage());
        jsonObject.put("Time", event.getTime());
        jsonPrintOut(jsonObject);
    }

    public void sendServerStartMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "ServerStart");
        jsonPrintOut(jsonObject);
    }

    public void sendServerQuitMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "ServerQuit");
        jsonPrintOut(jsonObject);
    }

    private void jsonPrintOut(JSONObject jsonObject) {
        if (!isOn) return;
        jsonObject.put("WHO", "World1-6");
        out.println(jsonObject.toJSONString());
        ourCheckError();
    }

    private void close() {
        try {
            socket.close();
            out.close();
            in.close();
            inSc.close();
        } catch (Exception ex) {
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.USELESS_TAG + " &6Closing sockets made an Exception"));
        }
    }

    private void ourCheckError() {
        if (out.checkError() && !tryToReconnect) {
            tryToReconnect = true;
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.EMERGENCY_TAG + " &6Server Socket has found an error going to try to reconnect."));
            close();
            new BukkitRunnable() {
                @Override
                public void run() {
                    setup();
                }
            }.runTaskLater(plugin, 600);
        }
    }
}

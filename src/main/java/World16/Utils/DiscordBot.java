package World16.Utils;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DiscordBot {

    private Main plugin;
    private CustomConfigManager customConfigManager;

    private PrintWriter out;
    private BufferedReader in;
    private Scanner inSc;

    private Socket socket;

    private boolean isEnabled;

    public DiscordBot(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        isEnabled = false;
    }

    public boolean setup() {
        try {
            socket = new Socket("76.182.18.245", 2020);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inSc = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            isEnabled = false;
            return false;
        }
        isEnabled = true;
        this.sendServerStartMessage();
        return true;
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
        if (!isEnabled) return;
        if (socket.isClosed()) setup();
        jsonObject.put("WHO", "World1-6");
        jsonObject.put("SV", this.plugin.getApi().getServerVersion());
        out.println(jsonObject.toJSONString());
        close();
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
}

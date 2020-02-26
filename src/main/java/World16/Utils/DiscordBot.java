package World16.Utils;

import CCUtils.Sockets.Sockets.Client.SimpleSocketClient;
import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class DiscordBot extends SimpleSocketClient {

    private Main plugin;
    private CustomConfigManager customConfigManager;

    private boolean isEnabled;
    private boolean isWaitingForAResponse;

    public DiscordBot(Main plugin, CustomConfigManager customConfigManager, boolean isEnabled) {
        super("ip.andrewsdatacenter.com", 2020);

        this.isEnabled = isEnabled;
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
    }

    public void sendJoinMessage(String displayName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerJoin");
        jsonObject.put("Player", displayName);
        ourJsonPrintOut(jsonObject, false);
    }

    public void sendLeaveMessage(String displayName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerQuit");
        jsonObject.put("Player", displayName);
        ourJsonPrintOut(jsonObject, false);
    }

    public void sendServerStartMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "ServerStart");
        ourJsonPrintOut(jsonObject, false);
    }

    public void sendServerQuitMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "ServerQuit");
        ourJsonPrintOut(jsonObject, false);
    }

    public void sendPlayerMessage(String playerName, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerMessage");
        jsonObject.put("Player", playerName);
        jsonObject.put("Message", message);
        ourJsonPrintOut(jsonObject, false);
    }

    public void ourJsonPrintOut(JSONObject jsonObject, boolean waitForAResponse) {
        if (!isEnabled) return;
        jsonObject.put("SV", this.plugin.getApi().getServerVersion());
        isWaitingForAResponse = waitForAResponse;
        jsonPrintOut(jsonObject, "World1-6", waitForAResponse);
    }

    @Override
    public void translate(JSONObject jsonObject) {
        if (isWaitingForAResponse) isWaitingForAResponse = false;
    }
}


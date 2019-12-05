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
        super("76.182.18.245", 2020);

        this.isEnabled = isEnabled;
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
    }

    public void sendJoinMessage(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerJoin");
        jsonObject.put("Player", player.getDisplayName());
        ourJsonPrintOut(jsonObject, false);
    }

    public void sendLeaveMessage(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerQuit");
        jsonObject.put("Player", player.getDisplayName());
        ourJsonPrintOut(jsonObject, false);
    }

    public void sendEasyBackupEvent(me.forseth11.easybackup.api.Event event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "EasyBackup");
        jsonObject.put("EasyBackupTYPE", event.getType().name());
        jsonObject.put("Message", event.getMessage());
        jsonObject.put("Time", event.getTime());
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

    public void sendPlayerMessage(Player player, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TYPE", "PlayerMessage");
        jsonObject.put("Player", player.getDisplayName());
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


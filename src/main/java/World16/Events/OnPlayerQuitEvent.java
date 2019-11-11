package World16.Events;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.SetListMap;
import World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private Main plugin;
    private API api;
    private SetListMap setListMap;
    private CustomConfigManager customConfigManager;

    public OnPlayerQuitEvent(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = new API(this.plugin);
        this.customConfigManager = customConfigManager;

        this.setListMap = this.plugin.getSetListMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQUIT(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        //TEMP.yml
        ConfigurationSection configurationSection = this.api.getPlayerTempYml(customConfigManager, p);
        configurationSection.set("Gamemode", p.getGameMode().name());
        this.customConfigManager.getTempYml().saveConfigSilent();
        //...

        //CLEAR Set's and List's and Map's
        setListMap.clearSetListMap(p);

        event.setQuitMessage("");
        Bukkit.broadcastMessage(Translate.chat(API.PREFIX + " &5Bye Bye, " + p.getDisplayName()));

        this.plugin.getDiscordBot().sendLeaveMessage(p);
    }
}

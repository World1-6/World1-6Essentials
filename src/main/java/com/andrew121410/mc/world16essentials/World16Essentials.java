package com.andrew121410.mc.world16essentials;

import com.andrew121410.mc.world16essentials.commands.*;
import com.andrew121410.mc.world16essentials.commands.back.BackCMD;
import com.andrew121410.mc.world16essentials.commands.home.DelhomeCMD;
import com.andrew121410.mc.world16essentials.commands.home.HomeCMD;
import com.andrew121410.mc.world16essentials.commands.home.HomeListCMD;
import com.andrew121410.mc.world16essentials.commands.home.SetHomeCMD;
import com.andrew121410.mc.world16essentials.commands.spawn.SetSpawnCMD;
import com.andrew121410.mc.world16essentials.commands.spawn.SpawnCMD;
import com.andrew121410.mc.world16essentials.commands.time.FirstJoinedCMD;
import com.andrew121410.mc.world16essentials.commands.time.LastJoinCMD;
import com.andrew121410.mc.world16essentials.commands.time.TimeOfLoginCMD;
import com.andrew121410.mc.world16essentials.commands.tp.TpAcceptCMD;
import com.andrew121410.mc.world16essentials.commands.tp.TpDenyCMD;
import com.andrew121410.mc.world16essentials.commands.tp.TpaCMD;
import com.andrew121410.mc.world16essentials.commands.warp.DelwarpCMD;
import com.andrew121410.mc.world16essentials.commands.warp.SetWarpCMD;
import com.andrew121410.mc.world16essentials.commands.warp.WarpCMD;
import com.andrew121410.mc.world16essentials.listeners.*;
import com.andrew121410.mc.world16essentials.managers.AfkManager;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.managers.HomeManager;
import com.andrew121410.mc.world16essentials.managers.WarpManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16essentials.utils.OtherPlugins;
import com.andrew121410.mc.world16essentials.utils.PlayerInitializer;
import com.andrew121410.mc.world16essentials.utils.SetListMap;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class World16Essentials extends JavaPlugin {

    private static World16Essentials plugin;

    private SetListMap setListMap;
    private OtherPlugins otherPlugins;

    private PlayerInitializer playerInitializer;

    //Managers
    private CustomConfigManager customConfigManager;
    private WarpManager warpManager;
    private AfkManager afkManager;
    private HomeManager homeManager;

    private API api;

    public static World16Essentials getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.setListMap = new SetListMap();

        // Load configs first
        registerMainConfig();
        this.customConfigManager = new CustomConfigManager(this);
        customConfigManager.registerAllCustomConfigs();

        this.otherPlugins = new OtherPlugins(this);
        this.api = new API(this);
        registerManagers();
        this.playerInitializer = new PlayerInitializer(this);

        registerListeners();
        registerCommands();

        Collection<? extends Player> playerList = getServer().getOnlinePlayers();
        if (!playerList.isEmpty()) {
            //Ran when the plugin gets reloaded...
            for (Player player : playerList) {
                this.playerInitializer.load(player);
            }
            Bukkit.getServer().broadcastMessage(Translate.color("&cWorld1-6Essentials was reloaded while this isn't recommend it is supported."));
        }

        //Should we keep spawn chunks in memory
        ConfigurationSection worldsConfigurationSection = this.customConfigManager.getShitYml().getConfig().getConfigurationSection("Worlds");
        if (worldsConfigurationSection != null) {
            for (String worldString : worldsConfigurationSection.getKeys(false)) {
                ConfigurationSection worldConfigurationSection = worldsConfigurationSection.getConfigurationSection(worldString);
                if (worldConfigurationSection == null) continue;
                World world = this.getServer().getWorld(worldString);
                String shouldWeKeepSpawnChunksInMemory = (String) worldConfigurationSection.get("ShouldKeepSpawnInMemory");
                if (shouldWeKeepSpawnChunksInMemory != null && world != null) {
                    if (shouldWeKeepSpawnChunksInMemory.equalsIgnoreCase("true")) {
                        world.setKeepSpawnInMemory(true);
                    } else if (shouldWeKeepSpawnChunksInMemory.equalsIgnoreCase("false")) {
                        world.setKeepSpawnInMemory(false);
                    }
                }
            }
        }

        pluginLoadMessage();
        registerBStats(); // Register bStats last
        getServer().getConsoleSender().sendMessage(Translate.color("&9[&6World1-6Essentials&9] &2World1-6Essentials has been loaded."));

        UpdateManager.registerUpdater(this, new com.andrew121410.mc.world16essentials.Updater(this));
    }

    @Override
    public void onDisable() {
        this.setListMap.clearSetListMap();
        getServer().getConsoleSender().sendMessage(Translate.color("&9[&6World1-6Essentials&9] &eWorld1-6Essentials has been unloaded."));
    }

    private void registerCommands() {
        new DayCMD(this);
        new NightCMD(this);
        new FeedCMD(this);
        new HealCMD(this);
        new FlyCMD(this);
        new DebugCMD(this, this.customConfigManager);
        new CommandBlockCMD(this);
        new BedCMD(this);
        new RamCMD(this);
        new EChestCMD(this);
        new SignCMD(this);
        new ColorsCMD(this);
        new AfkCMD(this);
        new FlySpeedCMD(this);
        new IsAfkCMD(this);
        new BackCMD(this);
        new BroadcastCMD(this, this.customConfigManager);
        new GodCMD(this);
        new MsgCMD(this, this.customConfigManager);
        new WaitDoCMD(this, this.customConfigManager);
        new RunCommandsCMD(this, this.customConfigManager);
        new WFormatCMD(this, this.customConfigManager);
        new XyzdxdydzCMD(this);
        new WorkBenchCMD(this, this.customConfigManager);
        new PowerToolCMD(this, this.customConfigManager);
        new UnSafeEnchatmentCMD(this);
        new CommandBlockFindCMD(this);
        new SudoCMD(this);
        new ShouldKeepSpawnChunksLoadedCMD(this);
        new CountAllEntitiesCMD(this);
        new HideCMD(this);
        new UnhideCMD(this);
        new ConfigCMD(this);

        // Time
        new LastJoinCMD(this);
        new TimeOfLoginCMD(this);
        new FirstJoinedCMD(this);

        //Gamemode commands
        new GmcCMD(this);
        new GmsCMD(this);
        new GmspCMD(this);
        new GmaCMD(this);

        //Tpa commands
        new TpaCMD(this, this.customConfigManager);
        new TpAcceptCMD(this, this.customConfigManager);
        new TpDenyCMD(this);

        //Spawn commands
        new SpawnCMD(this, this.customConfigManager);
        new SetSpawnCMD(this, this.customConfigManager);

        //Homes
        new DelhomeCMD(this);
        new HomeCMD(this);
        new HomeListCMD(this);
        new SetHomeCMD(this);

        //Warps
        new WarpCMD(this);
        new SetWarpCMD(this);
        new DelwarpCMD(this);
    }

    private void registerListeners() {
        //Bukkit.getServer().getPluginManager().registerEvents(this, this);
        new OnPlayerJoinEvent(this);
        new OnPlayerQuitEvent(this);
        //...
        new OnPlayerDeathEvent(this);
        new OnPlayerDamageEvent(this);
        new OnPlayerTeleportEvent(this);
        //...
        new OnPlayerBedEnterEvent(this);
        //...
        new OnAsyncPlayerChatEvent(this);
        new OnPlayerInteractEvent(this);

        new OnServerCommandEvent(this);
        new OnSignChangeEvent(this);
    }

    private void registerMainConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    private void registerManagers() {
        this.homeManager = new HomeManager(this);

        this.warpManager = new WarpManager(this, this.customConfigManager);
        this.warpManager.loadAllWarps();

        this.afkManager = new AfkManager(this);
    }

    private void registerBStats() {
        new Metrics(this, 3011);
    }

    private void pluginLoadMessage() {
        String stringBuilder = " \r\n&2" + "__        __         _     _ _        __\n" + "\\ \\      / /__  _ __| | __| / |      / /_\n" + " \\ \\ /\\ / / _ \\| '__| |/ _` | |_____| '_ \\\n" + "  \\ V  V / (_) | |  | | (_| | |_____| (_) |\n" + "   \\_/\\_/ \\___/|_|  |_|\\__,_|_|      \\___/\n" + "\n" + "&6Developer: &dAndrew121410\r\n" + "&3Date of version: &e" + API.DATE_OF_VERSION + "" + " \r\n";
        getServer().getConsoleSender().sendMessage(Translate.color(stringBuilder));
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public CustomConfigManager getCustomConfigManager() {
        return customConfigManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public API getApi() {
        return api;
    }

    public PlayerInitializer getPlayerInitializer() {
        return playerInitializer;
    }

    public AfkManager getAfkManager() {
        return afkManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public OtherPlugins getOtherPlugins() {
        return otherPlugins;
    }
}
package com.andrew121410.World16.Main;

import com.andrew121410.World16.Commands.*;
import com.andrew121410.World16.Commands.home.delhome;
import com.andrew121410.World16.Commands.home.home;
import com.andrew121410.World16.Commands.home.homelist;
import com.andrew121410.World16.Commands.home.sethome;
import com.andrew121410.World16.Commands.jail.deljail;
import com.andrew121410.World16.Commands.jail.jail;
import com.andrew121410.World16.Commands.jail.setjail;
import com.andrew121410.World16.Commands.spawn.setspawn;
import com.andrew121410.World16.Commands.spawn.spawn;
import com.andrew121410.World16.Commands.tp.tpa;
import com.andrew121410.World16.Commands.tp.tpaccept;
import com.andrew121410.World16.Commands.tp.tpdeny;
import com.andrew121410.World16.Commands.warp.delwarp;
import com.andrew121410.World16.Commands.warp.setwarp;
import com.andrew121410.World16.Commands.warp.warp;
import com.andrew121410.World16.Events.*;
import com.andrew121410.World16.Managers.*;
import com.andrew121410.World16.Utils.*;
import com.andrew121410.World16.test.test1;
import com.andrew121410.World16Elevators.ElevatorManager;
import com.andrew121410.World16Elevators.Objects.*;
import com.andrew121410.World16FireAlarms.FireAlarmManager;
import com.andrew121410.World16FireAlarms.Objects.FireAlarmSettings;
import com.andrew121410.World16FireAlarms.Objects.FireAlarmSound;
import com.andrew121410.World16FireAlarms.Objects.Screen.FireAlarmScreen;
import com.andrew121410.World16FireAlarms.Objects.Screen.FireAlarmSignOS;
import com.andrew121410.World16FireAlarms.Objects.Simple.SimpleFireAlarm;
import com.andrew121410.World16FireAlarms.Objects.Simple.SimpleStrobe;
import com.andrew121410.World16TrafficLights.Objects.TrafficLight;
import com.andrew121410.World16TrafficLights.Objects.TrafficLightSystem;
import com.andrew121410.World16TrafficLights.Objects.TrafficSystem;
import com.andrew121410.World16TrafficLights.TrafficSystemManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    static {
        //Elevators
        ConfigurationSerialization.registerClass(ElevatorController.class, "ElevatorController");
        ConfigurationSerialization.registerClass(SignObject.class, "SignObject");
        ConfigurationSerialization.registerClass(ElevatorMovement.class, "ElevatorMovement");
        ConfigurationSerialization.registerClass(FloorObject.class, "FloorObject");
        ConfigurationSerialization.registerClass(ElevatorObject.class, "ElevatorObject");
        //Fire Alarms
        ConfigurationSerialization.registerClass(FireAlarmSound.class, "FireAlarmSound");
        ConfigurationSerialization.registerClass(FireAlarmSettings.class, "FireAlarmSettings");
        ConfigurationSerialization.registerClass(SimpleStrobe.class, "IStrobe");
        ConfigurationSerialization.registerClass(SimpleFireAlarm.class, "IFireAlarm");
        ConfigurationSerialization.registerClass(FireAlarmSignOS.class, "FireAlarmSignOS");
        ConfigurationSerialization.registerClass(FireAlarmScreen.class, "FireAlarmScreen");
        //Traffic Lights
        ConfigurationSerialization.registerClass(TrafficSystem.class, "TrafficSystem");
        ConfigurationSerialization.registerClass(TrafficLightSystem.class, "TrafficLightSystem");
        ConfigurationSerialization.registerClass(TrafficLight.class, "TrafficLight");
    }

    private static Main plugin;

    private SetListMap setListMap;

    private DiscordBot discordBot;
    private PlayerInitializer playerInitializer;

    //Managers
    private CustomConfigManager customConfigManager;
    private JailManager jailManager;
    private WarpManager warpManager;
    private ElevatorManager elevatorManager;
    private FireAlarmManager fireAlarmManager;
    private TrafficSystemManager trafficSystemManager;
    private AfkManager afkManager;
    private HomeManager homeManager;

    private API api;
    private OtherPlugins otherPlugins;

    public void onEnable() {
        plugin = this;
        this.otherPlugins = new OtherPlugins(this);
        this.setListMap = new SetListMap();

        regCustomManagers();
        regFileConfigGEN();
        regEvents();
        regCommands();
        regBStats();

        this.playerInitializer = new PlayerInitializer(this);

        getLogger().info("[World1-6Essentials] is now loaded!");
    }

    public void onDisable() {
        this.discordBot.sendServerQuitMessage();
        this.jailManager.saveAllJails();
        this.warpManager.saveAllWarps();
        this.elevatorManager.saveAllElevators();
        this.fireAlarmManager.saveFireAlarms();
        this.trafficSystemManager.saveAll();
        this.setListMap.clearSetListMap();
        getLogger().info("[World1-6Essentials] is now disabled.");
    }

    private void regCommands() {
        new day(this);
        new night(this);
        new feed(this);
        new heal(this);
        new fly(this);
        new debug(this, this.customConfigManager);
        new commandblock(this);
        new bed(this);
        new ram(this);
        new echest(this);
        new sign(this);
        new key(this); //KEY COMMAND
        new colors(this);
        new afk(this);
        new flyspeed(this, this.customConfigManager);
        new isafk(this, this.customConfigManager);
        new back(this);
        new broadcast(this, this.customConfigManager);
        new god(this);
        new msg(this, this.customConfigManager);
        new test1(this, this.customConfigManager);
        new waitdo(this, this.customConfigManager);
        new runCommands(this, this.customConfigManager);
        new wformat(this, this.customConfigManager);
        new xyzdxdydz(this);
        new workbench(this, this.customConfigManager);
        new elevator(this);
        new lastjoin(this, this.customConfigManager);
        new firealarm(this, this.customConfigManager);
        new powertool(this, this.customConfigManager);
        new unsafenchant(this, this.customConfigManager);
        new seats(this, this.customConfigManager);
        new commandblockfind(this);
        new trafficlight(this, this.customConfigManager);

        //Gamemode commands
        new gmc(this);
        new gms(this);
        new gmsp(this);
        new gma(this);

        //Tpa commands
        new tpa(this, this.customConfigManager);
        new tpaccept(this, this.customConfigManager);
        new tpdeny(this, this.customConfigManager);

        //Spawn commands
        new spawn(this, this.customConfigManager);
        new setspawn(this, this.customConfigManager);

        //Homes
        new delhome(this);
        new home(this);
        new homelist(this);
        new sethome(this);

        //Jails
        new jail(this, this.customConfigManager);
        new setjail(this, this.customConfigManager);
        new deljail(this, this.customConfigManager);

        //Warps
        new warp(this);
        new setwarp(this);
        new delwarp(this);
    }

    private void regEvents() {
        //Bukkit.getServer().getPluginManager().registerEvents(this, this);
        new OnPlayerJoinEvent(this, this.customConfigManager);
        new OnPlayerQuitEvent(this, this.customConfigManager);
        //...
        new OnPlayerDeathEvent(this);
        new OnPlayerDamageEvent(this);
        new OnPlayerTeleportEvent(this);
        //...
        new OnPlayerBedEnterEvent(this);
        new OnJoinTitleEvent(this);
        //...
        new OnAsyncPlayerChatEvent(this);
        new OnPlayerInteractEvent(this);
        new OnPlayerMoveEvent(this);
        new OnBlockBreakEvent(this);

        //Inventory
        new OnInventoryClickEvent(this);

        new OnServerCommandEvent(this);
        new OnSignChangeEvent(this);
    }

    private void regFileConfigGEN() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    private void regCustomManagers() {
        this.customConfigManager = new CustomConfigManager(this);
        customConfigManager.registerAllCustomConfigs();

        this.api = new API(this);

        this.homeManager = new HomeManager(this);

        this.jailManager = new JailManager(this.customConfigManager, this);
        this.jailManager.loadAllJails();

        this.warpManager = new WarpManager(this, this.customConfigManager);
        this.warpManager.loadAllWarps();

        this.fireAlarmManager = new FireAlarmManager(this, this.customConfigManager, this.api.isFireAlarmsEnabled());
        this.fireAlarmManager.loadFireAlarms();

        regElevators();

        this.trafficSystemManager = new TrafficSystemManager(this, this.customConfigManager, api.isTrafficSystemEnabled());
        trafficSystemManager.loadAll();

        this.afkManager = new AfkManager(this);

        this.discordBot = new DiscordBot(this, this.customConfigManager, this.api.isDiscordBotEnabled());
        this.discordBot.sendServerStartMessage();
    }

    private void regElevators() {
        this.elevatorManager = new ElevatorManager(this, this.customConfigManager, this.api.isElevatorsEnabled());
        if (this.otherPlugins.hasWorldEdit()) {
            this.elevatorManager.loadAllElevators();
        } else {
            plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.EMERGENCY_TAG + " &cElevator's won't be working since there's no WorldEdit."));
        }
    }

    private void regBStats() {
        new Metrics(this);
    }

    //Getters
    public static Main getPlugin() {
        return plugin;
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public CustomConfigManager getCustomConfigManager() {
        return customConfigManager;
    }

    public JailManager getJailManager() {
        return jailManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public API getApi() {
        return api;
    }

    public OtherPlugins getOtherPlugins() {
        return otherPlugins;
    }

    public ElevatorManager getElevatorManager() {
        return elevatorManager;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }

    public FireAlarmManager getFireAlarmManager() {
        return fireAlarmManager;
    }

    public TrafficSystemManager getTrafficSystemManager() {
        return trafficSystemManager;
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
}
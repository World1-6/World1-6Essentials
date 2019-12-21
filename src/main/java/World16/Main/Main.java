package World16.Main;

import World16.Commands.*;
import World16.Commands.home.delhome;
import World16.Commands.home.home;
import World16.Commands.home.homelist;
import World16.Commands.home.sethome;
import World16.Commands.jail.deljail;
import World16.Commands.jail.jail;
import World16.Commands.jail.setjail;
import World16.Commands.spawn.setspawn;
import World16.Commands.spawn.spawn;
import World16.Commands.tp.tpa;
import World16.Commands.tp.tpaccept;
import World16.Commands.tp.tpdeny;
import World16.Commands.warp.delwarp;
import World16.Commands.warp.setwarp;
import World16.Commands.warp.warp;
import World16.Events.*;
import World16.Managers.CustomConfigManager;
import World16.Managers.JailManager;
import World16.Managers.WarpManager;
import World16.Utils.*;
import World16.test.test1;
import World16Elevators.ElevatorManager;
import World16Elevators.Objects.ElevatorMovement;
import World16Elevators.Objects.ElevatorObject;
import World16Elevators.Objects.FloorObject;
import World16Elevators.Objects.SignObject;
import World16FireAlarms.FireAlarmManager;
import World16FireAlarms.Objects.FireAlarmSound;
import World16FireAlarms.Objects.Screen.FireAlarmScreen;
import World16FireAlarms.Objects.Screen.FireAlarmSignOS;
import World16FireAlarms.Objects.Simple.SimpleFireAlarm;
import World16FireAlarms.Objects.Simple.SimpleStrobe;
import World16TrafficLights.Objects.TrafficLight;
import World16TrafficLights.Objects.TrafficLightSystem;
import World16TrafficLights.Objects.TrafficSystem;
import World16TrafficLights.TrafficSystemManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    static {
        //Elevators
        ConfigurationSerialization.registerClass(SignObject.class, "SignObject");
        ConfigurationSerialization.registerClass(ElevatorMovement.class, "ElevatorMovement");
        ConfigurationSerialization.registerClass(FloorObject.class, "FloorObject");
        ConfigurationSerialization.registerClass(ElevatorObject.class, "ElevatorObject");
        //Fire Alarms
        ConfigurationSerialization.registerClass(FireAlarmSound.class, "FireAlarmSound");
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

    //Managers
    private CustomConfigManager customConfigManager;
    private JailManager jailManager;
    private WarpManager warpManager;
    private ElevatorManager elevatorManager;
    private FireAlarmManager fireAlarmManager;
    private TrafficSystemManager trafficSystemManager;

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
        new debug(this);
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

        this.api = new API(this, customConfigManager);

        this.jailManager = new JailManager(this.customConfigManager, this);
        this.jailManager.loadAllJails();

        this.warpManager = new WarpManager(this, this.customConfigManager);
        this.warpManager.loadAllWarps();

        this.fireAlarmManager = new FireAlarmManager(this, this.customConfigManager, this.api.isFireAlarmsEnabled());
        this.fireAlarmManager.loadFireAlarms();

        regElevators();

        this.trafficSystemManager = new TrafficSystemManager(this, this.customConfigManager, api.isTrafficSystemEnabled());
        trafficSystemManager.loadAll();

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
}
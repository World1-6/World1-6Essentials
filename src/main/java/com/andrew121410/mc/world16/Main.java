package com.andrew121410.mc.world16;

import com.andrew121410.mc.world16.commands.*;
import com.andrew121410.mc.world16.commands.home.DelhomeCMD;
import com.andrew121410.mc.world16.commands.home.HomeCMD;
import com.andrew121410.mc.world16.commands.home.HomeListCMD;
import com.andrew121410.mc.world16.commands.home.SetHomeCMD;
import com.andrew121410.mc.world16.commands.jail.DelHomeCMD;
import com.andrew121410.mc.world16.commands.jail.JailCMD;
import com.andrew121410.mc.world16.commands.jail.SetJailCMD;
import com.andrew121410.mc.world16.commands.spawn.SetSpawnCMD;
import com.andrew121410.mc.world16.commands.spawn.SpawnCMD;
import com.andrew121410.mc.world16.commands.tp.TpAcceptCMD;
import com.andrew121410.mc.world16.commands.tp.TpDenyCMD;
import com.andrew121410.mc.world16.commands.tp.TpaCMD;
import com.andrew121410.mc.world16.commands.warp.DelwarpCMD;
import com.andrew121410.mc.world16.commands.warp.SetWarpCMD;
import com.andrew121410.mc.world16.commands.warp.WarpCMD;
import com.andrew121410.mc.world16.events.*;
import com.andrew121410.mc.world16.managers.*;
import com.andrew121410.mc.world16.test.test1;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16.utils.Metrics;
import com.andrew121410.mc.world16.utils.PlayerInitializer;
import com.andrew121410.mc.world16.utils.SetListMap;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    private SetListMap setListMap;

    private PlayerInitializer playerInitializer;

    //Managers
    private CustomConfigManager customConfigManager;
    private JailManager jailManager;
    private WarpManager warpManager;
    private AfkManager afkManager;
    private HomeManager homeManager;

    private API api;

    public void onEnable() {
        plugin = this;
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
        this.jailManager.saveAllJails();
        this.warpManager.saveAllWarps();
        this.setListMap.clearSetListMap();
        getLogger().info("[World1-6Essentials] is now disabled.");
    }

    private void regCommands() {
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
        new FlySpeedCMD(this, this.customConfigManager);
        new IsAfkCMD(this, this.customConfigManager);
        new BackCMD(this);
        new BroadcastCMD(this, this.customConfigManager);
        new GodCMD(this);
        new MsgCMD(this, this.customConfigManager);
        new test1(this, this.customConfigManager);
        new WaitDoCMD(this, this.customConfigManager);
        new RunCommandsCMD(this, this.customConfigManager);
        new WFormatCMD(this, this.customConfigManager);
        new XyzdxdydzCMD(this);
        new WorkBenchCMD(this, this.customConfigManager);
        new LastJoinCMD(this, this.customConfigManager);
        new PowerToolCMD(this, this.customConfigManager);
        new USafeEnchantmentCMD(this, this.customConfigManager);
        new SeatsCMD(this, this.customConfigManager);
        new CommandBlockFindCMD(this);

        //Gamemode commands
        new GmcCMD(this);
        new GmsCMD(this);
        new GmspCMD(this);
        new GmaCMD(this);

        //Tpa commands
        new TpaCMD(this, this.customConfigManager);
        new TpAcceptCMD(this, this.customConfigManager);
        new TpDenyCMD(this, this.customConfigManager);

        //Spawn commands
        new SpawnCMD(this, this.customConfigManager);
        new SetSpawnCMD(this, this.customConfigManager);

        //Homes
        new DelhomeCMD(this);
        new HomeCMD(this);
        new HomeListCMD(this);
        new SetHomeCMD(this);

        //Jails
        new JailCMD(this, this.customConfigManager);
        new SetJailCMD(this, this.customConfigManager);
        new DelHomeCMD(this, this.customConfigManager);

        //Warps
        new WarpCMD(this);
        new SetWarpCMD(this);
        new DelwarpCMD(this);
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
        //...
        new OnAsyncPlayerChatEvent(this);
        new OnPlayerInteractEvent(this);

        new OnServerCommandEvent(this);
        new OnSignChangeEvent(this);
    }

    private void regFileConfigGEN() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    private void regCustomManagers() {
        this.api = new API(this);

        this.customConfigManager = new CustomConfigManager(this);
        customConfigManager.registerAllCustomConfigs();

        this.homeManager = new HomeManager(this);

        this.jailManager = new JailManager(this.customConfigManager, this);
        this.jailManager.loadAllJails();

        this.warpManager = new WarpManager(this, this.customConfigManager);
        this.warpManager.loadAllWarps();

        this.afkManager = new AfkManager(this);
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
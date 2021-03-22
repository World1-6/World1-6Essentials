package com.andrew121410.mc.world16;

import com.andrew121410.mc.world16.commands.*;
import com.andrew121410.mc.world16.commands.home.DelhomeCMD;
import com.andrew121410.mc.world16.commands.home.HomeCMD;
import com.andrew121410.mc.world16.commands.home.HomeListCMD;
import com.andrew121410.mc.world16.commands.home.SetHomeCMD;
import com.andrew121410.mc.world16.commands.spawn.SetSpawnCMD;
import com.andrew121410.mc.world16.commands.spawn.SpawnCMD;
import com.andrew121410.mc.world16.commands.tp.TpAcceptCMD;
import com.andrew121410.mc.world16.commands.tp.TpDenyCMD;
import com.andrew121410.mc.world16.commands.tp.TpaCMD;
import com.andrew121410.mc.world16.commands.warp.DelwarpCMD;
import com.andrew121410.mc.world16.commands.warp.SetWarpCMD;
import com.andrew121410.mc.world16.commands.warp.WarpCMD;
import com.andrew121410.mc.world16.events.*;
import com.andrew121410.mc.world16.managers.AfkManager;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.managers.HomeManager;
import com.andrew121410.mc.world16.managers.WarpManager;
import com.andrew121410.mc.world16.test.test1;
import com.andrew121410.mc.world16.utils.*;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
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

    @Override
    public void onEnable() {
        plugin = this;
        this.setListMap = new SetListMap();
        this.api = new API(this);
        this.otherPlugins = new OtherPlugins(this);

        regCustomManagers();
        regFileConfigGEN();
        regEvents();
        regCommands();
        regBStats();

        this.playerInitializer = new PlayerInitializer(this);

        Collection<? extends Player> playerList = getServer().getOnlinePlayers();
        if (!playerList.isEmpty()) {
            //Ran when the plugin gets reloaded...
            for (Player player : playerList) {
                this.playerInitializer.load(player);
            }
            Bukkit.getServer().broadcastMessage(Translate.color("&cWorld1-6Essentials was reloaded while this isn't recommend it is supported."));
        }

        pluginLoadMessage();
        getServer().getConsoleSender().sendMessage(Translate.color("&9[&6World1-6Essentials&9] &2World1-6Essentials has been loaded."));
    }

    @Override
    public void onDisable() {
        this.setListMap.clearSetListMap();
        getServer().getConsoleSender().sendMessage(Translate.color("&9[&6World1-6Essentials&9] &eWorld1-6Essentials has been unloaded."));
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
        new UnSafeEnchatmentCMD(this, this.customConfigManager);
        new CommandBlockFindCMD(this);
        new SudoCMD(this);

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

        //Warps
        new WarpCMD(this);
        new SetWarpCMD(this);
        new DelwarpCMD(this);
    }

    private void regEvents() {
        //Bukkit.getServer().getPluginManager().registerEvents(this, this);
        new OnPlayerJoinEvent(this, this.customConfigManager);
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

    private void regFileConfigGEN() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    private void regCustomManagers() {
        this.customConfigManager = new CustomConfigManager(this);
        customConfigManager.registerAllCustomConfigs();

        this.homeManager = new HomeManager(this);

        this.warpManager = new WarpManager(this, this.customConfigManager);
        this.warpManager.loadAllWarps();

        this.afkManager = new AfkManager(this);
    }

    private void regBStats() {
        new Metrics(this);
    }

    private void pluginLoadMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" \r\n&2");
        stringBuilder.append("__        __         _     _ _        __\n" +
                "\\ \\      / /__  _ __| | __| / |      / /_\n" +
                " \\ \\ /\\ / / _ \\| '__| |/ _` | |_____| '_ \\\n" +
                "  \\ V  V / (_) | |  | | (_| | |_____| (_) |\n" +
                "   \\_/\\_/ \\___/|_|  |_|\\__,_|_|      \\___/\n" +
                "\n");
        stringBuilder.append("&6Developer: &dAndrew121410\r\n");
        stringBuilder.append("&3Date of version: &e" + API.DATE_OF_VERSION + "");
        stringBuilder.append(" \r\n");
        getServer().getConsoleSender().sendMessage(Translate.color(stringBuilder.toString()));
    }

    //Getters
    public static World16Essentials getPlugin() {
        return plugin;
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
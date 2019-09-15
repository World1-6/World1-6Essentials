package World16.Main;

import World16.Commands.*;
import World16.Commands.home.delhome;
import World16.Commands.home.home;
import World16.Commands.home.homelist;
import World16.Commands.home.sethome;
import World16.Commands.tp.tpa;
import World16.Commands.tp.tpaccept;
import World16.Commands.tp.tpdeny;
import World16.Events.*;
import World16.Events.PluginEvents.EasyBackupEvent;
import World16.Managers.CustomConfigManager;
import World16.Managers.JailManager;
import World16.Utils.*;
import World16.test.test1;
import World16Elevators.ElevatorMain;
import World16Elevators.Objects.BoundingBox;
import World16Elevators.Objects.ElevatorObject;
import World16Elevators.Objects.FloorObject;
import World16Elevators.Objects.SignObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    static {
        //Elevators
        ConfigurationSerialization.registerClass(BoundingBox.class, "BoundingBox");
        ConfigurationSerialization.registerClass(SignObject.class, "SignObject");
        ConfigurationSerialization.registerClass(FloorObject.class, "FloorObject");
        ConfigurationSerialization.registerClass(ElevatorObject.class, "ElevatorObject");
    }

    private Main plugin;

    private SetListMap setListMap;

    private DiscordBot discordBot;

    //Managers
    private CustomConfigManager customConfigManager;
    private JailManager jailManager;
    private ElevatorMain elevatorMain;

    private API api;
    private OtherPlugins otherPlugins;

    public void onEnable() {
        this.plugin = this;
        this.otherPlugins = new OtherPlugins(this);
        this.setListMap = new SetListMap();
        this.api = new API(plugin);

        regCustomManagers();
        regFileConfigGEN();
        regDiscordBot();
        regElevators();
        regEvents();
        regCommands();
        regBStats();

        getLogger().info("[World1-6Essentials] is now loaded!");
    }

    public void onDisable() {
        this.discordBot.sendServerQuitMessage();
        this.getElevatorMain().saveAllElevators();
        this.setListMap.clearSetListMap();
        getLogger().info("[World1-6Essentials] is now disabled.");
    }

    private void regCommands() {
        new gmc(this);
        new gms(this);
        new gmsp(this);
        new gma(this);

        new day(this);
        new night(this);
        new feed(this);
        new heal(this);
        new fly(this);
        new debug(this);
        new commandblock(this);
        new bed(this);
        new ram(this);
        new spawn(this, this.customConfigManager);
        new echest(this);
        new sign(this);
        new key(this); //KEY COMMAND
        new colors(this);
        new setjail(this, this.customConfigManager, this.jailManager);
        new setspawn(this, this.customConfigManager);
        new jail(this, this.customConfigManager, this.jailManager);
        new afk(this);
        new flyspeed(this, this.customConfigManager);
        new isafk(this, this.customConfigManager);
        new back(this);
        new broadcast(this, this.customConfigManager);
        new god(this);
        new msg(this, this.customConfigManager);

        new tpa(this, this.customConfigManager);
        new tpaccept(this, this.customConfigManager);
        new tpdeny(this, this.customConfigManager);

        new test1(this, this.customConfigManager);
        new eram(this, this.customConfigManager);
        new waitdo(this, this.customConfigManager);
        new runCommands(this, this.customConfigManager);
        new wformat(this, this.customConfigManager);
        new xyzdxdydz(this);
        new workbench(this, this.customConfigManager);
        new elevator(this, this.customConfigManager);

        //Homes
        new delhome(this.plugin);
        new home(this.plugin);
        new homelist(this.plugin);
        new sethome(this.plugin);
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

        //PluginEvents
        new EasyBackupEvent(this);
    }

    private void regFileConfigGEN() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    private void regCustomManagers() {
        this.customConfigManager = new CustomConfigManager(this);
        customConfigManager.registerAllCustomConfigs();

        this.jailManager = new JailManager(this.customConfigManager, this);
        this.jailManager.getAllJailsFromConfig();
    }

    private void regBStats() {
        new Metrics(this);
    }

    private void regDiscordBot() {
        this.discordBot = new DiscordBot(this, this.customConfigManager);
        boolean discordbot = this.discordBot.setup();
        if (discordbot) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this, this.discordBot);
        } else {
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.EMERGENCY_TAG + " &cDiscord Bot has not been enabled because of exception"));
        }
    }

    private void regElevators() {
        this.elevatorMain = new ElevatorMain(this, this.customConfigManager);
        if (this.otherPlugins.hasWorldEdit()) {
            this.elevatorMain.loadAllElevators();
        } else {
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.EMERGENCY_TAG + " &cElevator's won't be working since there's no WorldEdit."));
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("World1-6Essentials")) {
                p.sendMessage(Translate.chat("&6Made By Andrew121410 My -> Discord: Andrew121410#2035"));
                return true;
            }
        }
        return true;
    }

    //Getters

    public Main getPlugin() {
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

    public API getApi() {
        return api;
    }

    public OtherPlugins getOtherPlugins() {
        return otherPlugins;
    }

    public ElevatorMain getElevatorMain() {
        return elevatorMain;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }
}
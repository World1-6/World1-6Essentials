package World16.Managers;

import World16.Main.Main;

public class CustomConfigManager {

    private Main plugin;

    private CustomYmlManager shitYml;
    private CustomYmlManager jailsYml;
    private CustomYmlManager warpsYml;
    private CustomYmlManager elevatorsYml;
    private CustomYmlManager playersYml;
    private CustomYmlManager firealarmsYml;
    private CustomYmlManager trafficLightYml;

    public CustomConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerAllCustomConfigs() {
        //Shit.yml
        this.shitYml = new CustomYmlManager(this.plugin);
        this.shitYml.setup("shit.yml");
        this.shitYml.saveConfig();
        this.shitYml.reloadConfig();
        //...

        //jails.yml
        this.jailsYml = new CustomYmlManager(this.plugin);
        this.jailsYml.setup("jails.yml");
        this.jailsYml.saveConfig();
        this.jailsYml.reloadConfig();
        //...

        //warps.yml
        this.warpsYml = new CustomYmlManager(this.plugin);
        this.warpsYml.setup("warps.yml");
        this.warpsYml.saveConfig();
        this.warpsYml.reloadConfig();
        //...

        //elevators.yml
        this.elevatorsYml = new CustomYmlManager(this.plugin);
        this.elevatorsYml.setup("elevators.yml");
        this.elevatorsYml.saveConfig();
        this.elevatorsYml.reloadConfig();
        //...

        //players.yml
        this.playersYml = new CustomYmlManager(this.plugin);
        this.playersYml.setup("players.yml");
        this.playersYml.saveConfig();
        this.playersYml.reloadConfig();
        //...

        //firealarms.yml
        this.firealarmsYml = new CustomYmlManager(this.plugin);
        this.firealarmsYml.setup("firealarms.yml");
        this.firealarmsYml.saveConfig();
        this.firealarmsYml.reloadConfig();
        //...

        //trafficlights.yml
        this.trafficLightYml = new CustomYmlManager(this.plugin);
        this.trafficLightYml.setup("trafficlights.yml");
        this.trafficLightYml.saveConfig();
        this.trafficLightYml.reloadConfig();
        //...

    }

    public void saveAll() {
        this.shitYml.saveConfigSilent();
        this.jailsYml.saveConfigSilent();
        this.warpsYml.saveConfigSilent();
        this.elevatorsYml.saveConfigSilent();
        this.playersYml.saveConfigSilent();
        this.firealarmsYml.saveConfigSilent();
        this.trafficLightYml.saveConfigSilent();
    }

    public void reloadAll() {
        this.shitYml.reloadConfigSilint();
        this.jailsYml.reloadConfigSilint();
        this.warpsYml.reloadConfigSilint();
        this.elevatorsYml.reloadConfigSilint();
        this.playersYml.reloadConfigSilint();
        this.firealarmsYml.reloadConfigSilint();
        this.trafficLightYml.reloadConfigSilint();
    }

    public CustomYmlManager getShitYml() {
        return shitYml;
    }

    public CustomYmlManager getJailsYml() {
        return jailsYml;
    }

    public CustomYmlManager getWarpsYml() {
        return warpsYml;
    }

    public CustomYmlManager getElevatorsYml() {
        return elevatorsYml;
    }

    public CustomYmlManager getPlayersYml() {
        return playersYml;
    }

    public CustomYmlManager getFireAlarmsYml() {
        return firealarmsYml;
    }

    public CustomYmlManager getTrafficLightYml() {
        return trafficLightYml;
    }
}

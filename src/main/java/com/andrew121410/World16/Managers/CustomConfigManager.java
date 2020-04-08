package com.andrew121410.World16.Managers;

import com.andrew121410.World16.Main.Main;

public class CustomConfigManager {

    private Main plugin;

    private CustomYmlManager shitYml;
    private CustomYmlManager jailsYml;
    private CustomYmlManager warpsYml;
    private CustomYmlManager elevatorsYml;
    private CustomYmlManager playersYml;
    private CustomYmlManager firealarmsYml;
    private CustomYmlManager trafficLightYml;
    private CustomYmlManager moneyYml;

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

        //money.yml
        this.moneyYml = new CustomYmlManager(this.plugin);
        this.moneyYml.setup("money.yml");
        this.moneyYml.saveConfig();
        this.moneyYml.reloadConfig();
        //...
    }

    public void saveAll() {
        this.shitYml.saveConfig();
        this.jailsYml.saveConfig();
        this.warpsYml.saveConfig();
        this.elevatorsYml.saveConfig();
        this.playersYml.saveConfig();
        this.firealarmsYml.saveConfig();
        this.trafficLightYml.saveConfig();
        this.moneyYml.saveConfig();
    }

    public void reloadAll() {
        this.shitYml.reloadConfig();
        this.jailsYml.reloadConfig();
        this.warpsYml.reloadConfig();
        this.elevatorsYml.reloadConfig();
        this.playersYml.reloadConfig();
        this.firealarmsYml.reloadConfig();
        this.trafficLightYml.reloadConfig();
        this.moneyYml.reloadConfig();
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

    public CustomYmlManager getMoneyYml() {
        return moneyYml;
    }
}

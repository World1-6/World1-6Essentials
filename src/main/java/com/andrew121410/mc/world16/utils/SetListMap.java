package com.andrew121410.mc.world16.utils;

import com.andrew121410.mc.world16.objects.*;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.*;

public class SetListMap {

    // 0 TO CLEAR AFTER THE PLAYER LEAVES
    // 1 TO ONLY CLEAR WHEN THE SERVER SHUTS DOWN

    private Map<String, KeyObject> keyDataM; //0
    private Map<UUID, LocationObject> backM; //0
    private Map<Player, Player> tpaM; //0
    private Map<String, Map<String, List<Location>>> eRamRaw; //0
    private Map<String, Location> latestClickedBlocked; //0
    private Map<UUID, AfkObject> afkMap; //0
    private Map<UUID, Map<String, Location>> homesMap; //0
    private Map<UUID, PowerToolObject> powerToolMap; //0
    private Map<Player, Arrow> sitMap;
    private Map<UUID, MoneyObject> moneyMap;

    private Map<String, UUID> uuidCache; //1
    private Map<String, Location> jails; //1
    private Map<String, List<String>> tabCompleteMap; //1
    private Map<String, Location> warpsMap; //1

    private List<String> flyList; //0
    private List<String> godmList; //0
    private List<Player> hiddenPlayers; //0

    private List<String> soundsList; //1
    private List<String> spyCommandBlock; //1

    //Constucer
    public SetListMap() {
        this.keyDataM = new HashMap<>();
        this.backM = new HashMap<>();
        this.tpaM = new LinkedHashMap<>();
        this.eRamRaw = new HashMap<>();
        this.latestClickedBlocked = new HashMap<>();
        this.afkMap = new HashMap<>();
        this.homesMap = new HashMap<>();
        this.powerToolMap = new HashMap<>();
        this.sitMap = new HashMap<>();
        this.moneyMap = new HashMap<>();

        this.uuidCache = new HashMap<>();
        this.jails = new HashMap<>();
        this.tabCompleteMap = new HashMap<>();
        this.warpsMap = new HashMap<>();

        //Lists
        this.flyList = new ArrayList<>();
        this.godmList = new ArrayList<>();
        this.hiddenPlayers = new ArrayList<>();

        this.soundsList = new ArrayList<>();
        this.spyCommandBlock = new ArrayList<>();
    }

    //METHODS
    public void clearSetListMap(Player p) {
        clearAllMaps(p);
        clearAllLists(p);
    }

    public void clearSetListMap() {
        clearAllMaps();
        clearAllLists();
    }

    public void clearAllMaps(Player p) {
        keyDataM.remove(p.getDisplayName());

        backM.remove(p.getUniqueId());

        tpaM.remove(p);

        eRamRaw.remove(p.getDisplayName());

        latestClickedBlocked.remove(p.getDisplayName());

        afkMap.remove(p.getUniqueId());

        homesMap.remove(p.getUniqueId());

        powerToolMap.remove(p.getUniqueId());

        sitMap.remove(p);

        moneyMap.remove(p.getUniqueId());
    }

    public void clearAllMaps() {
        keyDataM.clear();
        backM.clear();
        tpaM.clear();
        eRamRaw.clear();
        latestClickedBlocked.clear();
        afkMap.clear();
        uuidCache.clear();
        jails.clear();
        tabCompleteMap.clear();
        homesMap.clear();
        warpsMap.clear();
        powerToolMap.clear();
        sitMap.clear();
        moneyMap.clear();
    }

    public void clearAllLists(Player p) {
        flyList.remove(p.getDisplayName());

        godmList.remove(p.getDisplayName());

        hiddenPlayers.remove(p);
    }

    public void clearAllLists() {
        flyList.clear();
        godmList.clear();
        hiddenPlayers.clear();
        soundsList.clear();
        spyCommandBlock.clear();
    }

    //Getters

    public Map<String, KeyObject> getKeyDataM() {
        return keyDataM;
    }

    public Map<UUID, LocationObject> getBackM() {
        return backM;
    }

    public Map<Player, Player> getTpaM() {
        return tpaM;
    }

    public Map<String, Map<String, List<Location>>> geteRamRaw() {
        return eRamRaw;
    }

    public Map<String, Location> getLatestClickedBlocked() {
        return latestClickedBlocked;
    }

    public Map<UUID, AfkObject> getAfkMap() {
        return afkMap;
    }

    public Map<UUID, Map<String, Location>> getHomesMap() {
        return homesMap;
    }

    public Map<String, UUID> getUuidCache() {
        return uuidCache;
    }

    public Map<String, Location> getJails() {
        return jails;
    }

    public Map<String, List<String>> getTabCompleteMap() {
        return tabCompleteMap;
    }

    public List<String> getFlyList() {
        return flyList;
    }

    public List<String> getGodmList() {
        return godmList;
    }

    public List<Player> getHiddenPlayers() {
        return hiddenPlayers;
    }

    public Map<String, Location> getWarpsMap() {
        return warpsMap;
    }

    public List<String> getSoundsList() {
        return soundsList;
    }

    public Map<UUID, PowerToolObject> getPowerToolMap() {
        return powerToolMap;
    }

    public Map<Player, Arrow> getSitMap() {
        return sitMap;
    }

    public List<String> getSpyCommandBlock() {
        return spyCommandBlock;
    }

    public Map<UUID, MoneyObject> getMoneyMap() {
        return moneyMap;
    }
}

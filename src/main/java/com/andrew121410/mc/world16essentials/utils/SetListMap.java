package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.commands.back.BackEnum;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.objects.PowerToolObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SetListMap {

    // 0 TO CLEAR AFTER THE PLAYER LEAVES
    // 1 TO ONLY CLEAR WHEN THE SERVER SHUTS DOWN

    private final Map<UUID, Map<BackEnum, Location>> backMap; //0
    private final Map<Player, Player> tpaMap; //0
    private final Map<UUID, AfkObject> afkMap; //0
    private final Map<UUID, Map<String, Location>> homesMap; //0
    private final Map<UUID, PowerToolObject> powerToolMap; //0
    private final Map<UUID, Long> timeOfLoginMap; //0
    private final Map<UUID, Set<String>> savedInventoryMap; //0

    private final Map<String, Location> warpsMap; //1
    private final Map<String, KitObject> kitsMap; //1

    private final List<String> flyList; //0
    private final List<String> godList; //0
    private final List<Player> hiddenPlayers; //0

    private final List<String> soundsList; //1
    private final List<String> spyCommandBlock; //1

    //Constructor
    public SetListMap() {
        this.backMap = new HashMap<>();
        this.tpaMap = new LinkedHashMap<>();
        this.afkMap = new HashMap<>();
        this.homesMap = new HashMap<>();
        this.powerToolMap = new HashMap<>();
        this.timeOfLoginMap = new HashMap<>();
        this.savedInventoryMap = new HashMap<>();

        this.warpsMap = new HashMap<>();
        this.kitsMap = new HashMap<>();

        //Lists
        this.flyList = new ArrayList<>();
        this.godList = new ArrayList<>();
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
        backMap.remove(p.getUniqueId());
        tpaMap.remove(p);
        afkMap.remove(p.getUniqueId());
        homesMap.remove(p.getUniqueId());
        powerToolMap.remove(p.getUniqueId());
        timeOfLoginMap.remove(p.getUniqueId());
        savedInventoryMap.remove(p.getUniqueId());
    }

    public void clearAllMaps() {
        backMap.clear();
        tpaMap.clear();
        afkMap.clear();
        homesMap.clear();
        powerToolMap.clear();
        timeOfLoginMap.clear();
        savedInventoryMap.clear();

        warpsMap.clear();
        kitsMap.clear();
    }

    public void clearAllLists(Player p) {
        flyList.remove(p.getDisplayName());
        godList.remove(p.getDisplayName());
        hiddenPlayers.remove(p);
    }

    public void clearAllLists() {
        flyList.clear();
        godList.clear();
        hiddenPlayers.clear();
        soundsList.clear();
        spyCommandBlock.clear();
    }

    public Map<UUID, Map<BackEnum, Location>> getBackMap() {
        return backMap;
    }

    public Map<Player, Player> getTpaMap() {
        return tpaMap;
    }

    public Map<UUID, AfkObject> getAfkMap() {
        return afkMap;
    }

    public Map<UUID, Map<String, Location>> getHomesMap() {
        return homesMap;
    }

    public List<String> getFlyList() {
        return flyList;
    }

    public List<String> getGodList() {
        return godList;
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

    public List<String> getSpyCommandBlock() {
        return spyCommandBlock;
    }

    public Map<UUID, Long> getTimeOfLoginMap() {
        return timeOfLoginMap;
    }

    public Map<String, KitObject> getKitsMap() {
        return kitsMap;
    }

    public Map<UUID, Set<String>> getSavedInventoryMap() {
        return savedInventoryMap;
    }
}

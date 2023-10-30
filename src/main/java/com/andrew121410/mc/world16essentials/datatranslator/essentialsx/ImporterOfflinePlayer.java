package com.andrew121410.mc.world16essentials.datatranslator.essentialsx;

import com.earth2me.essentials.OfflinePlayerStub;
import org.bukkit.Server;

import java.util.UUID;

public class ImporterOfflinePlayer extends OfflinePlayerStub {

    public ImporterOfflinePlayer(String name, UUID uuid, Server server) {
        super(uuid, server);
        this.setName(name);
    }
}
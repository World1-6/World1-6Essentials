package com.andrew121410.World16.Utils;

import com.andrew121410.World16.Main.Main;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class OtherPlugins {

    private Main plugin;


    //Plugins
    //WorldEdit
    private WorldEditPlugin worldEditPlugin;
    private boolean hasWorldEdit;
    //Vault
    private VaultCore vaultCore;
    private boolean hasVault;

    public OtherPlugins(Main plugin) {
        this.plugin = plugin;

        this.vaultCore = new VaultCore(this.plugin);

        setupWorldEditPlugin();
        setupVault();
    }

    private void setupWorldEditPlugin() {
        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin("WorldEdit");

        if (plugin == null) {
            hasWorldEdit = false;
            return;
        }

        if (plugin instanceof WorldEditPlugin) {
            this.worldEditPlugin = (WorldEditPlugin) plugin;
            hasWorldEdit = true;
        } else hasWorldEdit = false;
    }

    private void setupVault() {
        if (this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            this.hasVault = false;
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.EMERGENCY_TAG + " " + "&cVault was not found?"));
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        }
        this.hasVault = true;
        this.plugin.getServer().getServicesManager().register(Economy.class, vaultCore, this.plugin, ServicePriority.Low);
        this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.USELESS_TAG + " " + "&aVault was found."));
    }

    //Getters
    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public VaultCore getVaultCore() {
        return vaultCore;
    }

    //Bool Getter
    public boolean hasWorldEdit() {
        return hasWorldEdit;
    }

    public boolean hasVault() {
        return hasVault;
    }
}

package com.andrew121410.World16.Utils;

import com.andrew121410.World16.Main.Main;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.plugin.Plugin;

public class OtherPlugins {

    private Main plugin;

    private boolean hasWorldEdit;

    //Plugins
    private WorldEditPlugin worldEditPlugin;

    public OtherPlugins(Main plugin) {
        this.plugin = plugin;

        setUpWorldEditPlugin();
    }

    private void setUpWorldEditPlugin() {
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

    //Getters
    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    //Bool Getter

    public boolean hasWorldEdit() {
        return hasWorldEdit;
    }
}

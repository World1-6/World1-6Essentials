package com.andrew121410.mc.world16.commands;

import com.andrew121410.CCUtils.storage.SQLite;
import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.KeyManager;
import com.andrew121410.mc.world16.objects.KeyObject;
import com.andrew121410.mc.world16.tabcomplete.KeyTab;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class KeyCMD implements CommandExecutor {

    //Maps
    private Map<String, KeyObject> keyDataM;
    //...

    private Main plugin;

    private API api;
    private KeyManager keyapi;
    private SQLite isql;

    public KeyCMD(Main getPlugin) {
        this.plugin = getPlugin;

        this.keyDataM = this.plugin.getSetListMap().getKeyDataM();

        this.api = new API(this.plugin);

        this.isql = new SQLite(this.plugin.getDataFolder(), "keys");
        this.keyapi = new KeyManager(this.plugin, this.isql);

        this.plugin.getCommand("key").setExecutor(this);
        this.plugin.getCommand("key").setTabCompleter(new KeyTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.key")) { // Permission
            api.PermissionErrorMessage(p);
            return true;
        }

        KeyObject keyObject = this.keyDataM.get(p.getDisplayName());

        if (args.length == 0) {
            p.sendMessage(Translate.chat("---------------"));
            p.sendMessage(Translate.chat("&bAll of key commands."));
            p.sendMessage(Translate.chat("&6/key list < Show's all of your keys."));
            p.sendMessage(Translate.chat("&6/key set < Sets your key."));
            p.sendMessage(Translate.chat("&6/key give < Gives you your key."));
            p.sendMessage(Translate.chat("&6/key reset < Resets/Clears all data from DATABASE."));
            p.sendMessage(Translate.chat("---------------"));
            return true;
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!p.hasPermission("world16.key.set")) { // Permission
                api.PermissionErrorMessage(p);
                return true;
            }
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&cUsage: /key set <KeyID> <Lore>"));
                return true;
            } else if (args.length == 3) {
                Integer keyID;
                try {
                    keyID = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    p.sendMessage(Translate.chat("Not a number..."));
                    return true;
                }

                // STRING BUILDER
                StringBuilder setData = new StringBuilder();
                for (int i = 2; i < args.length; i++) setData.append(args[i]);
                String setDataDone = setData.toString(); // OUT PUT OF STRING BUILDER

                if (keyObject != null) {
                    keyObject.setKey(keyID, setDataDone);
                    keyapi.SetKeyAndDeleteOldKey(isql, keyID, p, setDataDone); //<-- MySql
                    p.sendMessage(Translate.chat("&6Your key has been set and stored in the isql database."));
                    return true;
                }
                return true;
            } else {
                p.sendMessage(Translate.chat("&cUsage: /key set <KeyID> <Lore>"));
                return true;
            }

            //GIVE
        } else if (args[0].equalsIgnoreCase("give")) {
            if (!p.hasPermission("world16.key.give")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&cUsage: /key give <KeyID>"));
                return true;
            } else {
                Integer keyID;
                try {
                    keyID = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    p.sendMessage(Translate.chat("Not a number..."));
                    return true;
                }
                if (keyID <= 5) {
                    keyapi.giveKey(p, keyID);
                    return true;
                } else {
                    p.sendMessage(Translate.chat("&cRight now keys can only go up too 5."));
                }
            }
            //RESET
        } else if (args[0].equalsIgnoreCase("reset")) {
            if (!p.hasPermission("world16.key.reset")) { // Permission
                api.PermissionErrorMessage(p);
                return true;
            }
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&cUsage: /key reset <KeyID>"));
                return true;
            } else if (args.length == 2 && !args[1].equalsIgnoreCase("@everything")) {
                Integer keyID;
                try {
                    keyID = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    p.sendMessage(Translate.chat("Not a number..."));
                    return true;
                }
                keyapi.ReplaceKey(isql, keyID, p, "null");
                keyDataM.remove(p.getDisplayName());
                p.sendMessage(Translate.chat("&4The lore has been reseted."));
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@everything")) {
                keyapi.ResetEverythingFromPlayerMySQL(isql, p);
                keyDataM.remove(p.getDisplayName());
                p.sendMessage(Translate.chat("&bOK..."));
                return true;
            } else {
                p.sendMessage(Translate.chat("&4Something went wrong."));
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                if (keyObject != null) {
                    p.sendMessage(Translate.chat("&6Keys: &aKeyID1 =&9 {key1} &aKeyID2 =&9 {key2} &aKeyID3 =&9 {key3} &aKeyID4 =&9 {key4} &aKeyID5 =&9 {key5}")
                            .replace("{key1}", keyObject.getKey1())
                            .replace("{key2}", keyObject.getKey2())
                            .replace("{key3}", keyObject.getKey3())
                            .replace("{key4}", keyObject.getKey4())
                            .replace("{key5}", keyObject.getKey5()));
                    return true;
                }
            }
        }
        return true;
    }
}
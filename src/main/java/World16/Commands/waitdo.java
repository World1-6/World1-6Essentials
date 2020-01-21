package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.CommandUtils;
import World16.Utils.CountdownTimer;
import World16.Utils.Translate;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class waitdo implements CommandExecutor {

    private Main plugin;

    private API api;

    private CommandUtils commandUtils;

    private CustomConfigManager customConfigManager;

    public waitdo(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();
        this.commandUtils = new CommandUtils(this.plugin);

        this.plugin.getCommand("waitdo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {

            if (!(sender instanceof BlockCommandSender)) {
                return true;
            }

            BlockCommandSender cmdblock = (BlockCommandSender) sender;
            Block commandblock = cmdblock.getBlock();

            if (args.length >= 4) {
                int sec = api.asIntOrDefault(args[0], 1);
                String[] doStart = args[1].split(API.CUSTOM_COMMAND_FORMAT);
                String[] doEnd = args[2].split(API.CUSTOM_COMMAND_FORMAT);
                String[] doEverySec = args[3].split(API.CUSTOM_COMMAND_FORMAT);
                //arg[4] is debug
                boolean debug = false;

                if (args.length >= 5 && args[4] != null) {
                    debug = api.asBooleanOrDefault(args[4], false);
                }

                String doStartString = String.join(" ", doStart);
                String doEndString = String.join(" ", doEnd);
                String doEverySecString = String.join(" ", doEverySec);

                if (debug) {
                    this.plugin.getServer().broadcastMessage(sec + " " + doStartString + " " + doEndString + " " + doEverySecString);
                }

                CountdownTimer timer = new CountdownTimer(this.plugin, sec, () -> this.plugin.getServer().dispatchCommand(sender, doStartString), () -> this.plugin.getServer().dispatchCommand(sender, doEndString), (t) -> this.plugin.getServer().dispatchCommand(sender, doEverySecString));
                timer.scheduleTimer();

                if (args.length >= 6) {
                    List<String[]> stringList = new ArrayList<>();
                    for (int i = 5; i < args.length; i++) {
                        stringList.add(args[i].split(API.CUSTOM_COMMAND_FORMAT));
                    }
                    this.commandUtils.runCommands(true, commandblock, sender, stringList);
                    stringList.clear();
                    return true;
                }
                return true;
            }
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.waitdo")) {
            api.PermissionErrorMessage(p);
            return true;
        }
        p.sendMessage(Translate.chat("&4&lOnly command blocks right now!"));
        p.sendMessage(Translate.chat("Usage: /waitdo <SEC> <StartCommandINWFormat> <EndCommandINWFORMAT> <DoEverySecINWFORMAT> <DEBUG?>"));
        return true;
    }
}
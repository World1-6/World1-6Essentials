package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RamCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    private String cpuModelCache = null;
    private String operatingSystemCache = null;
    private String kernelNumberCache = null;
    private String javaVersionCache = null;

    public RamCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("ram").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.ram")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 0) {
            sendInfo(sender);
        }

        return true;
    }

    private void sendInfo(CommandSender sender) {
        if (this.cpuModelCache == null) {
            try (Stream<String> stream = Files.lines(Paths.get("/proc/cpuinfo"))) {
                this.cpuModelCache = stream.filter(line -> line.startsWith("model name"))
                        .map(line -> line.replaceAll(".*: ", ""))
                        .findFirst().orElse("");
            } catch (Exception ignored) {
                this.cpuModelCache = null;
            }
        }
        if (this.cpuModelCache != null) {
            sender.sendMessage(Translate.color("&6CPU: &7" + this.cpuModelCache));
        }

        if (this.operatingSystemCache == null) {
            try {
                this.operatingSystemCache = System.getProperty("os.name");
            } catch (Exception ignored) {
                this.operatingSystemCache = null;
            }
        }
        if (this.operatingSystemCache != null) {
            sender.sendMessage(Translate.color("&6OS: &7" + this.operatingSystemCache));
        }

        if (this.kernelNumberCache == null) {
            try {
                this.kernelNumberCache = System.getProperty("os.version");
            } catch (Exception ignored) {
                this.kernelNumberCache = null;
            }
        }
        if (this.kernelNumberCache != null) {
            sender.sendMessage(Translate.color("&6Kernel: &7" + this.kernelNumberCache));
        }

        if (this.javaVersionCache == null) {
            try {
                this.javaVersionCache = System.getProperty("java.version");
            } catch (Exception ignored) {
                this.javaVersionCache = null;
            }
        }
        if (this.javaVersionCache != null) {
            sender.sendMessage(Translate.color("&6Java: &7" + this.javaVersionCache));
        }

        // Disk space. Example Disk usage 100% (500GB/500GB)
        File file = new File("./");
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        long usedPercentSpace = (usedSpace * 100) / totalSpace;
        String usedSpaceInGB = String.valueOf(usedSpace / 1024 / 1024 / 1024);
        String totalSpaceInGB = String.valueOf(totalSpace / 1024 / 1024 / 1024);

        // Used percent color.
        String usedPercentColor = "";
        if (usedPercentSpace >= 90) {
            usedPercentColor = "<red>";
        } else if (usedPercentSpace >= 80) {
            usedPercentColor = "<yellow>";
        } else {
            usedPercentColor = "<green>";
        }

        sender.sendMessage(Translate.miniMessage("<gold>Disk usage: " + usedPercentColor + usedPercentSpace + "% <yellow>(<gold>" + usedSpaceInGB + "<yellow>/<gold>" + totalSpaceInGB + " GB<yellow>)"));

        // RAM Usage
        long maxMemory = (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        long allocatedMemory = (Runtime.getRuntime().totalMemory() / 1024 / 1024);
        long allocatedPercent = (allocatedMemory * 100) / maxMemory;
        long freeMemory = (Runtime.getRuntime().freeMemory() / 1024 / 1024);
        long freePercent = (freeMemory * 100) / maxMemory;
        long usedMemory = allocatedMemory - freeMemory;
        long usedPercent = (usedMemory * 100) / maxMemory;

        sender.sendMessage(Translate.color("&6Maximum memory: &c" + maxMemory + " MB."));
        sender.sendMessage(Translate.color("&6Allocated memory: &c" + allocatedMemory + " MB." + " &6(" + allocatedPercent + "%)"));
        sender.sendMessage(Translate.color("&6Used memory: &c" + usedMemory + " MB." + " &6(" + usedPercent + "%)"));
        sender.sendMessage(Translate.color("&6Free memory: &c" + freeMemory + " MB." + " &6(" + freePercent + "%)"));
    }
}

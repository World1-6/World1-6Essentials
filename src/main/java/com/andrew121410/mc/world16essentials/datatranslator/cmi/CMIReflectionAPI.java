package com.andrew121410.mc.world16essentials.datatranslator.cmi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class CMIReflectionAPI {

    // Unfortunately, the CMI-API doesn't use maven nor gradle, so we have to use reflection to use it, cries...

    public CMIReflectionAPI() {

    }

    //Homes

    public void addHome(UUID uuid, String name, Location location) {
        Object cmiPlayerManagerObject = getCMIPlayerManagerObject();
        Object cmiUserObject = getCMIUserObject(uuid, cmiPlayerManagerObject);
        addHome(name, location, cmiUserObject);
    }

    private void addHome(String name, Location location, Object cmiUserObject) {
        Object cmiLocationObject = makeCMILocationObject(location);
        Object cmiHomeObject = makeCMIHomeObject(name, cmiLocationObject);
        try {
            Class<?> aClass = cmiUserObject.getClass();
            Method method = aClass.getMethod("addHome", Object.class, Boolean.class);
            method.invoke(aClass, cmiHomeObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Location> getHomes(UUID uuid) {
        Object cmiPlayerManagerObject = getCMIPlayerManagerObject();
        Object cmiUserObject = getCMIUserObject(uuid, cmiPlayerManagerObject);
        LinkedHashMap<String, Object> rawHomes = (LinkedHashMap<String, Object>) getHomes(cmiUserObject);

        if (rawHomes == null) return null;

        HashMap<String, Location> homes = new HashMap<>();
        rawHomes.forEach((homeName, cmiHomeObject) -> homes.put(homeName, getLocationFromCMIHomeObject(cmiHomeObject)));
        return homes;
    }

    private Location getLocationFromCMIHomeObject(Object cmiHomeObject) {
        try {
            Class<?> aClass = cmiHomeObject.getClass();
            Method method = aClass.getMethod("getLoc");
            return (Location) method.invoke(cmiHomeObject); // CMILocation extends Location, so we can cast it
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getHomes(Object cmiUserObject) {
        try {
            Class<?> aClass = cmiUserObject.getClass();
            Method method = aClass.getMethod("getHomes");
            return method.invoke(cmiUserObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object makeCMIHomeObject(String homeName, Object cmiLocationObject) {
        try {
            Class<?> aClass = Class.forName("com.Zrips.CMI.Modules.Homes.CmiHome");
            Constructor<?> constructor = aClass.getConstructor(String.class, Object.class);
            return constructor.newInstance(homeName, cmiLocationObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getCMIUserObject(UUID uuid, Object cmiPlayerManagerObject) {
        try {
            Class<?> aClass = cmiPlayerManagerObject.getClass();
            Method method = aClass.getMethod("getUser", UUID.class);
            return method.invoke(aClass, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getCMIPlayerManagerObject() {
        try {
            Class<?> aClass = getCMIPluginClass();
            Method method = aClass.getMethod("getPlayerManager");
            return method.invoke(aClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Global - used by homes, and warps

    private Class<? extends JavaPlugin> getCMIPluginClass() {
        return Bukkit.getPluginManager().getPlugin("CMI").getClass().asSubclass(JavaPlugin.class);
    }

    private Object makeCMILocationObject(Location location) {
        try {
            Class<?> aClass = Class.forName("net.Zrips.CMILib.Container.CMILocation");
            Constructor<?> constructor = aClass.getConstructor(Location.class);
            return constructor.newInstance(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Warps

    public void addWarp(String name, Location location) {
        Object cmiWarpManagerObject = getCMIWarpManagerObject();

        Object cmiLocationObject = makeCMILocationObject(location);
        Object cmiWarpObject = makeCMIWarpObject(name, cmiLocationObject);

        addWarp(cmiWarpManagerObject, name, cmiWarpObject);
    }

    private void addWarp(Object cmiWarpManagerObject, String name, Object cmiWarpObject) {
        try {
            Class<?> aClass = cmiWarpManagerObject.getClass();
            Method method = aClass.getMethod("addWarp", String.class, Object.class);
            method.invoke(aClass, name, cmiWarpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Location> getWarps() {
        Object cmiWarpManagerObject = getCMIWarpManagerObject();
        List<Object> rawWarps = (List<Object>) getWarps(cmiWarpManagerObject);

        if (rawWarps == null) return null;

        Map<String, Location> warps = new HashMap<>();
        for (Object rawWarp : rawWarps) {
            String name = getWarpNameFromCMIWarpObject(rawWarp);
            Location location = getLocationFromCMIWarpObject(rawWarp);
            warps.put(name, location);
        }

        return warps;
    }

    private Object getWarps(Object cmiWarpManagerObject) {
        try {
            Class<?> aClass = cmiWarpManagerObject.getClass();
            Method method = aClass.getMethod("getWarps");
            return method.invoke(cmiWarpManagerObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Location getLocationFromCMIWarpObject(Object cmiWarpObject) {
        // Yes I know this is the same method as getLocationFromCMIHomeObject, but just in case in the future CMI changes the method name

        try {
            Class<?> aClass = cmiWarpObject.getClass();
            Method method = aClass.getMethod("getLoc");
            return (Location) method.invoke(cmiWarpObject); // CMILocation extends Location, so we can cast it
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getWarpNameFromCMIWarpObject(Object cmiWarpObject) {
        try {
            Class<?> aClass = cmiWarpObject.getClass();
            Method method = aClass.getMethod("getName");
            return (String) method.invoke(cmiWarpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object makeCMIWarpObject(String name, Object cmiLocationObject) {
        try {
            Class<?> aClass = Class.forName("com.Zrips.CMI.Modules.Warps.CmiWarp");
            Constructor<?> constructor = aClass.getConstructor(String.class, Object.class);
            return constructor.newInstance(name, cmiLocationObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getCMIWarpManagerObject() {
        try {
            Class<?> aClass = getCMIPluginClass();
            Method method = aClass.getMethod("getWarpManager");
            return method.invoke(aClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

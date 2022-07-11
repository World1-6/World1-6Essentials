package com.andrew121410.mc.world16utils.time;

import com.andrew121410.ccutils.utils.StringDataTimeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CountdownTimer implements Runnable {

    private final JavaPlugin plugin;

    private Integer assignedTaskId;

    private final int seconds;
    private int secondsLeft;

    private final Consumer<CountdownTimer> everySecond;
    private Runnable beforeTimer;
    private final Runnable afterTimer;

    public CountdownTimer(JavaPlugin plugin, int seconds, Runnable beforeTimer, Runnable afterTimer, Consumer<CountdownTimer> everySecond) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = seconds;
        this.beforeTimer = beforeTimer;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    public CountdownTimer(JavaPlugin plugin, int seconds, int secondsLeft, Runnable afterTimer, Consumer<CountdownTimer> everySecond) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = secondsLeft;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    /**
     * Runs the timer once, decrements seconds etc...
     * Really wish we could make it protected/private so you couldn't access it
     */
    @Override
    public void run() {
        // Is the timer up?
        if (secondsLeft < 1) {
            // Do what was supposed to happen after the timer
            afterTimer.run();

            // Cancel timer
            if (assignedTaskId != null) Bukkit.getScheduler().cancelTask(assignedTaskId);
            return;
        }

        // Are we just starting?
        if (secondsLeft == seconds) beforeTimer.run();

        // Do what's supposed to happen every second
        everySecond.accept(this);

        // Decrement the seconds left
        secondsLeft--;
    }

    public void scheduleTimer() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }

    @Deprecated
    public void scheduleTimerAsync() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 0L, 20L);
    }

    public void cancelTimer() {
        Bukkit.getScheduler().cancelTask(this.assignedTaskId);
    }

    public int getTotalSeconds() {
        return seconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public String getFancyTimeLeft(boolean shortText) {
        long secondsM = TimeUnit.SECONDS.toMillis(this.seconds);
        long secondsLeftM = TimeUnit.SECONDS.toMillis(this.secondsLeft);

        // Uses milliseconds
        return StringDataTimeBuilder.makeIntoEnglishWords(secondsM, secondsLeftM, shortText, true);
    }

    public Integer getAssignedTaskId() {
        return assignedTaskId;
    }
}
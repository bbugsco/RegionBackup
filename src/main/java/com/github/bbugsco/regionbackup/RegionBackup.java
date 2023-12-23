package com.github.bbugsco.regionbackup;

import org.bukkit.Bukkit;
import java.util.ArrayList;

public final class RegionBackup extends org.bukkit.plugin.java.JavaPlugin {

    public static java.util.ArrayList<Region> activeRegions;

    @Override
    public void onEnable() {
		saveDefaultConfig();
		if (getConfig().getBoolean("debug_commands")) {
            java.util.Objects.requireNonNull(getCommand("regionbackup")).setExecutor(new RegionBackupCommands(this));
		}
        activeRegions = new ArrayList<>();
        runTasks();
    }

    @SuppressWarnings("deprecation")
    private void runTasks() {
        long currentTimeMillis = System.currentTimeMillis();
        long nextHour = getNextHourMillis();
        long initialDelay = nextHour - currentTimeMillis;

        // Hourly backup task
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new UpdateActiveRegionsTask(), initialDelay * 50L, 72000L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new com.github.bbugsco.regionbackup.tasks.RegionBackupTask(this), initialDelay * 50L, 72000L) ;

        // 10-second delay check player location for loaded regions
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new com.github.bbugsco.regionbackup.tasks.UpdateActiveRegionsTask(), 0L, getConfig().getInt("check_active_regions_interval") * 20L);
    }

    private static long getNextHourMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        long currentHourMillis = currentTimeMillis - (currentTimeMillis % 3_600_000);
        return currentHourMillis + 3_600_000;
    }

}
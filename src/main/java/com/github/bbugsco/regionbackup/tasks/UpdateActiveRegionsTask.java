package com.github.bbugsco.regionbackup.tasks;

import com.github.bbugsco.regionbackup.Region;
import com.github.bbugsco.regionbackup.RegionBackup;
import org.bukkit.Bukkit;

public class UpdateActiveRegionsTask implements Runnable {

    @Override
    public void run() {
        updateActiveRegions();
    }

    private Region getRegion(org.bukkit.Location location) {
        if (location.getWorld() == null) return new Region(0, 0, Bukkit.getWorlds().get(0).getName());
        return new Region(location.getBlockX() >> 9, location.getBlockZ() >> 9, location.getWorld().getName());
    }

    private void updateActiveRegions() {
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            Region region = getRegion(player.getLocation());
            if (!RegionBackup.activeRegions.contains(region)) RegionBackup.activeRegions.add(region);
        }
    }

}
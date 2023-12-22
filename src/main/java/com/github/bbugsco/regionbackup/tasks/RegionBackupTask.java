package com.github.bbugsco.regionbackup.tasks;

import com.github.bbugsco.regionbackup.RegionBackup;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegionBackupTask implements Runnable {

    private final RegionBackup plugin;

    public RegionBackupTask(RegionBackup plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        backupRegions();
    }

    public void backupRegions() {
        if (RegionBackup.activeRegions.isEmpty()) return;

        // Root directory of backup folder
        String backupPath = plugin.getConfig().getString("backup_path");

        // Get Time in format yyyy-MM-dd_HH:mm
        Timestamp nextHour = new Timestamp(System.currentTimeMillis());
        String formattedString = formatTimestamp(nextHour);

        // Create folder with datetime
        File outputDir = new File(backupPath + "/" + formattedString);

        // Check if directory was created
        boolean dirCreated = outputDir.mkdirs();
        if (!dirCreated) plugin.getLogger().warning("Failed to create directory for backup: does the directory already exist? " + outputDir.getAbsolutePath());

        // Copy each region to the backup folder
        for (com.github.bbugsco.regionbackup.Region region : RegionBackup.activeRegions) {
            String regionName =  "r." + region.x + "." + region.y + ".mca";

            File source = new File(region.worldName + "/region/" + regionName);
            File destination = new File(outputDir + "/" + region.worldName + "/" + regionName);

            try {
                org.apache.commons.io.FileUtils.copyFile(source, destination);
            } catch (java.io.IOException exception) {
                plugin.getLogger().warning("Error copying file");
                exception.fillInStackTrace();
            }
        }

        RegionBackup.activeRegions = new java.util.ArrayList<>();
    }

    private static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return null;
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        return dateFormat.format(date);
    }

}
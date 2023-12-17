package com.github.bbugsco.regionbackup;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegionBackupTasks {

	private final RegionBackup plugin;

	private final ScheduledExecutorService scheduler;
	private ArrayList<Region> activeRegions;

	public RegionBackupTasks(RegionBackup plugin) {
		this.plugin = plugin;

		scheduler = Executors.newScheduledThreadPool(1);
		activeRegions = new ArrayList<>();

		long currentTimeMillis = System.currentTimeMillis();
		long nextHour = getNextHourMillis();
		long initialDelay = nextHour - currentTimeMillis;

		// Hourly backup task
		this.scheduler.scheduleAtFixedRate(this::backupRegions, initialDelay, 1, TimeUnit.HOURS);

		// 10-second delay check player location for loaded regions
		this.scheduler.scheduleAtFixedRate(this::updateActiveRegions, 0, 10, TimeUnit.SECONDS);
	}

	public void shutdown() {
		this.scheduler.shutdown();
	}

	public ArrayList<Region> getActiveRegions() {
		return this.activeRegions;
	}

	public void clearActiveRegions() {
		this.activeRegions = new ArrayList<>();
	}

	public void backupRegions() {
		String PATH = "home/bbugsco/server/backup";

		Timestamp nextHour = new Timestamp(System.currentTimeMillis());
		String formattedString = formatTimestamp(nextHour);

		File outputDir = new File(PATH + "/" + formattedString);
		boolean dirCreated = outputDir.mkdirs();
		if (!dirCreated) plugin.getLogger().warning("Failed to create directory for backup: does the directory already exist? " + outputDir.getAbsolutePath());

		for (World world : Bukkit.getWorlds()) {
			world.save();
		}

		for (Region region : activeRegions) {
			String regionName =  "r." + region.x + "." + region.y + ".mca";

			File source = new File(region.worldName + "/region/" + regionName);
			File destination = new File(PATH + "/" + region.worldName + "/" + regionName);
			System.out.println("Source" + source.getAbsolutePath());
			System.out.println("Dest" + destination.getAbsolutePath());

			try {
				FileUtils.copyFile(source, destination);
			} catch (IOException exception) {
				plugin.getLogger().warning("Error");
			}
		}

		this.activeRegions = new ArrayList<>();
	}

	public void updateActiveRegions() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Region region = getRegion(player.getLocation());
			if (!this.activeRegions.contains(region)) this.activeRegions.add(region);
		}
		System.out.println(activeRegions);
	}

	private Region getRegion(Location location) {
		if (location.getWorld() == null) return new Region(0, 0, Bukkit.getWorlds().get(0).getName());
		return new Region(location.getBlockX() >> 9, location.getBlockZ() >> 9, location.getWorld().getName());
	}

	private static long getNextHourMillis() {
		long currentTimeMillis = System.currentTimeMillis();
		long currentHourMillis = currentTimeMillis - (currentTimeMillis % 3_600_000);
		return currentHourMillis + 3_600_000;
	}

	private static String formatTimestamp(Timestamp timestamp) {
		if (timestamp == null) return null;
		Date date = new Date(timestamp.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(date);
	}

}
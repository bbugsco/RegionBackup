package com.github.bbugsco.regionbackup;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegionBackupTasks {

	private final ScheduledExecutorService scheduler;
	private ArrayList<Region> activeRegions;
	// private String PATH = "home/bbugsco/server/backup"
	private final String PATH = "/home/benhi/serverBackup";
	private long nextHour;

	public RegionBackupTasks() {
		scheduler = Executors.newScheduledThreadPool(1);
		activeRegions = new ArrayList<>();

		long currentTimeMillis = System.currentTimeMillis();
		nextHour = getNextHourMillis();
		long initialDelay = nextHour - currentTimeMillis;

		// Hourly backup task
		this.scheduler.scheduleAtFixedRate(this::backupRegions, initialDelay, 1, TimeUnit.HOURS);
		System.out.println("added hourly task for " + new Timestamp(currentTimeMillis + initialDelay));

		// 10-second delay check player location for loaded regions
		this.scheduler.scheduleAtFixedRate(this::updateActiveRegions, 0, 10, TimeUnit.SECONDS);
		System.out.println("Created 10 second repeating task");
	}

	public void shutdown() {
		this.scheduler.shutdown();
	}

	private void updateActiveRegions() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Region region = getRegion(player.getLocation());
			if (!this.activeRegions.contains(region)) this.activeRegions.add(region);
		}
	}

	private void backupRegions() {
		Timestamp nextHour = new Timestamp(this.nextHour);
		String formattedString = formatTimestamp(nextHour);
		File outputDir = new File(PATH + "/" + formattedString);
		boolean dirCreated = outputDir.mkdirs();
		if (!dirCreated) System.out.println("Failed to create directory for backup: does the directory already exist? " + outputDir.getAbsolutePath());



		this.activeRegions = new ArrayList<>();
		this.nextHour = getNextHourMillis();
	}

	private Region getRegion(Location location) {
		return new Region(location.getBlockX() >> 9, location.getBlockZ() >> 9);
	}

	private static long getNextHourMillis() {
		long currentTimeMillis = System.currentTimeMillis();
		long currentHourMillis = currentTimeMillis - (currentTimeMillis % 3_600_000);
		return currentHourMillis + 3_600_000;
	}


	public static String formatTimestamp(Timestamp timestamp) {
		if (timestamp == null) return null;
		Date date = new Date(timestamp.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(date);
	}


}

package com.github.bbugsco.regionbackup;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegionBackupCommands implements CommandExecutor {

	RegionBackup plugin;

	public RegionBackupCommands(RegionBackup plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (sender.hasPermission("regionbackup.command")) {
			switch (alias) {
				case "forcebackup":
					plugin.getBackupTasks().backupRegions();
					break;
				case "clearactiveregions":
					plugin.getBackupTasks().clearActiveRegions();
					break;
				case "showactiveregions":
					ArrayList<Region> regions = plugin.getBackupTasks().getActiveRegions();
					for (Region region : regions) {
						sender.sendMessage(region.toString());
					}
			}
		}
		return true;
	}



}

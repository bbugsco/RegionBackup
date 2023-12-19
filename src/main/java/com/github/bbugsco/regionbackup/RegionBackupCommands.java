package com.github.bbugsco.regionbackup;

import com.github.bbugsco.regionbackup.tasks.UpdateActiveRegionsTask;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class RegionBackupCommands implements org.bukkit.command.CommandExecutor {

	RegionBackup plugin;

	public RegionBackupCommands(RegionBackup plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull org.bukkit.command.CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
		if (sender.hasPermission("regionbackup.command")) {
			switch (alias) {
				case "debug_forcebackup":
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, new com.github.bbugsco.regionbackup.tasks.RegionBackupTask(plugin));
					break;
				case "debug_clearactiveregions":
					RegionBackup.activeRegions = new ArrayList<>();
					break;
				case "debug_showactiveregions":
					for (Region region : RegionBackup.activeRegions) {
						sender.sendMessage(region.toString());
					}
					break;
				case "debug_updateactiveregions":
                    Bukkit.getScheduler().runTask(plugin, new UpdateActiveRegionsTask());
					break;
			}
		}
		return true;
	}



}

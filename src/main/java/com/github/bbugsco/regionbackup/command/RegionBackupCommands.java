package com.github.bbugsco.regionbackup.command;

import com.github.bbugsco.regionbackup.Region;
import com.github.bbugsco.regionbackup.RegionBackup;
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
				case "debug_force_backup":
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, new com.github.bbugsco.regionbackup.tasks.RegionBackupTask(plugin));
					break;
				case "debug_clear_active_regions":
					RegionBackup.activeRegions = new ArrayList<>();
					break;
				case "debug_show_active_regions":
					for (Region region : RegionBackup.activeRegions) {
						sender.sendMessage(region.toString());
					}
					break;
				case "debug_update_active_regions":
                    Bukkit.getScheduler().runTask(plugin, new UpdateActiveRegionsTask());
					break;
			}
		}
		return true;
	}



}

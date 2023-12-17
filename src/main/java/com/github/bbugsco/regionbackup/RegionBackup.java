package com.github.bbugsco.regionbackup;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RegionBackup extends JavaPlugin {

	private RegionBackupTasks backupTasks;

    @Override
    public void onEnable() {
		saveDefaultConfig();
	    backupTasks = new RegionBackupTasks(this);
		if (getConfig().getBoolean("debug_commands")) {
			Objects.requireNonNull(getCommand("regionbackup")).setExecutor(new RegionBackupCommands(this));
		}
    }

    @Override
    public void onDisable() {
	    backupTasks.shutdown();
    }

	public RegionBackupTasks getBackupTasks() {
		return this.backupTasks;
	}

}

package com.github.bbugsco.regionbackup;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegionBackup extends JavaPlugin {

	private RegionBackupTasks backupTasks;

	/*
	TODO:
	 - add world to region
	 - save world before backup
	 - io logic
	 */

    @Override
    public void onEnable() {
	    backupTasks = new RegionBackupTasks();
    }

    @Override
    public void onDisable() {
	    backupTasks.shutdown();
    }


}

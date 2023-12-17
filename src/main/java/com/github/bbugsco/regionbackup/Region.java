package com.github.bbugsco.regionbackup;

import java.awt.*;
import java.util.Objects;

public class Region extends Point {

	public final String worldName;

	public Region(int x, int y, String worldName) {
		super(x, y);
		this.worldName = worldName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Region compareTo = (Region) obj;
		return this.x == compareTo.x && this.y == compareTo.y && Objects.equals(worldName, compareTo.worldName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, worldName);
	}

	@Override
	public String toString() {
		return "com.github.bbugsco.regionbackup.Region{" + "x=" + x + ", y=" + y + ", worldName=" + worldName + '}';
	}

}
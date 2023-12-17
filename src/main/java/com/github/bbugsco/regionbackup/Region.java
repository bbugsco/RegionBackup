package com.github.bbugsco.regionbackup;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;

public class Region extends Point {

	private final UUID worldID;

	public Region(int x, int y, UUID worldID) {
		super(x, y);
		this.worldID = worldID;
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
		return this.x == compareTo.x && this.y == compareTo.y && worldID == compareTo.worldID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, worldID);
	}

}

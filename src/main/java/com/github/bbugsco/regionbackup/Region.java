package com.github.bbugsco.regionbackup;

import java.awt.*;
import java.util.Objects;

public class Region extends Point {

	public Region(int x, int y) {
		super(x, y);
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
		return this.x == compareTo.x && this.y == compareTo.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

}

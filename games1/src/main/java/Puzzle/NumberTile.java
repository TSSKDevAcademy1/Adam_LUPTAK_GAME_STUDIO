package  Puzzle;

import java.io.Serializable;

public class NumberTile implements Serializable {
	/**
	 * Every tile will have unique number here is an definition of one tile
	 */
	private int value = 0;

	public NumberTile(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {

		return value + "";

	}

}

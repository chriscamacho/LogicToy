package uk.co.bedroomcoders.chrisc.LogicToy;

// enumeration of cardinal directions
public enum DIR {
	NORTH(0,-1),
	EAST(1,0),
	SOUTH(0,1),
	WEST(-1,0);

	private final int value1;
	private final int value2;

	private DIR(final int newValue1,final int newValue2) {
		value1 = newValue1;
		value2 = newValue2;
	}

	// never actually ended up using these!
	public int getXValue() { return value1; }
	public int getYValue() { return value2; }
	
	// used to find the reverse of the direction
	public DIR reverse() {
		DIR nd=null;
		if (this==DIR.NORTH) nd = DIR.SOUTH;	
		if (this==DIR.EAST) nd = DIR.WEST;	
		if (this==DIR.SOUTH) nd = DIR.NORTH;	
		if (this==DIR.WEST) nd = DIR.EAST;
		return nd;
	}
}

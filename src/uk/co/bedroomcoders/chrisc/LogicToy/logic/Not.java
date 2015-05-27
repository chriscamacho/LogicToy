package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;


public class Not extends LogicGate {
	
	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (i!=1) return "need exactly 1 input";
		if (o!=1) return "need exactly 1 output";
		return null;
	}
	
	public void updateState(stateEvent se) {
		
		Tile tile = se.Target;
		tile.outState=!se.newState;
		if (se.Direction.reverse()==DIR.NORTH) 	tile.inStateN=se.newState;
		if (se.Direction.reverse()==DIR.EAST) 	tile.inStateE=se.newState;
		if (se.Direction.reverse()==DIR.SOUTH)	tile.inStateS=se.newState;
		if (se.Direction.reverse()==DIR.WEST)	tile.inStateW=se.newState;
		
		int x=(int)tile.position.getX();
		int y=(int)tile.position.getY();
		if (tile.getIsOutput(DIR.NORTH)) new stateEvent(Main.currentTick+10,Main.grid[x][y-1],DIR.NORTH,tile.outState);
		if (tile.getIsOutput(DIR.EAST )) new stateEvent(Main.currentTick+10,Main.grid[x+1][y],DIR.EAST ,tile.outState);
		if (tile.getIsOutput(DIR.SOUTH)) new stateEvent(Main.currentTick+10,Main.grid[x][y+1],DIR.SOUTH,tile.outState);
		if (tile.getIsOutput(DIR.WEST )) new stateEvent(Main.currentTick+10,Main.grid[x-1][y],DIR.WEST ,tile.outState);
		

	}

}

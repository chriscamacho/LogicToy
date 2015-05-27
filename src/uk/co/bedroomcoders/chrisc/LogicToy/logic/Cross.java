package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;


public class Cross extends LogicGate {
	
	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (i!=2 || o!=2) return "There must be exactly two inputs and outputs";
		if (tile.getIsInput(DIR.NORTH)) { if (!tile.getIsOutput(DIR.SOUTH)) return "North/South must have 1 input and 1 output"; }
		if (tile.getIsInput(DIR.SOUTH)) { if (!tile.getIsOutput(DIR.NORTH)) return "North/South must have 1 input and 1 output"; }
		if (tile.getIsInput(DIR.EAST)) { if (!tile.getIsOutput(DIR.WEST)) return "East/West must have 1 input and 1 output"; }
		if (tile.getIsInput(DIR.WEST)) { if (!tile.getIsOutput(DIR.EAST)) return "East/West must have 1 input and 1 output"; }
		
		return null;
	}
	
	public void updateState(stateEvent se) {
		
		Tile tile = se.Target;
		int x=(int)tile.position.getX();
		int y=(int)tile.position.getY();
		if (se.Direction.reverse()==DIR.NORTH && tile.getIsInput(DIR.NORTH)) 	
			new stateEvent(Main.currentTick,Main.grid[x][y+1],DIR.SOUTH,se.newState);

		if (se.Direction.reverse()==DIR.EAST && tile.getIsInput(DIR.EAST)) 	
			new stateEvent(Main.currentTick,Main.grid[x-1][y],DIR.WEST,se.newState);

		if (se.Direction.reverse()==DIR.SOUTH && tile.getIsInput(DIR.SOUTH)) 	
			new stateEvent(Main.currentTick,Main.grid[x][y-1],DIR.NORTH,se.newState);

		if (se.Direction.reverse()==DIR.WEST && tile.getIsInput(DIR.WEST)) 	
			new stateEvent(Main.currentTick,Main.grid[x+1][y],DIR.EAST,se.newState);

	}
		
}

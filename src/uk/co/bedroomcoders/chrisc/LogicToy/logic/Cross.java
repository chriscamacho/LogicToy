package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;


public class Cross extends LogicGate {
	
	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (i!=2 || o!=2) return "There must be exactly two inputs and outputs";
		if (tile.getInput(DIR.NORTH)) { if (!tile.getOutput(DIR.SOUTH)) return "North/South must have 1 input and 1 output"; }
		if (tile.getInput(DIR.SOUTH)) { if (!tile.getOutput(DIR.NORTH)) return "North/South must have 1 input and 1 output"; }
		if (tile.getInput(DIR.EAST)) { if (!tile.getOutput(DIR.WEST)) return "East/West must have 1 input and 1 output"; }
		if (tile.getInput(DIR.WEST)) { if (!tile.getOutput(DIR.EAST)) return "East/West must have 1 input and 1 output"; }
		
		return null;
	}
	
	public void updateState(stateEvent se) {
		
		Tile tile = se.Target;
		int x=(int)tile.position.getX();
		int y=(int)tile.position.getY();
		if (se.Direction.reverse()==DIR.NORTH && tile.getInput(DIR.NORTH)) 	
			new stateEvent(Main.currentTick,Main.grid[x][y+1],DIR.SOUTH,se.newState);

		if (se.Direction.reverse()==DIR.EAST && tile.getInput(DIR.EAST)) 	
			new stateEvent(Main.currentTick,Main.grid[x-1][y],DIR.WEST,se.newState);

		if (se.Direction.reverse()==DIR.SOUTH && tile.getInput(DIR.SOUTH)) 	
			new stateEvent(Main.currentTick,Main.grid[x][y-1],DIR.NORTH,se.newState);

		if (se.Direction.reverse()==DIR.WEST && tile.getInput(DIR.WEST)) 	
			new stateEvent(Main.currentTick,Main.grid[x+1][y],DIR.EAST,se.newState);

	}
		
}

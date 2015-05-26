package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;

public class Wire extends LogicGate {
		
	public String invalid(Tile tile) {

		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (i>1) return "There can only be one input";
		if (i!=0 && o==0) return "missing an output";
		
		return null;
	}
	
	public void updateState(stateEvent se) {
		
		int x = (int)se.Target.position.getX();
		int y = (int)se.Target.position.getY();
		if (se.Target.getOutput(DIR.NORTH)) new stateEvent(Main.currentTick,Main.grid[x][y-1],DIR.NORTH,se.newState);
		if (se.Target.getOutput(DIR.EAST )) new stateEvent(Main.currentTick,Main.grid[x+1][y],DIR.EAST ,se.newState);
		if (se.Target.getOutput(DIR.SOUTH)) new stateEvent(Main.currentTick,Main.grid[x][y+1],DIR.SOUTH,se.newState);
		if (se.Target.getOutput(DIR.WEST )) new stateEvent(Main.currentTick,Main.grid[x-1][y],DIR.WEST ,se.newState);

	}

}

package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;


public class And extends LogicGate {
	
	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (i!=2) return "There must be two inputs";
		if (o!=1) return "There must be one output";		
		return null;
	}
	
	public void updateState(stateEvent se) {

		// set the input states from the stateEvent
		Tile tile = se.Target;
		if (se.Direction.reverse()==DIR.NORTH) 	tile.inStateN=se.newState;
		if (se.Direction.reverse()==DIR.EAST) 	tile.inStateE=se.newState;
		if (se.Direction.reverse()==DIR.SOUTH)	tile.inStateS=se.newState;
		if (se.Direction.reverse()==DIR.WEST)	tile.inStateW=se.newState;
		
		boolean ns = tile.inStateN;
		boolean es = tile.inStateE;
		boolean ss = tile.inStateS;
		boolean ws = tile.inStateW;
		
		if (tile.getInput(DIR.NORTH)==false) ns = true;
		if (tile.getInput(DIR.EAST )==false) es = true;
		if (tile.getInput(DIR.SOUTH)==false) ss = true;
		if (tile.getInput(DIR.WEST )==false) ws = true;
		// set none inputs high so we can AND all of them together
		
		// set the output state and create a new stateEvent
		tile.outState = ns & es & ss & ws;

		int x=(int)tile.position.getX();
		int y=(int)tile.position.getY();
		if (se.Target.getOutput(DIR.NORTH)) new stateEvent(Main.currentTick+10,Main.grid[x][y-1],DIR.NORTH,tile.outState);
		if (se.Target.getOutput(DIR.EAST )) new stateEvent(Main.currentTick+10,Main.grid[x+1][y],DIR.EAST ,tile.outState);
		if (se.Target.getOutput(DIR.SOUTH)) new stateEvent(Main.currentTick+10,Main.grid[x][y+1],DIR.SOUTH,tile.outState);
		if (se.Target.getOutput(DIR.WEST )) new stateEvent(Main.currentTick+10,Main.grid[x-1][y],DIR.WEST ,tile.outState);

	}
		
}

package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;

public class Xor extends LogicGate {
		
	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();		
		if (i!=2) return "There must be two inputs";
		if (o!=1) return "There must be one output";
		return null;
	}
	
	public void updateState(stateEvent se) {
		
		Tile tile = se.Target;
		if (se.Direction.reverse()==DIR.NORTH) 	tile.inStateN=se.newState;
		if (se.Direction.reverse()==DIR.EAST) 	tile.inStateE=se.newState;
		if (se.Direction.reverse()==DIR.SOUTH)	tile.inStateS=se.newState;
		if (se.Direction.reverse()==DIR.WEST)	tile.inStateW=se.newState;


		boolean[] ins=new boolean[4];
		ins[0]=tile.getInput(DIR.NORTH);
		ins[1]=tile.getInput(DIR.EAST);
		ins[2]=tile.getInput(DIR.SOUTH);
		ins[3]=tile.getInput(DIR.WEST);
		boolean[] val=new boolean[4];
		val[0]=tile.inStateN;val[1]=tile.inStateE;val[2]=tile.inStateS;val[3]=tile.inStateW;  
		
		boolean f1=false;
		boolean v1=false,v2=false;
		
		for (int i=0;i<4;i++) {
			if (!f1) {
				if (ins[i]) {
					f1=true;v1=val[i];
				}
			} else {
				if (ins[i]) {
					v2=val[i];
					break;
				}
			}
		}

		tile.outState = v1 ^ v2;


		int x=(int)tile.position.getX();
		int y=(int)tile.position.getY();
		if (se.Target.getOutput(DIR.NORTH)) new stateEvent(Main.currentTick+10,Main.grid[x][y-1],DIR.NORTH,tile.outState);
		if (se.Target.getOutput(DIR.EAST )) new stateEvent(Main.currentTick+10,Main.grid[x+1][y],DIR.EAST ,tile.outState);
		if (se.Target.getOutput(DIR.SOUTH)) new stateEvent(Main.currentTick+10,Main.grid[x][y+1],DIR.SOUTH,tile.outState);
		if (se.Target.getOutput(DIR.WEST )) new stateEvent(Main.currentTick+10,Main.grid[x-1][y],DIR.WEST ,tile.outState);
		
	}

}

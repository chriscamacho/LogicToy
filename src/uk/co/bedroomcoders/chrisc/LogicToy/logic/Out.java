package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;

public class Out extends LogicGate {
	
	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (o!=0) return "Outputs don't have in circuit outputs";
		if (i<1) return "Need at least 1 input";		
		return null;
	}
	
	public void updateState(stateEvent se) {
		se.Target.outState=se.newState;
		se.Target.Vstate.setVisible(se.Target.outState);
	}
	
}

package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;


public class In extends LogicGate {

	public String invalid(Tile tile) {
		int i = tile.countInputs();
		int o = tile.countOutputs();
		if (i!=0) return "Inputs don't have in circuit inputs";
		if (o<1) return "Need at least 1 output";
		return null;
	}
	
	// in is a special case
	public void updateState(stateEvent se) {
		// user updates state with right click
		// loading grid updates all circiut inputs
		// TODO should editing a tile trigger all inputs ?
	}

}

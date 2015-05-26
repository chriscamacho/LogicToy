package uk.co.bedroomcoders.chrisc.LogicToy;

import java.util.LinkedList;

public class stateEvent {
	
	public static LinkedList<stateEvent> events = new LinkedList<stateEvent>();
	
	public long Tick; // the tick the event should be actioned
	public Tile Target; 
	public DIR Direction; // source to destination direction
	public boolean newState; // the state of the source output
	public boolean processed;
	
	public stateEvent(long tick, Tile target, DIR direction, boolean state) {

		processed = false;
		Tick = tick;
		Target = target;
		Direction = direction;
		newState = state;
		events.add(this);
		
	}
	
	// attempt to "solve" feedback loops
	boolean equals(stateEvent other) {
		//if (Tick!=other.Tick) return false;
		if (Target!=other.Target) return false;
		if (Direction!=other.Direction) return false;
		if (newState!=other.newState) return false;
		return true;
	}
}

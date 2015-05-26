package uk.co.bedroomcoders.chrisc.LogicToy;
import uk.co.bedroomcoders.chrisc.LogicToy.logic.*;

import javafx.scene.*;
import javafx.scene.image.*;
import javafx.geometry.Point2D;

public class Tile extends Group {
	
	public LogicGate logicType = new Wire();
	public boolean needsUpdate = false;
	private boolean inN, inE, inS, inW;
	private boolean outN,outE,outS,outW;
	
	public boolean inStateN, inStateE, inStateS, inStateW;
	public boolean outState; // all outputs are always the same!
	
	protected static Image Iinwire,Ioutwire,Iand,Iinout,Icross;
	protected static Image Inot,Ior,Ion,Iinvalid,Ibg,Ievent;
	
	private ImageView VinN, VinE, VinS, VinW;
	private ImageView VoutN,VoutE,VoutS,VoutW;
	private ImageView Vinvalid;
	private ImageView Vbg;
	public ImageView Vlogic,Vstate;
	
	
	public ImageView Vevent;
		
	public Point2D position;
	
	public static void loadGraphics() {
		Ibg			= new Image("gfx/bg.png");
		Iinwire		= new Image("gfx/inwire.png");
		Ioutwire	= new Image("gfx/outwire.png");
		Icross		= new Image("gfx/cross.png");
		Iand		= new Image("gfx/and.png");
		Iinout		= new Image("gfx/inout.png");
		Inot		= new Image("gfx/not.png");
		Ior			= new Image("gfx/or.png");
		Ion			= new Image("gfx/on.png");
		Iinvalid	= new Image("gfx/invalid.png");
		Ievent		= new Image("gfx/event.png");
	}
	
	Tile(int x, int y) {
		
		if (Ibg==null) {
			throw new RuntimeException("You must call loadGraphics before creating an instance of tile");
		}
		
		position = new Point2D(x,y);
		setTranslateX(x*34+2);
		setTranslateY(y*34+2);

		
		Vbg = new ImageView(Ibg);
		getChildren().add(Vbg);
		
		VinN = new ImageView(Iinwire);
		getChildren().add(VinN);
		VinN.setRotate(270);
		VinN.setVisible(false);
		
		VinE = new ImageView(Iinwire);
		getChildren().add(VinE);
		VinE.setVisible(false);
		
		VinS = new ImageView(Iinwire);
		getChildren().add(VinS);
		VinS.setRotate(90);
		VinS.setVisible(false);
		
		VinW = new ImageView(Iinwire);
		getChildren().add(VinW);
		VinW.setRotate(180);
		VinW.setVisible(false);

		
		VoutN = new ImageView(Ioutwire);
		VoutN.setRotate(270);
		getChildren().add(VoutN);
		VoutN.setVisible(false);
		
		VoutE = new ImageView(Ioutwire);
		getChildren().add(VoutE);
		VoutE.setVisible(false);
		
		VoutS = new ImageView(Ioutwire);
		getChildren().add(VoutS);
		VoutS.setRotate(90);
		VoutS.setVisible(false);
		
		VoutW = new ImageView(Ioutwire);
		getChildren().add(VoutW);
		VoutW.setRotate(180);
		VoutW.setVisible(false);

		
		Vlogic = new ImageView(Iand);
		getChildren().add(Vlogic);
		Vlogic.setVisible(false);
		
		Vstate = new ImageView(Ion);
		getChildren().add(Vstate);
		Vstate.setVisible(false);
		
		Vinvalid = new ImageView(Iinvalid);
		getChildren().add(Vinvalid);
		Vinvalid.setVisible(false);

		Vevent = new ImageView(Ievent);
		getChildren().add(Vevent);
		Vevent.setUserData(this);
		Vevent.setPickOnBounds(true);  
	}
	
	public void updateGraphics() {
		VoutN.setVisible(outN);
		VoutE.setVisible(outE);
		VoutS.setVisible(outS);
		VoutW.setVisible(outW);
		
		VinN.setVisible(inN);
		VinE.setVisible(inE);
		VinS.setVisible(inS);
		VinW.setVisible(inW);
	}
	
	public void setSideOutput(DIR side, boolean out) {
		
		if (side==DIR.NORTH)	outN = out; 
		if (side==DIR.EAST)		outE = out; 
		if (side==DIR.SOUTH)	outS = out; 
		if (side==DIR.WEST)		outW = out; 
		
		updateGraphics();
	}
	
	public void setSideInput(DIR side, boolean in) {

		if (side==DIR.NORTH)	inN = in; 
		if (side==DIR.EAST)		inE = in; 
		if (side==DIR.SOUTH)	inS = in; 
		if (side==DIR.WEST)		inW = in; 
		
		updateGraphics();
	}
	
	public boolean getInput(DIR side) {
		if (side==DIR.NORTH)	return inN; 
		if (side==DIR.EAST)		return inE; 
		if (side==DIR.SOUTH)	return inS; 
		if (side==DIR.WEST)		return inW;
		return false; 		
	}
	
	public boolean getOutput(DIR side) {
		if (side==DIR.NORTH)	return outN; 
		if (side==DIR.EAST)		return outE; 
		if (side==DIR.SOUTH)	return outS; 
		if (side==DIR.WEST)		return outW;
		return false; 		
	}
	
	public int countInputs() {
		int c = 0;
		if (getInput(DIR.NORTH)) c++;
		if (getInput(DIR.EAST)) c++;
		if (getInput(DIR.SOUTH)) c++;
		if (getInput(DIR.WEST)) c++;
		return c;
	}

	public int countOutputs() {
		int c = 0;
		if (getOutput(DIR.NORTH)) c++;
		if (getOutput(DIR.EAST)) c++;
		if (getOutput(DIR.SOUTH)) c++;
		if (getOutput(DIR.WEST)) c++;
		return c;
	}
	
	public String toXml() {
		String out = new String();
		out =	"    <tile>\n";
		out+=	"        <pos x=\""+((int)position.getX())+"\" y=\""+((int)position.getY())+"\" />\n";
		out+=	"        <type logic=\""+logicType.getClass().getSimpleName()+"\" />\n";
		out+=	"        <outputs north=\""+getOutput(DIR.NORTH)+"\" east=\""+getOutput(DIR.EAST)+"\" south=\""+getOutput(DIR.SOUTH)+"\" west=\""+getOutput(DIR.WEST)+"\" />\n";
		out+=	"        <inputs north=\""+getInput(DIR.NORTH)+"\" east=\""+getInput(DIR.EAST)+"\" south=\""+getInput(DIR.SOUTH)+"\" west=\""+getInput(DIR.WEST)+"\" />\n";
		out+=	"    </tile>\n";
		return out;
	}
	
	// TODO give each logic instance it own image instance
	public void setLogicVisual(LogicGate v) {

		//gateNames = { "Wire", "In", "Out", "Cross", "Not", "Or", "And" };
		if (v instanceof Wire) Vlogic.setVisible(false);
		if (v instanceof In) {
				Vlogic.setImage(Tile.Iinout);
				Vlogic.setVisible(true);
		}
		if (v instanceof Out) {
				Vlogic.setImage(Tile.Iinout);
				Vlogic.setVisible(true);
		}
		if (v instanceof Cross) {
				Vlogic.setImage(Tile.Icross);
				Vlogic.setVisible(true);
		}
		if (v instanceof Not) {
				Vlogic.setImage(Tile.Inot);
				Vlogic.setVisible(true);
		}
		if (v instanceof Or) {
				Vlogic.setImage(Tile.Ior);
				Vlogic.setVisible(true);
		}
		if (v instanceof And) {
				Vlogic.setImage(Tile.Iand);
				Vlogic.setVisible(true);
		}
	}	
}

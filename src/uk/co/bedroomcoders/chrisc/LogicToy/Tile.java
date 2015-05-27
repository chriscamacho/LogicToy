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
	
	private static Image Iinwire,Ioutwire,Ion,Ibg,Ievent;
	
	private ImageView VinN, VinE, VinS, VinW;
	private ImageView VoutN,VoutE,VoutS,VoutW;
	private ImageView Vbg;
	public ImageView Vlogic,Vstate;
	
	
	public ImageView Vevent;
		
	public Point2D position;
	
	public static void loadGraphics() {
		Ibg			= new Image("gfx/bg.png");
		Iinwire		= new Image("gfx/inwire.png");
		Ioutwire	= new Image("gfx/outwire.png");
		Ion			= new Image("gfx/on.png");
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

		
		Vlogic = new ImageView();
		Wire w=new Wire();
		setLogicVisual(w);
		
		getChildren().add(Vlogic);
		
		Vstate = new ImageView(Ion);
		getChildren().add(Vstate);
		Vstate.setVisible(false);
		
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
	
	public boolean getIsInput(DIR side) {
		if (side==DIR.NORTH)	return inN; 
		if (side==DIR.EAST)		return inE; 
		if (side==DIR.SOUTH)	return inS; 
		if (side==DIR.WEST)		return inW;
		return false; 		
	}
	
	public boolean getIsOutput(DIR side) {
		if (side==DIR.NORTH)	return outN; 
		if (side==DIR.EAST)		return outE; 
		if (side==DIR.SOUTH)	return outS; 
		if (side==DIR.WEST)		return outW;
		return false; 		
	}
	
	public int countInputs() {
		int c = 0;
		if (getIsInput(DIR.NORTH)) c++;
		if (getIsInput(DIR.EAST)) c++;
		if (getIsInput(DIR.SOUTH)) c++;
		if (getIsInput(DIR.WEST)) c++;
		return c;
	}

	public int countOutputs() {
		int c = 0;
		if (getIsOutput(DIR.NORTH)) c++;
		if (getIsOutput(DIR.EAST)) c++;
		if (getIsOutput(DIR.SOUTH)) c++;
		if (getIsOutput(DIR.WEST)) c++;
		return c;
	}
	
	public String toXml() {
		String out = new String();
		out =	"    <tile>\n";
		out+=	"        <pos x=\""+((int)position.getX())+"\" y=\""+((int)position.getY())+"\" />\n";
		out+=	"        <type logic=\""+logicType.getClass().getSimpleName()+"\" />\n";
		out+=	"        <outputs north=\""+getIsOutput(DIR.NORTH)+"\" east=\""+getIsOutput(DIR.EAST)+
					"\" south=\""+getIsOutput(DIR.SOUTH)+"\" west=\""+getIsOutput(DIR.WEST)+"\" />\n";
		out+=	"        <inputs north=\""+getIsInput(DIR.NORTH)+"\" east=\""+getIsInput(DIR.EAST)+
					"\" south=\""+getIsInput(DIR.SOUTH)+"\" west=\""+getIsInput(DIR.WEST)+"\" />\n";
		out+=	"    </tile>\n";
		return out;
	}
	
	// TODO give each logic instance it own image instance
	public void setLogicVisual(LogicGate v) {
		Vlogic.setImage(v.getImage());
		/*
		//gateNames = { "Wire", "In", "Out", "Cross", "Not", "Or", "And" };
		if (v instanceof Wire) Vlogic.setImage(Tile.Iwire);
		if (v instanceof In) Vlogic.setImage(Tile.Iin);
		if (v instanceof Out) Vlogic.setImage(Tile.Iout);
		if (v instanceof Cross) Vlogic.setImage(Tile.Icross);
		if (v instanceof Not) Vlogic.setImage(Tile.Inot);
		if (v instanceof Or) Vlogic.setImage(Tile.Ior);
		if (v instanceof And) Vlogic.setImage(Tile.Iand);
		if (v instanceof Xor) Vlogic.setImage(Tile.Ixor);
		*/
	}	
}

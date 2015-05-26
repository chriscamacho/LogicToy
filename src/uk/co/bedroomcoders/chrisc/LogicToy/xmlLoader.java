package uk.co.bedroomcoders.chrisc.LogicToy;
import uk.co.bedroomcoders.chrisc.LogicToy.logic.*;

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.lang.reflect.Constructor;

public class xmlLoader {
	
	public static void load(String filename,Tile[][] newgrid) {
		try {

			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("tile");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				NodeList cList = nNode.getChildNodes();
				Tile currentTile=null;
				int x,y;
				for (int i=0;i<cList.getLength();i++) {
					Node cNode = cList.item(i);

					if (cNode.getNodeName().equals("pos")) {
						x=Integer.parseInt(((Element)cNode).getAttribute("x"));
						y=Integer.parseInt(((Element)cNode).getAttribute("y"));
						currentTile = newgrid[x][y];
					} 
					if (cNode.getNodeName().equals("type")) {
						
						String li = (((Element)cNode).getAttribute("logic"));//Integer.parseInt(((Element)cNode).getAttribute("logic"));

						LogicGate lg = null;
						
						lg = LogicGate.createInstance(li);
									
						currentTile.logicType = lg;
						currentTile.setLogicVisual(lg);
					}
					if (cNode.getNodeName().equals("inputs")) {
						currentTile.setSideInput(DIR.NORTH, Boolean.parseBoolean(((Element)cNode).getAttribute("north")));
						currentTile.setSideInput(DIR.EAST, Boolean.parseBoolean(((Element)cNode).getAttribute("east")));
						currentTile.setSideInput(DIR.SOUTH, Boolean.parseBoolean(((Element)cNode).getAttribute("south")));
						currentTile.setSideInput(DIR.WEST, Boolean.parseBoolean(((Element)cNode).getAttribute("west")));
					}
					if (cNode.getNodeName().equals("outputs")) {
						currentTile.setSideOutput(DIR.NORTH, Boolean.parseBoolean(((Element)cNode).getAttribute("north")));
						currentTile.setSideOutput(DIR.EAST, Boolean.parseBoolean(((Element)cNode).getAttribute("east")));
						currentTile.setSideOutput(DIR.SOUTH, Boolean.parseBoolean(((Element)cNode).getAttribute("south")));
						currentTile.setSideOutput(DIR.WEST, Boolean.parseBoolean(((Element)cNode).getAttribute("west")));
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

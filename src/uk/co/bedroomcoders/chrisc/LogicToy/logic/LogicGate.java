package uk.co.bedroomcoders.chrisc.LogicToy.logic;
import uk.co.bedroomcoders.chrisc.LogicToy.*;

import java.util.ArrayList;
import java.io.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public abstract class LogicGate {

	private static HashMap<Class<?>,Integer> indexes= new HashMap<Class<?>,Integer>();

	// index is only for runtime, class type to index used for ChoiceBox
	public int getIndex() {
		return indexes.get(this.getClass());
	}
	
	public static String packageName;
	public static String[] gateNames;
	
	// from a String "And" "Not" etc create a LogicGate instance
	public static LogicGate createInstance(String name) {
		Class<?> clazz=null;
		Constructor<?> ctor=null;
		LogicGate lg=null;
		try {
			clazz = Class.forName(LogicGate.packageName+"."+name);
			ctor = clazz.getConstructor();
			lg = (LogicGate)ctor.newInstance();
		} catch (Exception e) { System.out.println(e); }
		return lg;
	}
		
	static {
		// use a concrete (ie not abstract) logic type to find the package name
		Wire w = new Wire();
		Package pack = w.getClass().getPackage();
		packageName = pack.getName();
		
		//System.out.println("Package = " + packageName);
		
		File folder = new File("./bin/"+packageName.replace(".","/"));
		File[] listOfFiles = folder.listFiles();
		
		ArrayList<String> g = new ArrayList<String>();
		for (File f: listOfFiles) {
			String n = f.getName().replace(".class","");
			if (!n.equals("LogicGate") && !n.equals("Wire")) g.add(n);
		}
		java.util.Collections.sort(g);
		g.add(0,"Wire"); // alphabetically sorted but with Wire first

		gateNames = g.toArray(new String[g.size()]);
		
		for (int i=0;i<g.size();i++) {
			LogicGate lg = createInstance(gateNames[i]);
			//lg.setIndex(i);
			indexes.put(lg.getClass(),new Integer(i));
		}
	}

	// when the user edit a tile have the created an invalid configuration
	// if so return an *meaningful* error message, null equals valid
	abstract public String invalid(Tile tile);
	
	// When a tile recieves a state change event this routine is used
	// to calculate the outputs (firing state changes on its outputs)
	abstract public void updateState(stateEvent se);

}


package uk.co.bedroomcoders.chrisc.LogicToy;
import uk.co.bedroomcoders.chrisc.LogicToy.logic.*;

import javafx.scene.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.application.*;
import javafx.scene.paint.*;
import javafx.scene.input.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.io.*;

public class Main extends Application {
	
	public static final int gridWidth = 58;
	public static final int gridHeight = 24;
	
	public static int currentTick = 0;
	
	protected Pane root;
	protected Scene scene;
	
	protected Stage mainStage;
	protected PropDialog propDialog;
	
	// stored in array for easy access by coordinate
	// prototype logic grid is grid based to fit in with a game idea... 
	
	// TODO each tile should have a back reference to the grid its on, rather
	// than having the grid public
	public static Tile[][] grid = new Tile[gridWidth][gridHeight];
	boolean leftDown,rightDown;
	ScrollPane scroller = new ScrollPane();
	
	
	public static void main(String[] args) { Application.launch(args); }
	
    @Override
    public void start(Stage stage) {
		
		mainStage = stage;
		
		root = new Pane();
		root.setPrefSize(gridWidth*34+4,gridHeight*34+4);
		scroller.setContent(root);


		final Menu fileMenu = new Menu("File");
		final Menu simMenu = new Menu("Sim");
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, simMenu);
		
		MenuItem newMenu = new MenuItem("New");
		fileMenu.getItems().add(newMenu);
		newMenu.setOnAction((e)->resetGrid());

		MenuItem loadMenu = new MenuItem("Load");
		fileMenu.getItems().add(loadMenu);
		loadMenu.setOnAction((e)->loadGrid());

		MenuItem saveMenu = new MenuItem("save");
		fileMenu.getItems().add(saveMenu);
		saveMenu.setOnAction((e)->saveGrid());

		MenuItem restateMenu = new MenuItem("reset state");
		simMenu.getItems().add(restateMenu);
		restateMenu.setOnAction((e)->resetState());
		

		VBox vb=new VBox();
		vb.getChildren().addAll(menuBar,scroller);
		
        scene = new Scene(vb);
        root.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Tile.loadGraphics();
		
		resetGrid();
		
		stage.setTitle("Logic Toy");
        stage.setScene(scene);

        stage.show();			
        stage.hide();
		stage.setWidth(640);
		stage.setHeight(480);
		stage.setMaxWidth(gridWidth*34+4); // 1.7976931348623157E308 really!
		//System.out.println("max/min size "+stage.getMaxWidth()+","+stage.getMinWidth());
        stage.show();	// work around for odd sizing interaction between scroller and stage
		
		
		
        scene.setOnKeyReleased(e->keyReleased(e));

	}
	
	public void mousePressed(MouseEvent me) {
		if (me.isPrimaryButtonDown()) leftDown=true;
		if (me.isSecondaryButtonDown()) rightDown=true;
	}
	
	public void mouseReleased(MouseEvent me) {
		
		if (leftDown) {
			Tile t = (Tile)((Node)me.getTarget()).getUserData();
			if (t==null) return;
			int x = (int)t.position.getX();
			int y = (int)t.position.getY();
			if (x==0 || y==0 || x==gridWidth-1 || y==gridHeight-1) return;

			if (propDialog==null) {
				propDialog=new PropDialog(mainStage,t);
				propDialog.showDialog();
			} else {
				propDialog.tile=t;
				propDialog.showDialog();
			}
			leftDown=false;
		}
		if (rightDown) {
			rightDown=false;
			Tile t = (Tile)((Node)me.getTarget()).getUserData();

			if (t.logicType instanceof In) { // toggle an input
				int x = (int)t.position.getX();
				int y = (int)t.position.getY();
				t.outState=!t.outState;
				t.Vstate.setVisible(t.outState);
				if (t.getIsOutput(DIR.NORTH)) new stateEvent(currentTick+10,grid[x][y-1],DIR.NORTH,t.outState);
				if (t.getIsOutput(DIR.EAST )) new stateEvent(currentTick+10,grid[x+1][y],DIR.EAST ,t.outState);
				if (t.getIsOutput(DIR.SOUTH)) new stateEvent(currentTick+10,grid[x][y+1],DIR.SOUTH,t.outState);
				if (t.getIsOutput(DIR.WEST )) new stateEvent(currentTick+10,grid[x-1][y],DIR.WEST ,t.outState);	
				doTick();			
			}
		}
	}
	
	public void resetGrid() {
		for (int y=0;y<gridHeight;y++) {
			for (int x=0;x<gridWidth;x++) {
				grid[x][y] = new Tile(x,y);
				root.getChildren().add(grid[x][y]);
				grid[x][y].Vevent.setOnMouseReleased((e)->mouseReleased(e));
				grid[x][y].Vevent.setOnMousePressed((e)->mousePressed(e));
			}
		}
	}
	
	public void resetState() {
		for (int y=0;y<gridHeight;y++) {
			for (int x=0;x<gridWidth;x++) {
				Tile tile = grid[x][y];

				if (tile.logicType instanceof In) { // input
					if (tile.getIsOutput(DIR.NORTH)) new stateEvent(currentTick,grid[x][y-1],DIR.NORTH,tile.outState);
					if (tile.getIsOutput(DIR.EAST )) new stateEvent(currentTick,grid[x+1][y],DIR.EAST ,tile.outState);
					if (tile.getIsOutput(DIR.SOUTH)) new stateEvent(currentTick,grid[x][y+1],DIR.SOUTH,tile.outState);
					if (tile.getIsOutput(DIR.WEST )) new stateEvent(currentTick,grid[x-1][y],DIR.WEST ,tile.outState);
				} 
			}
		}
		doTick();		
	}
	
	public void loadGrid() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open grid File");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile == null) return;

		resetGrid();
		xmlLoader.load(selectedFile.getAbsolutePath(),grid);
		
		resetState();
		mainStage.setMaxWidth(gridWidth*34+4); // TODO find out what causes this bug - see end of start method
		mainStage.setMaxHeight(gridHeight*34+4); // using the menu ???
		mainStage.hide();
		mainStage.show();
	}
	
	public void saveGrid() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save grid File");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showSaveDialog(mainStage);
		if (selectedFile == null) return;

		File fout = new File(selectedFile.getAbsolutePath());
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(fout);
		} catch(Exception e) {
			System.out.println(e);
			return;
		}
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		try {			
			bw.write("<grid>\n");
		} catch(Exception e) {
			System.out.println(e);
			return;
		}
		for (int y=0;y<gridHeight;y++) {
			for (int x=0;x<gridWidth;x++) {
				if (grid[x][y].logicType instanceof Wire) { // only output wire if it has out/inputs
					if (grid[x][y].getIsInput(DIR.NORTH) ||
						grid[x][y].getIsInput(DIR.EAST) ||
						grid[x][y].getIsInput(DIR.SOUTH) ||
						grid[x][y].getIsInput(DIR.WEST) ||
						grid[x][y].getIsOutput(DIR.NORTH) ||
						grid[x][y].getIsOutput(DIR.EAST) ||
						grid[x][y].getIsOutput(DIR.SOUTH) ||
						grid[x][y].getIsOutput(DIR.WEST) 
						) {
							try {
								bw.write(grid[x][y].toXml()+"\n");
							} catch(Exception e) {
								System.out.println(e);
								return;
							}							
						}
				} else { // else always output other types
					try {
						bw.write(grid[x][y].toXml()+"\n");
					} catch(Exception e) {
						System.out.println(e);
						return;
					}	
				}

			}
		}
		try {
			bw.write("</grid>\n");	
			bw.close();
		} catch(Exception e) {
			System.out.println(e);
			return;
		}				
	}
	
	public void keyReleased(KeyEvent ke) {
		
		if (ke.getCode() == KeyCode.S) { saveGrid(); } 
		if (ke.getCode() == KeyCode.L) { loadGrid(); }
		
	}
	
	public void doTick() {

		LinkedList<stateEvent> updateList = new LinkedList<stateEvent>();
		
		Iterator<stateEvent> it;
		boolean done=false;
		while (!done) {
			done=true;
			it = stateEvent.events.iterator();
			while (it.hasNext()) {
				stateEvent se = it.next();
				if (se.Tick<=currentTick && se.processed==false) {
					if (se.Target.getIsInput(se.Direction.reverse())==true) {
							done=false;
							se.processed=true;
							updateList.add(se); // will probably add to event list so need to do later to avoid comodification
					} else {
						se.processed=true;
					}
				}
			}
			
			for (Iterator<stateEvent> iterator = updateList.iterator(); iterator.hasNext(); ) {
				stateEvent se = iterator.next();
				se.Target.logicType.updateState(se);
			}
			updateList.clear();
			
			// attempt to trim duplicates caused by feedback loops (NOT a fix!)
			for (Iterator<stateEvent> iterator = stateEvent.events.iterator(); iterator.hasNext(); ) {
				stateEvent se = iterator.next(); 
				for (Iterator<stateEvent> iterator2 = stateEvent.events.iterator(); iterator2.hasNext(); ) {
					stateEvent se2 = iterator2.next();
					if (se!=se2) if (se2.equals(se)) se2.processed = true; 			
				}
			}
			
			for (Iterator<stateEvent> iterator = stateEvent.events.iterator(); iterator.hasNext(); ) {
				stateEvent se = iterator.next();
				if (se.processed) iterator.remove();
			}
			
			
		}


		currentTick++;				
		Timeline t = new Timeline( new KeyFrame(Duration.millis(2), e->doTick()));
		t.setCycleCount(1);
		t.play();


	}



}

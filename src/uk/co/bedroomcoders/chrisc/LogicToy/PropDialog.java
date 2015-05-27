package uk.co.bedroomcoders.chrisc.LogicToy;
import uk.co.bedroomcoders.chrisc.LogicToy.logic.*;

import javafx.stage.*;
import javafx.scene.*;
import javafx.event.*;
import javafx.application.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.beans.value.*;

import java.lang.reflect.Constructor;


public class PropDialog extends Stage {
	
	Stage				Parent;
	Group				root;
	VBox				vbox;
	HBox				hbox1,hbox2,hbox3,hboxBottom;
	CheckBox			CBinN,CBinE,CBinS,CBinW;
	CheckBox			CBoutN,CBoutE,CBoutS,CBoutW;
	ChoiceBox<String>	logicChoice;
	Label				errorLabel;
	Button 				ok,clear;

	Tile				tile;
	
	PropDialog(Stage parent, Tile editTile) {
		super(StageStyle.TRANSPARENT);
		Parent = parent;
		tile = editTile;

		root=new Group();
        Scene scene = new Scene(root);
		setScene(scene); 
        
		initModality(Modality.WINDOW_MODAL);
		initOwner(Parent);
		initStyle(StageStyle.DECORATED);
		
		vbox=new VBox();
		hbox1=new HBox();
		hbox2=new HBox();
		hbox3=new HBox();
		hboxBottom=new HBox();
		
		Insets i = new Insets(8);
		
		CBinN=makeCB("in N");
		CBinE=makeCB("in E");
		CBinS=makeCB("in S");
		CBinW=makeCB("in W");

		CBoutN=makeCB("out N");	
		CBoutE=makeCB("out E");	
		CBoutS=makeCB("out S");	
		CBoutW=makeCB("out W");	
		
		root.getChildren().add(vbox);
		vbox.getChildren().addAll(hbox1,hbox2,hbox3,hboxBottom);
		
		hbox1.getChildren().addAll(CBinN,CBinE,CBinS,CBinW);
		hbox2.getChildren().addAll(CBoutN,CBoutE,CBoutS,CBoutW);
		
		
		logicChoice = new ChoiceBox<String>();
		hbox3.setPadding(i);
		logicChoice.getItems().addAll(LogicGate.gateNames);
		logicChoice.getSelectionModel().selectedItemProperty().addListener((o, cv, nv)->choiceChanged(o,cv,nv));
		hbox3.getChildren().addAll(logicChoice); 
		
		ok = new Button("OK");
		ok.setOnAction((e)->onAction(e));
		ok.setPadding(i);
		hboxBottom.getChildren().add(ok);
				
		clear = new Button("Clear");
		clear.setOnAction((e)->onAction(e));
		clear.setPadding(i);
		hboxBottom.getChildren().add(clear);
		
		errorLabel = new Label();
		errorLabel.setTextFill(Color.web("#ff0000"));
		hboxBottom.getChildren().add(errorLabel);
	}

	private CheckBox makeCB(String l) {		
		Insets i = new Insets(8);
		CheckBox c=new CheckBox(l);
		c.setPadding(i);
		c.setOnAction((e)->onAction(e));
		return c;
	}
	
	public void showDialog() {
		show();
		
		CBinN.setSelected(tile.getIsInput(DIR.NORTH));
		CBinE.setSelected(tile.getIsInput(DIR.EAST));
		CBinS.setSelected(tile.getIsInput(DIR.SOUTH));
		CBinW.setSelected(tile.getIsInput(DIR.WEST));
		
		CBoutN.setSelected(tile.getIsOutput(DIR.NORTH));
		CBoutE.setSelected(tile.getIsOutput(DIR.EAST));
		CBoutS.setSelected(tile.getIsOutput(DIR.SOUTH));
		CBoutW.setSelected(tile.getIsOutput(DIR.WEST));
		

		logicChoice.getSelectionModel().clearAndSelect(tile.logicType.getIndex());
		
		Platform.runLater(()->centre());
	}
	
	public void choiceChanged(ObservableValue<? extends String> o, String cv, String nv) {
		
		int i = logicChoice.getSelectionModel().getSelectedIndex();
		if (tile.logicType.getIndex()!=i) {

			LogicGate lg=null;
			
			lg = LogicGate.createInstance(LogicGate.gateNames[i]);

			tile.logicType = lg;
			tile.setLogicVisual(lg);
		}
		
		
	
	}
	
	public void onAction(ActionEvent ae) {
		
		// all logic has this in common only one input OR output per side
		if (ae.getTarget() instanceof CheckBox) {
			CheckBox cb = (CheckBox)ae.getTarget();
			if (cb==CBinN && CBinN.isSelected()) { CBoutN.setSelected(false); }
			if (cb==CBinE && CBinE.isSelected()) { CBoutE.setSelected(false); }
			if (cb==CBinS && CBinS.isSelected()) { CBoutS.setSelected(false); }
			if (cb==CBinW && CBinW.isSelected()) { CBoutW.setSelected(false); }
			
			if (cb==CBoutN && CBoutN.isSelected()) { CBinN.setSelected(false); }
			if (cb==CBoutE && CBoutE.isSelected()) { CBinE.setSelected(false); }
			if (cb==CBoutS && CBoutS.isSelected()) { CBinS.setSelected(false); }
			if (cb==CBoutW && CBoutW.isSelected()) { CBinW.setSelected(false); }
			
			return;
		}

		if (ae.getTarget()==ok) {
			
			// first update the in/outputs
			tile.setSideInput(DIR.NORTH,CBinN.isSelected());
			tile.setSideInput(DIR.EAST,CBinE.isSelected());
			tile.setSideInput(DIR.SOUTH,CBinS.isSelected());
			tile.setSideInput(DIR.WEST,CBinW.isSelected());
			tile.setSideOutput(DIR.NORTH,CBoutN.isSelected());
			tile.setSideOutput(DIR.EAST,CBoutE.isSelected());
			tile.setSideOutput(DIR.SOUTH,CBoutS.isSelected());
			tile.setSideOutput(DIR.WEST,CBoutW.isSelected());
			
			String invalid = tile.logicType.invalid(tile);
			
			errorLabel.setText(invalid);
			
			if (invalid==null && ae.getSource()!=clear) hide(); // if valid and not from clear button
			return;
		}
		
		if (ae.getTarget()==clear) {
			CBinN.setSelected(false);
			CBinE.setSelected(false);
			CBinS.setSelected(false);
			CBinW.setSelected(false);
			CBoutN.setSelected(false);
			CBoutE.setSelected(false);
			CBoutS.setSelected(false);
			CBoutW.setSelected(false);
			logicChoice.getSelectionModel().clearAndSelect(0);
			ActionEvent nae= new ActionEvent(clear, ok);
			onAction(nae);
		}
		
	}

	
	public void centre() {
		setX(Parent.getX()+Parent.getWidth()/2-getWidth()/2);
		setY(Parent.getY()+Parent.getHeight()/2-getHeight()/2);
	}
	
}

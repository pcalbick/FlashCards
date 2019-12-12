package application.controller;

/*
 * 
 * Pretty sure unused and needs clean up
 * 
 */

import application.model.CardsModel;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class RootPaneController {
	
	@FXML
	MenuItem playButton;
	
	@FXML
	VBox container;
	
	private CardsModel model;
	
	public void handlePlay() {
		if(model == null)
			System.out.println("null");
		else
			System.out.println("not null");
	}
	
	public VBox getContainer() {
		return container;
	}
	
	public void init(CardsModel model) {
		this.model = model;
	}
}

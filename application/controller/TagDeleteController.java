package application.controller;

/*
 * 
 * Called when delete is pressed after hovering a tag
 * 
 */

import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TagDeleteController {
	
	@FXML
	Button yes;
	
	@FXML
	Button no;
	
	private Stage stage;
	private TagItemController itemController;
	private CardsModel model;
	private VBox container;
	private GridPane item;
	
	//Delete tag from master list of tags and tag from all created cards
	public void handleYes() {
		for(CardDataStructure c : model.getMaster()) {
			for(int i=0; i<c.getTags().size(); i++) {
				if(c.getTags().get(i).equalsIgnoreCase(itemController.getTag().getText())) {
					c.removeTag(itemController.getTag().getText());
					model.getTestList().remove(c);
				}
			}
		}
		model.deleteTag(model.getObservableTags().indexOf(itemController.getTag().getText()));
		
		container.getChildren().remove(item);
		
		stage.close();
	}
	
	public void handleNo() {
		stage.close();
	}
	
	public void init(Stage stage, TagItemController itemController, CardsModel model, VBox container, GridPane item) {
		this.stage = stage;
		this.model = model;
		
		this.itemController = itemController;
		this.item = item;
		
		this.container = container;
	}
}

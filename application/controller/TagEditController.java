package application.controller;

/*
 * 
 * Changes the tag in the master list as well 
 * 
 */

import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TagEditController {
	
	@FXML
	TextField tag;
	
	@FXML
	Button accept;
	
	@FXML
	Button delete;
	
	@FXML
	Button cancel;
	
	private TagItemController controller;
	private CardsModel model;
	private Stage stage;
	private String oldTag;
	
	public void handleAccept() {
		if(!model.getObservableTags().contains(tag.getText())) {
			if(tag.getStyleClass().contains("warning"))
				tag.getStyleClass().remove("warning");
			if(tag.getText() != oldTag) {
				for(CardDataStructure c : model.getMaster()) {
					for(int i=0; i<c.getTags().size(); i++) {
						if(c.getTags().get(i).equalsIgnoreCase(oldTag)) {
							c.changeTag(tag.getText(), oldTag);
						}
					}
				}
				model.changeTag(model.getObservableTags().indexOf(oldTag), tag.getText());
				controller.setTag(tag.getText());
			}
			stage.close();
		}
		tag.getStyleClass().add("warning");
		/* NEEDS TO BE ASYNC?
		 * would solve bug documented on main TODO
		if(tag.getText() != oldTag) {
			for(CardDataStructure c : model.getMaster()) {
				for(int i=0; i<c.getTags().size(); i++) {
					if(c.getTags().get(i).equalsIgnoreCase(oldTag)) {
						c.removeTag(oldTag);
						c.addTag(tag.getText());
					}
				}
			}
			model.changeTag(model.getObservableTags().indexOf(oldTag), tag.getText());
			controller.setTag(tag.getText());
		}*/
	}
	
	public void handleCancel() {
		stage.close();
	}
	
	public void init(TagItemController controller, CardsModel model, Stage stage) {
		this.stage = stage;
		this.model = model;
		this.controller = controller;
		this.tag.setText(controller.getTag().getText());
		oldTag = controller.getTag().getText();
		
		this.stage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("application/application.css").toString());
	}

}

package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TagItemController {
	
	@FXML
	Label tag;
	
	@FXML
	Label edit;
	
	@FXML
	Label delete;
	
	public void setTag(String tag) {
		this.tag.setText(tag);
	}
	
	public Label getTag() {
		return tag;
	}
	
	public Label getEdit() {
		return edit;
	}
	
	public Label getDelete() {
		return delete;
	}
}

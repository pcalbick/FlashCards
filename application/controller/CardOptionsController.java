package application.controller;

/*
 * 
 * Stage used for the creation of a new card
 * 
 */

import java.util.ArrayList;
import java.util.List;

import application.model.CardsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CardOptionsController {
	
	@FXML
	Button okButton;
	
	@FXML
	Button cancelButton;
	
	@FXML
	TextField question;
	
	@FXML
	TextField answer;
	
	@FXML
	TextField tags;
	
	private Stage optionsStage;
	private CardsModel model;
	
	public void handleOk() {
		if(!question.getText().equals("") && !answer.getText().equals("")) {
			List<String> tagList = new ArrayList<>();
			
			if(!tags.getText().isEmpty()) {
				String temp = tags.getText();
				while(!temp.isEmpty()) {
					String tag = "";
					if(temp.contains(","))
						tag = temp.substring(0, temp.indexOf(',')+1);
					else
						tag = temp;
					
					temp = temp.replace(tag, "");
					
					tag = tag.replace(",", "");
					tag = tag.trim();
					tagList.add(tag);
				}
			}
			
			for(String tag : tagList) {
				if(!model.getObservableTags().contains(tag))
					model.addTag(tag);
			}

			model.addCard(question.getText(), answer.getText(), tagList);
			optionsStage.close();
		} else {
			question.getStyleClass().remove("warning");
			answer.getStyleClass().remove("warning");
			if(question.getText().equals(""))
				question.getStyleClass().add("warning");
			if(answer.getText().equals(""))
				answer.getStyleClass().add("warning");
		}
	}
	
	public void handleCancel() {
		optionsStage.close();
	}
	
	public String getQuestion() {
		return question.getText();
	}
	
	public String getAnswer() {
		return answer.getText();
	}
	
	public List<String> getTags(){
		List<String> tagList = new ArrayList<>();
		
		if(!tags.getText().isEmpty()) {
			String temp = tags.getText();
			while(!temp.isEmpty()) {
				String tag = "";
				if(temp.contains(","))
					tag = temp.substring(0, temp.indexOf(',')+1);
				else
					tag = temp;
				
				temp = temp.replace(tag, "");
				
				tag = tag.replace(",", "");
				tag = tag.trim();
				tagList.add(tag);
			}
		}
		
		return tagList;
	}
	
	public void init(Stage optionsStage, CardsModel model) {
		optionsStage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("application/application.css").toString());
		this.optionsStage = optionsStage;
		this.model = model;
	}
}

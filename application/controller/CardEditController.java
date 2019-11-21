package application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.UnusedTags;
import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CardEditController {
	@FXML
	Button okButton;
	
	@FXML
	Button removeButton;
	
	@FXML
	Button cancelButton;
	
	@FXML
	TextField question;
	
	@FXML
	TextField answer;
	
	@FXML
	TextField tags;
	
	private Stage stage;
	private CardsModel model;
	private int index;
	private BorderPane pane;
	private List<String> oldTags;
	
	public void handleOk() {
		if(!question.getText().isEmpty() && !answer.getText().isEmpty()) {
			List<String> tagList = new ArrayList<>();
			
			if(!tags.getText().isEmpty()) {
				tagList = getTags();
				
				for(String tag : getTags()) {
					if(!model.getObservableTags().contains(tag))
						model.addTag(tag);
				}
			}
			
			model.changeCard(question.getText(), answer.getText(), tagList, index);
			
			Label q = (Label) pane.getChildren().get(0);
			Label a = (Label) pane.getChildren().get(1);
			q.setText(question.getText());
			a.setText(answer.getText());
			
			removeTags(tagList,false);
			stage.close();
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
		stage.close();
	}
	
	public void handleRemove() {
		//removeTags(getTags(),false);
		
		//model.removeCard(index);
		stage.close();
	}
	
	public void handleDelete() {
		model.deleteCard(index);
		
		removeTags(getTags(),true);
		stage.close();
	}
	
	public String getQuestion() {
		return question.getText();
	}
	
	public String getAnswer() {
		return answer.getText();
	}
	
	public void removeTags(List<String> tags, boolean delete) {
		UnusedTags unusedTags = new UnusedTags();
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<List<String>> future;
		if(delete)
			future = executor.submit(unusedTags.getDeleteTask(tags, model.getMaster()));
		else
			future = executor.submit(unusedTags.getTask(tags,oldTags,model.getMaster()));
		try {
			for(String s : future.get()) {
				model.deleteTag(model.indexOfTag(s));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
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
	
	public void init(Stage stage, CardsModel model, int index, BorderPane pane, CardDataStructure card) {
		stage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("application/application.css").toString());
		question.setText(model.getQuestion(index));
		answer.setText(model.getAnswer(index));
		List<String> tagList = model.getTags(index);
		
		String tagString = "";
		for(int i=0; i<tagList.size(); i++) {
			tagString += tagList.get(i) + ", ";
		}
		
		tags.setText(tagString);
		oldTags = card.getTags();
		
		this.stage = stage;
		this.model = model;
		this.index = index;
		this.pane = pane;
	}
}

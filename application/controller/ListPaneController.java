package application.controller;

import java.util.List;

import application.Main;
import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ListPaneController {
	@FXML
	Button newCard;
	
	@FXML
	VBox container;
	
	private Main main;
	private Stage primaryStage;
	
	private ScrollPane listPane;
	
	private CardsModel model;
	private int deleteIndex;
	
	public void handleNewCard() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(main.getClass().getResource("view/CardOptionView.fxml"));
			VBox cardOptions = (VBox) loader.load();
			CardOptionsController controller = loader.getController();
			
			Stage optionsStage = new Stage();
			optionsStage.setTitle("Create New Card");
			optionsStage.initModality(Modality.WINDOW_MODAL);
			optionsStage.initOwner(primaryStage);
			
			Scene cardScene = new Scene(cardOptions);
			cardScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					if(e.getCode().equals(KeyCode.ENTER)) {
						for(String tag : controller.getTags()) {
							if(!model.getObservableTags().contains(tag))
								model.addTag(tag);
						}
						
						model.addCard(controller.getQuestion(), controller.getAnswer(), controller.getTags());
						optionsStage.close();
					}
				}
			});
			
			optionsStage.setScene(cardScene);
			controller.init(optionsStage,model);
			
			optionsStage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadCards(List<CardDataStructure> cards) {
		model.clearCards();
		model.clearMaster();
		container.getChildren().clear();
		for(CardDataStructure c : cards) {
			List<String> tags = c.getTags();
			model.addCard(c.getQuestion(), c.getAnswer(), tags);
		}
	}
	
	public VBox getContainer() {
		return container;
	}
	
	public void init(Main main, Stage primaryStage, CardsModel model, ScrollPane listPane) {
		this.main = main;
		this.primaryStage = primaryStage;
		
		this.listPane = listPane;
		
		this.model = model;
		
		addListener();
	}
	
	private void addListener() {
		model.getObservableList().addListener((ListChangeListener.Change<? extends CardDataStructure> c) -> {
			while(c.next()) {
				if(c.wasAdded()) {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(main.getClass().getResource("view/MiniCardView.fxml"));
						BorderPane pane = (BorderPane) loader.load();
						
						pane.getStyleClass().add("card");
						
						MiniCardController controller = loader.getController();
						controller.init(model.getQuestion(model.getSize()-1), model.getAnswer(model.getSize()-1));

						pane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent e) {
								try {
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(main.getClass().getResource("view/CardEditView.fxml"));
									VBox view = (VBox) loader.load();
									CardEditController controller = loader.getController();
									
									Label question = (Label) pane.getChildren().get(0);
									Label answer = (Label) pane.getChildren().get(1);
									int index = model.getCardIndex(question.getText(), answer.getText());
									
									Stage stage = new Stage();
									
									Scene scene = new Scene(view);
									scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent e) {
											if(e.getCode().equals(KeyCode.ENTER)) {
												for(String tag : controller.getTags()) {
													if(!model.getObservableTags().contains(tag))
														model.addTag(tag);
												}
												model.changeCard(controller.getQuestion(), controller.getAnswer(), controller.getTags(), index);
												
												controller.removeTags(controller.getTags(),false);
												
												BorderPane card = (BorderPane) container.getChildren().get(index);
												Label question = (Label) card.getChildren().get(0);
												Label answer = (Label) card.getChildren().get(1);
												question.setText(model.getQuestion(index));
												answer.setText(model.getAnswer(index));
												stage.close();
											}
										}
									});
									
									stage.setScene(scene);
									
									controller.init(stage, model, index, pane, model.getCard(model.getCardIndex(question.getText(), answer.getText())));
									deleteIndex = index;
									
									stage.showAndWait();
								} catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						});
						container.getChildren().add(pane);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(c.wasRemoved()) {
					if(container.getChildren().get(deleteIndex) != null)
						container.getChildren().remove(deleteIndex);
				}
			}
		});
		
		container.heightProperty().addListener((ods,ov,nv) -> {
			if(nv.doubleValue() > ov.doubleValue())
				listPane.setVvalue(1.0);
		});
	}
}

package application.controller;

/*
 * 
 * Main screen. The screen is split into two sides. One side containing the master list of cards that is searchable.
 * the other container the chosen cards to be tested.
 * 
 */

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.Main;
import application.Search;
import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ListPaneController {
	//Create card button
	@FXML
	Button newCard;
	
	//Rendered master list container
	@FXML
	VBox container;
	
	//Scroll Pane for the master list container
	@FXML
	ScrollPane scroll;
	
	//Rendered test list container
	@FXML
	VBox testContainer;
	
	//Scroll Pane for test list container
	@FXML
	ScrollPane testScroll;
	
	//Search test input
	@FXML
	TextField search;
	
	//Clear search results button
	@FXML
	Button clear;
	
	//Container of the next two warnings
	@FXML
	VBox testWarning;
	
	//Tool tip that is displaced when no cards selected
	//Tells user to select a tag to populate list
	@FXML
	Label warning1;
	
	@FXML
	Label warning2;
	
	private Main main;
	private Stage primaryStage;
	
	private CardsModel model;
	
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
				//Allowing keyboard functionality on created window
				
				@Override
				public void handle(KeyEvent e) {
					if(e.getCode().equals(KeyCode.ENTER)) {
						if(!controller.getQuestion().isEmpty() && !controller.getAnswer().isEmpty()) {
							for(String tag : controller.getTags()) {
								if(!model.getObservableTags().contains(tag))
									model.addTag(tag);
							}
							
							model.addCard(controller.getQuestion(), controller.getAnswer(), controller.getTags());
							optionsStage.close();
						} else {
							controller.question.getStyleClass().remove("warning");
							controller.answer.getStyleClass().remove("warning");
							if(controller.question.getText().equals(""))
								controller.question.getStyleClass().add("warning");
							if(controller.answer.getText().equals(""))
								controller.answer.getStyleClass().add("warning");
						}
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
	
	//Clear the search and return the master list to its original state of displaying all cards
	public void handleClear() {
		if(newCard.isDisable())
			newCard.setDisable(false);
		
		search.clear();
		
		model.getObservableCards().clear();
		container.getChildren().clear();
		
		for(CardDataStructure c : model.getMaster()) {
			model.addToCards(c);
		}
	}
	
	//Called when load from the button pane is clicked and a json file selected
	public void loadCards(List<CardDataStructure> cards) {
		if(cards != null) {
			model.clearMaster();
			model.clearCards();
			model.clearTest();
			container.getChildren().clear();
			testContainer.getChildren().clear();
			for(CardDataStructure c : cards) {
				List<String> tags = c.getTags();
				model.addCard(c.getQuestion(), c.getAnswer(), tags);
			}
		}
	}
	
	//Returns the side of the screen containing the master list
	public VBox getContainer() {
		return container;
	}
	
	public void init(Main main, Stage primaryStage, CardsModel model) {
		this.main = main;
		this.primaryStage = primaryStage;
		
		this.model = model;
		
		addListener();
	}
	
	private void addListener() {
		//Searchable and rendered master list of cards
		model.getObservableCards().addListener((ListChangeListener.Change<? extends CardDataStructure> c) -> {
			while(c.next()) {
				if(c.wasAdded()) {
					try {
						//When a new card is created render the card
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(main.getClass().getResource("view/MiniCardView.fxml"));
						BorderPane pane = (BorderPane) loader.load();
						
						pane.getStyleClass().add("card");
						
						MiniCardController controller = loader.getController();
						controller.init(model.getQuestion(model.getSize()-1), model.getAnswer(model.getSize()-1));
						
						//Allow editing of the card by clicking on it
						pane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent e) {
								try {
									//Show the card editor window
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(main.getClass().getResource("view/CardEditView.fxml"));
									VBox view = (VBox) loader.load();
									CardEditController controller = loader.getController();
									
									Label question = (Label) pane.getChildren().get(0);
									Label answer = (Label) pane.getChildren().get(1);
									int index = model.getCardIndex(question.getText(), answer.getText());
									
									Stage stage = new Stage();
									
									Scene scene = new Scene(view);
									scene.getStylesheets().add(getClass().getClassLoader().getResource("application/application.css").toString());
									
									//Allow keyboard control of window created
									scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent e) {
											if(e.getCode().equals(KeyCode.ENTER)) {
												if(!controller.getQuestion().isEmpty() && !controller.getAnswer().isEmpty()) {
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
												} else {
													controller.question.getStyleClass().remove("warning");
													controller.answer.getStyleClass().remove("warning");
													if(controller.question.getText().equals(""))
														controller.question.getStyleClass().add("warning");
													if(controller.answer.getText().equals(""))
														controller.answer.getStyleClass().add("warning");
												}
											}
										}
									});
									
									stage.setScene(scene);
									
									controller.init(stage, model, index, pane, model.getCard(model.getCardIndex(question.getText(), answer.getText())));
									
									stage.showAndWait();
								} catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						});
						
						//Finally add new card to the master list side of the screen
						container.getChildren().add(pane);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				//Delete rendered card if removed from list
				if(c.wasRemoved()) {
					container.getChildren().remove(c.getFrom());
				}
			}
		});
		
		//Rendered list of cards chosen for to be tested
		model.getTestList().addListener((ListChangeListener.Change<? extends CardDataStructure> c) -> {
			while(c.next()) {
				if(c.wasAdded()) {
					try {
						//Render the card added to the test list
						testWarning.getChildren().clear();
						
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(main.getClass().getResource("view/MiniCardView.fxml"));
						BorderPane pane = (BorderPane) loader.load();
						
						pane.getStyleClass().add("card");
						
						MiniCardController controller = loader.getController();
						
						controller.init(c.getList().get(c.getFrom()).getQuestion(), c.getList().get(c.getFrom()).getAnswer());

						testContainer.getChildren().add(pane);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(c.wasRemoved()) {
					//Remove cards and if empty show tool tips
					if(model.getTestList().isEmpty()) {
						testWarning.getChildren().add(warning1);
						testWarning.getChildren().add(warning2);
					}
					testContainer.getChildren().remove(c.getFrom());
				}
			}
		});
		
		//Allow search functionality for the rendered master list
		search.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode().equals(KeyCode.ENTER)) {
					if(!search.getText().isEmpty()) {
						newCard.setDisable(true);
						model.getObservableCards().clear();
						container.getChildren().clear();
						
						Search searchTask = new Search();
						
						ExecutorService executor = Executors.newSingleThreadExecutor();
						Future<List<CardDataStructure>> future;
						
						future = executor.submit(searchTask.getSearch(model.getMaster(), search.getText()));
						try {
							for(CardDataStructure c : future.get()) {
								model.addToCards(c);
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						} finally {
							executor.shutdown();
						}
					}
				}
			}
		});
		
		//Always have the scroll at the bottom showing the last item added
		container.heightProperty().addListener((ods,ov,nv) -> {
			if(nv.doubleValue() > ov.doubleValue())
				scroll.setVvalue(1.0);
		});
		
		testContainer.heightProperty().addListener((ods,ov,nv) -> {
			if(nv.doubleValue() > ov.doubleValue())
				testScroll.setVvalue(1.0);
		});
	}
}

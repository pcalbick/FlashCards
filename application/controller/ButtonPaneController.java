package application.controller;

import java.io.File;
import java.util.List;

import application.JsonLoader;
import application.JsonWriter;
import application.Main;
import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ButtonPaneController {
	
	@FXML
	Button play;
	
	@FXML
	Button test;
	
	@FXML
	Button save;
	
	@FXML
	Button saveAs;
	
	@FXML
	Button load;
	
	private Main main;
	private CardsModel model;
	private Stage primaryStage;
	private ListPaneController listController;
	private TagsViewController tagController;
	
	public void handlePlay() {
		if(!model.isEmpty())
			makeTestWindow(false);
	}
	
	public void handleTest() {
		if(!model.isEmpty())
			makeTestWindow(true);
	}
	
	private void makeTestWindow(boolean test) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(main.getClass().getResource("view/PlayView.fxml"));
			GridPane play = (GridPane) loader.load();
			PlayController controller = loader.getController();
			
			Stage playStage = new Stage();
			playStage.setTitle("Play");
			playStage.initModality(Modality.WINDOW_MODAL);
			playStage.initOwner(primaryStage);
			
			Scene cardScene = new Scene(play);
			cardScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					if(e.getCode().equals(KeyCode.ESCAPE)) {
						playStage.close();
					}
				}
			});
			
			playStage.setScene(cardScene);
			controller.init(playStage, model, test);
			
			playStage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleSave() {
		if(model.getPath() != null && !model.getMaster().isEmpty()) {
			JsonWriter writer = new JsonWriter();
			writer.save(model.getPath(), model.getMaster().iterator(), model.getObservableTags().iterator());
		}		
	}
	
	public void handleSaveAs() {
		if(!model.getMaster().isEmpty()) {
			JsonWriter writer = new JsonWriter();
			writer.saveAs(primaryStage, model, model.getMaster().iterator(), model.getObservableTags().iterator());
		}
	}
	
	public void handleLoad() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open Card File");
		FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("JSON", "*.json");
		chooser.getExtensionFilters().add(extension);
		
		File file = chooser.showOpenDialog(primaryStage);
		
		JsonLoader load = new JsonLoader();
		List<CardDataStructure> list = load.load(file);
		if(list != null)
			listController.loadCards(list);
		
		List<String> tagList = load.loadTags(file);
		if(tagList != null)
			tagController.load(tagList);
		
		model.setPath(load.loadPath(file));
	}
	
	public void init(Main main, CardsModel model, Stage primaryStage, ListPaneController listController, TagsViewController tagController) {
		this.main = main;
		this.model = model;
		this.primaryStage = primaryStage;
		this.listController = listController;
		this.tagController = tagController;
	}
}

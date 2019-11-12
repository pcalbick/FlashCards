package application.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.Search;
import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SearchController {
	
	@FXML
	TextField search;
	
	private CardsModel model;
	private ListPaneController listController;
	
	private void handleSearch(){
		model.clearCards();
		listController.getContainer().getChildren().clear();
		
		Search search = new Search();
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<List<CardDataStructure>> future = executor.submit(search.getSearch(model.getMaster(), this.search.getText()));
		
		try {
			if(future.get() != null || !future.get().isEmpty()) {
				for(CardDataStructure c : future.get())
					model.addToTest(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		executor.shutdown();
	}
	
	public void init(CardsModel model, ListPaneController listController) {
		this.model = model;
		this.listController = listController;
		
		addListener();
	}
	
	private void addListener() {
		search.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode().equals(KeyCode.ENTER)) {
					handleSearch();
				}
			}
		});
	}

}

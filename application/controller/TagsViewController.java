package application.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.Comparators;
import application.InsertionSort;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TagsViewController {
	
	@FXML
	Label title;
	
	@FXML
	VBox container;
	
	private CardsModel model;
	
	public void load(List<String> tags) {
		if(tags != null) {
			model.clearTags();
			container.getChildren().clear();
			for(String s : tags) {
				model.addTag(s);
			}
		}
	}
	
	public void init(CardsModel model, Main main) {
		this.model = model;
		
		model.getObservableTags().addListener((ListChangeListener.Change<? extends String> c) -> {
			while(c.next()) {
				if(c.wasAdded() && !c.wasReplaced()) {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(main.getClass().getResource("view/TagViewItem.fxml"));
						GridPane tag = (GridPane) loader.load();
						
						TagItemController controller = loader.getController();
						controller.setTag(c.getList().get(c.getList().size()-1));
						
						addListeners(tag,controller,main);
						
						container.getChildren().add(tag);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(c.wasRemoved() && !c.wasReplaced()) {
					for(String s : c.getRemoved()) {
						for(int i=0; i<container.getChildren().size(); i++) {
							GridPane tag = (GridPane) container.getChildren().get(i);
							Label label = (Label) tag.getChildren().get(0);
							if(s.equalsIgnoreCase(label.getText())) {
								System.out.println(label.getText());
								container.getChildren().remove(tag);
							}
						}
					}
				}
			}
		});
	}
	
	public void addListeners(GridPane tag, TagItemController controller, Main main) {
		tag.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				controller.getEdit().setVisible(true);
				controller.getDelete().setVisible(true);
			}
		});
		
		tag.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				controller.getEdit().setVisible(false);
				controller.getDelete().setVisible(false);
			}
		});
		
		TagItemController itemController = controller;
		controller.getEdit().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(main.getClass().getResource("view/TagEditView.fxml"));
					VBox scene = (VBox) loader.load();
					
					TagEditController controller = loader.getController();
					
					Stage stage = new Stage();
					stage.setTitle("Edit Tag");
					stage.initModality(Modality.WINDOW_MODAL);
					stage.setScene(new Scene(scene));
					
					controller.init(itemController, model, stage);
					
					stage.showAndWait();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		controller.getDelete().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(main.getClass().getResource("view/TagDeleteView.fxml"));
					VBox scene = (VBox) loader.load();
					
					TagDeleteController controller = loader.getController();
					
					Stage stage = new Stage();
					stage.setTitle("Edit Tag");
					stage.initModality(Modality.WINDOW_MODAL);
					stage.setScene(new Scene(scene));
					
					controller.init(stage,itemController,model,container,tag);
					
					stage.showAndWait();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		controller.getTag().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				Search search = new Search();
				ExecutorService executor = Executors.newSingleThreadExecutor();
				
				if(!tag.getStyleClass().contains("highLight")) {
					tag.getStyleClass().add("highLight");

					Future<List<CardDataStructure>> future = executor.submit(search.getSearch(model.getMaster(), itemController.getTag().getText()));
					
					try {
						if(future.get() != null || !future.get().isEmpty()) {
							for(CardDataStructure c : future.get())
								if(!model.getTestList().contains(c))
									model.addToTest(c);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						executor.shutdown();
					}
					
				} else {
					tag.getStyleClass().remove("highLight");
					
					Future<List<CardDataStructure>> future = executor.submit(search.getSearch(model.getTestList(), itemController.getTag().getText()));
					
					try {
						if(future.get() != null || !future.get().isEmpty()) {
							for(CardDataStructure c : future.get())
								model.removeFromList(c);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						executor.shutdown();
					}
				}
			}
		});
	}
}

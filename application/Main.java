/* TODO
 * 
 * ADD CARD DOES NOT ACTUALLY ADD TO VISIBLE LIST UNLESS SHOW ALL SELECTED?
 * 
 * MOVE TAG PANE TO RIGHT OF BUTTON PANE AND CARD PANE TO RIGHT OF THAT (GRID PANE? ONLY CARD PANE SHOULD EXPAND ON RESIZE)
 * 
 * REVAMP BUTTON PANE (WHAT SHOULD BE THERE? MENU BAR INSTEAD?)
 * ADD SAVE AND SAVE AS
 * 
 * PERMANENT TAG OF SHOW ALL AT TOP SELECTED BY DEFAULT
 * CLICK TAG IS SELECT TAG NOT EDIT TAG (BUGGED)
 * 
 * PLAY BUTTON ASKS HOW MANY CARDS YOU WANT TO TEST (DEFAULT TO MAX SELECTED?)
 * REVAMP PLAY SCREEN?
 * 
 * REMOVE SEARCH BAR? KEEP FOR SINGLE CARD SEARCHES?
 * 
 * OBLIGITORY CSS BEAUTIFICATION
 * 
 * REMOVE CARDCONTAINER AND CARDVIEW (OBSOLETE) (CAREFUL DONT FUCK UP THE PROJECT)
 * 
 * ROOT PANE CONTROLLER MIGHT NOT BE NEEDED
 * 
 */

package application;
	
import application.controller.ButtonPaneController;
import application.controller.ListPaneController;
import application.controller.RootPaneController;
import application.controller.SearchController;
import application.controller.TagsViewController;
import application.model.CardsModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	//View containers
	//private BorderPane cardContainer;
	
	//Models
	private CardsModel cardsModel;
	
	//Controllers
	private RootPaneController rootPaneController;
	private ListPaneController listController;
	private TagsViewController tagController;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootPane.fxml"));
			BorderPane rootPane = (BorderPane) loader.load();
			
			cardsModel = new CardsModel();
			rootPaneController = new RootPaneController();
			rootPaneController.init(cardsModel);
			
			addListPane(rootPane, primaryStage);
			addTags(rootPane);
			addButtonPane(rootPane, primaryStage);
			addSearch(rootPane);
			//addCardContainer(rootPane);
			
			Scene scene = new Scene(rootPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			
			//primaryStage.setHeight(600);
			//primaryStage.setWidth(700);
			
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(700);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addListPane(BorderPane rootPane, Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ListPane.fxml"));
			ScrollPane listPane = (ScrollPane) loader.load();
			
			listController = loader.getController();
			listController.init(this, primaryStage, cardsModel, listPane);
			
			rootPane.setCenter(listPane);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addButtonPane(BorderPane rootPane, Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ButtonView.fxml"));
			VBox view = (VBox) loader.load();
			
			ButtonPaneController controller = loader.getController();
			controller.init(this, cardsModel, primaryStage, listController, tagController);
			
			rootPane.setLeft(view);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addTags(BorderPane rootPane) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/TagsView.fxml"));
			VBox tagContainer = (VBox) loader.load();
			
			tagController = loader.getController();
			tagController.init(cardsModel, this);
			
			rootPane.setRight(tagContainer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addSearch(BorderPane rootPane) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SearchView.fxml"));
			HBox searchContainer = (HBox) loader.load();
			
			SearchController controller = loader.getController();
			controller.init(cardsModel, listController);
			
			rootPane.setTop(searchContainer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private void addCardContainer(BorderPane rootPane) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CardContainer.fxml"));
			BorderPane cardContainer = (BorderPane) loader.load();
			this.cardContainer = cardContainer;
			rootPane.setCenter(cardContainer);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) {
		launch(args);
	}
}

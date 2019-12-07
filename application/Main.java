/* TODO
 * 
 * CHANGE SEARCH FROM SEARCH TAG TO SEARCH QUESTION OR ANSWER (THEN TAG?)
 * SEARCH FUNCTION FOR TAGS (LOTS OF TAGS)
 * SELECT ALL TAGS OPTION
 * HIDE MAIN SCREEN WHILE TESTING
 * PLAY BUTTON ASKS HOW MANY CARDS YOU WANT TO TEST (DEFAULT TO MAX SELECTED?)
 * CHANGING TAG TO ALREADY CREATED TAG DOES NOT REMOVE TAG AND REPLACE WITH CREATED TAG
 * WINDOW CLOSE EVENT ON MAIN PANE SYSTEM.EXIT(0);
 * 
 * ROOT PANE CONTROLLER MIGHT NOT BE NEEDED
 * 
 * CHANGING TAG WHILE IN TEST MESSES IT UP?
 * 
 * RETHINK UI!!!
 * ADD SORT?
 * 
 * REVAMP BUTTON PANE (WHAT SHOULD BE THERE? MENU BAR INSTEAD?)
 * 
 * OBLIGITORY CSS BEAUTIFICATION
 * 
 * SAVE ARRAY OF TESTS
 * 
 * CLOSE MAIN STAGE CLOSES ALL CHILDREN STAGES
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
import javafx.scene.control.SplitPane;
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
			//addSearch(rootPane);
			//addCardContainer(rootPane);
			
			Scene scene = new Scene(rootPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			
			primaryStage.setMinWidth(1150);
			primaryStage.setMinHeight(700);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addListPane(BorderPane rootPane, Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ListPane.fxml"));
			SplitPane listPane = (SplitPane) loader.load();
			
			listController = loader.getController();
			listController.init(this, primaryStage, cardsModel);
			
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
	
	/*private void addSearch(BorderPane rootPane) {
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
	}*/
	
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

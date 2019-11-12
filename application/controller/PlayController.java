package application.controller;

import java.util.Random;

import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlayController {
	
	@FXML
	TextField answer;
	
	@FXML
	Text question;
	
	@FXML
	Text correct;
	
	@FXML
	Button accept;
	
	@FXML
	Button tryAgain;
	
	@FXML
	Button next;
	
	@FXML
	VBox container;
	
	private Object[] cards;
	private int count = 0;
	private int numberCorrect = 0;
	
	public void handleAccept() {
		if(!answer.getText().equals("")) {
			CardDataStructure c = (CardDataStructure) cards[count];
			
			container.getStyleClass().remove("card");
			correct.setVisible(true);
			
			if(answer.getText().equalsIgnoreCase(c.getAnswer())) {
				container.getStyleClass().add("correct");
				tryAgain.setDisable(false);
				next.setDisable(false);
				numberCorrect++;
			} else {
				container.getStyleClass().add("incorrect");
				tryAgain.setDisable(false);
				next.setDisable(false);
			}
		}  else {
			answer.getStyleClass().add("warning");
		}
	}
	
	public void handleTryAgain() {
		if(count < numberCorrect)
			numberCorrect--;
		container.getStyleClass().clear();
		container.getStyleClass().add("card");
		
		correct.setVisible(false);
		tryAgain.setDisable(true);
		next.setDisable(true);
		
		answer.setText("");
	}
	
	public void handleNext() {
		count++;
		if(count < cards.length) {
			container.getStyleClass().clear();
			container.getStyleClass().add("card");
			
			CardDataStructure c = (CardDataStructure) cards[count];
			question.setText(c.getQuestion());
			correct.setText(c.getAnswer());
			correct.setVisible(false);
			
			tryAgain.setDisable(true);
			next.setDisable(true);
			
			answer.setText("");
		} else {
			container.getStyleClass().clear();
			container.getStyleClass().add("card");
			
			question.setText(Integer.toString(numberCorrect) + " out of " + Integer.toString(cards.length) + " correct!");
			
			correct.setVisible(false);
			
			tryAgain.setDisable(true);
			next.setDisable(true);
			accept.setDisable(true);
			
			answer.setText("");
			answer.setDisable(true);
		}
	}
	
	public void init(Stage stage, CardsModel model) {
		stage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("application/application.css").toString());
		container.getStyleClass().add("card");
		
		correct.setVisible(false);
		
		cards = (Object[]) model.getTestList().toArray();
		shuffle(cards);
		
		CardDataStructure c = (CardDataStructure) cards[count];
		question.setText(c.getQuestion());
		correct.setText(c.getAnswer());
		
		answer.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode().equals(KeyCode.ENTER)) {
					handleAccept();
				}
			}
		});
		
		tryAgain.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode().equals(KeyCode.ENTER)) {
					handleTryAgain();
				}
			}
		});
		
		next.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode().equals(KeyCode.ENTER)) {
					handleNext();
				}
			}
		});
	}
	
	public void shuffle(Object[] a) {
		Random random = new Random();
		for(int i=0; i<a.length; i++) {
			int rand = random.nextInt(i+1);
			Object swap = a[rand];
			a[rand] = a[i];
			a[i] = swap;
		}
	}
}

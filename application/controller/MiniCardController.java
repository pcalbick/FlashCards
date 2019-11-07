package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MiniCardController {
	@FXML
	Label question;
	
	@FXML
	Label answer;
	
	public void init(String question, String answer) {
		this.question.setText(question);
		this.answer.setText(answer);
	}
}

package application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardsModel {
	
	private List<CardDataStructure> master = new ArrayList<>();
	private ObservableList<CardDataStructure> cards = FXCollections.observableList(new ArrayList<CardDataStructure>());
	private ObservableList<String> tags = FXCollections.observableList(new ArrayList<String>());
	
	public void addCard(String first, String second, List<String> tags) {
		CardDataStructure card = new CardDataStructure(first,second,tags);
		cards.add(card);
		master.add(card);
	}
	
	public void addToList(CardDataStructure card) {
		cards.add(card);
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public ObservableList<CardDataStructure> getObservableList(){
		return cards;
	}
	
	public ObservableList<String> getObservableTags(){
		return tags;
	}
	
	public String getQuestion(int index) {
		return cards.get(index).getQuestion();
	}
	
	public String getAnswer(int index) {
		return cards.get(index).getAnswer();
	}
	
	public List<String> getTags(int index){
		return cards.get(index).getTags();
	}
	
	public String getTag(int index) {
		return tags.get(index);
	}
	
	public CardDataStructure getCard(int index) {
		return cards.get(index);
	}
	
	public List<CardDataStructure> getMaster() {
		return master;
	}
	
	public void changeCard(String question, String answer, List<String> tags, int index) {
		CardDataStructure card = cards.get(index);
		card.changeQuestion(question);
		card.changeAnswer(answer);
		card.changeTags(tags);
	}
	
	public void changeTag(int index, String tag) {
		tags.set(index, tag);
	}
	
	public void removeCard(int index) {
		cards.remove(index);
	}
	
	public void removeCard(CardDataStructure card) {
		cards.remove(card);
	}
	
	public void deleteCard(int index) {
		master.remove(cards.get(index));
		cards.remove(index);
	}
	
	public void deleteTag(int index) {
		tags.remove(index);
	}
	
	public int indexOfTag(String s) {
		return tags.indexOf(s);
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public boolean isEmpty() {
		return cards.isEmpty();
	}
	
	public void clearMaster() {
		master.clear();
	}
	
	public void clearCards() {
		cards.clear();
	}
	
	public void clearTags() {
		tags.clear();
	}
	
	//PROBABLY A NO NO (DONT LOOP IN MODEL?)
	public int getCardIndex(String question, String answer) {
		for(int i=0; i<cards.size(); i++) {
			if(cards.get(i).getAnswer().equals(answer) && cards.get(i).getQuestion().equals(question))
				return i;
		}
		return -1;
	}
	
	public int getTagIndex(String tag) {
		for(int i=0; i<tags.size(); i++) {
			if(tags.get(i).equals(tag))
				return i;
		}
		return -1;
	}
}

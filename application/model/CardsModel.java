package application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardsModel {
	
	private List<CardDataStructure> master = new ArrayList<>();
	private ObservableList<CardDataStructure> cards = FXCollections.observableList(new ArrayList<CardDataStructure>());
	private ObservableList<CardDataStructure> test = FXCollections.observableList(new ArrayList<CardDataStructure>());
	private ObservableList<String> tags = FXCollections.observableList(new ArrayList<String>());
	
	//Create Card
	public void addCard(String first, String second, List<String> tags) {
		CardDataStructure card = new CardDataStructure(first,second,tags);
		cards.add(card);
		master.add(card);
	}
	
	//Add To Cards
	public void addToCards(CardDataStructure card) {
		cards.add(card);
	}
	
	//Add To Test
	public void addToTest(CardDataStructure card) {
		test.add(card);
	}
	
	//Add Tag
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	//Get Master List
	public List<CardDataStructure> getMaster(){
		return master;
	}
	
	//Get Observable Lists
	public ObservableList<CardDataStructure> getObservableCards() {
		return cards;
	}
	
	public ObservableList<CardDataStructure> getTestList(){
		return test;
	}
	
	public ObservableList<String> getObservableTags(){
		return tags;
	}
	
	//Get From Specific Card
	public String getQuestion(int index) {
		return cards.get(index).getQuestion();
	}
	
	public String getAnswer(int index) {
		return cards.get(index).getAnswer();
	}
	
	public List<String> getTags(int index){
		return cards.get(index).getTags();
	}
	
	//Get Specific Tag
	public String getTag(int index) {
		return tags.get(index);
	}
	
	//Get Card From Specific List
	public CardDataStructure getCard(int index) {
		return cards.get(index);
	}
	
	public CardDataStructure getTestCard(int index) {
		return test.get(index);
	}
	
	//Change Components
	public void changeCard(String question, String answer, List<String> tags, int index) {
		CardDataStructure card = cards.get(index);
		card.changeQuestion(question);
		card.changeAnswer(answer);
		card.changeTags(tags);
	}
	
	public void changeTag(int index, String tag) {
		tags.set(index, tag);
	}
	
	//Remove From Cards
	public void removeFromCards(CardDataStructure card) {
		cards.remove(card);
	}
	
	//Remove From Test
	public void removeFromList(int index) {
		test.remove(index);
	}
	
	public void removeFromList(CardDataStructure card) {
		test.remove(card);
	}
	
	//Delete Cards or Tags
	public void deleteCard(int index) {
		if(test.size() > index && test.contains(test.get(index)))
			test.remove(index);
		master.remove(cards.get(index));
		cards.remove(index);
	}
	
	public void deleteTag(int index) {
		tags.remove(index);
	}
	
	//Get Indexes
	public int indexOfTag(String s) {
		return tags.indexOf(s);
	}
	
	public int indexOfTest(CardDataStructure card) {
		return test.indexOf(card);
	}
	
	//Get Size
	public int getSize() {
		return cards.size();
	}
	
	public int getTestSize() {
		return test.size();
	}
	
	public boolean isEmpty() {
		return test.isEmpty();
	}
	
	//Clear Lists
	public void clearCards() {
		cards.clear();
	}
	
	public void clearTest() {
		test.clear();
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

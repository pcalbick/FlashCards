package application.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//import java.util.function.UnaryOperator;

public class CardDataStructure {
	private String q;
	private String a;
	
	private List<String> tags = new ArrayList<>();
	
	public CardDataStructure(String q, String a, List<String> tags) {
		this.q = q;
		this.a = a;
		this.tags = tags;
	}
	
	public String getQuestion() {
		return q;
	}
	
	public String getAnswer() {
		return a;
	}
	
	public void changeQuestion(String q) {
		this.q = q;
	}
	
	public void changeAnswer(String a) {
		this.a = a;
	}
	
	public void changeTag(String tag, String oldTag) {
		int index = tags.indexOf(oldTag);
		tags.set(index, tag);
	}
	
	public void changeTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void removeTag(String tag) {
		tags.remove(tag);
	}
	
	public List<String> getTags(){
		return tags;
	}
}

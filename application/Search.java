package application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import application.model.CardDataStructure;

public class Search {
	
	public Callable<List<CardDataStructure>> getSearch(List<CardDataStructure> master, String search) {
		return new Callable<List<CardDataStructure>>() {
			public List<CardDataStructure> call() {
				List<CardDataStructure> list = new ArrayList<>();
				for(CardDataStructure c : master) {
					for(String s : c.getTags()) {
						if(s.equalsIgnoreCase(search))
							list.add(c);
					}
				}
				return list;
			}
		};
	}
}

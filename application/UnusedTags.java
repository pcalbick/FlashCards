package application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import application.model.CardDataStructure;

public class UnusedTags {
	public Callable<List<String>> getTask(List<String> list, List<String> compare, List<CardDataStructure> master) {
		return new Callable<List<String>>() {
			public List<String> call() {
				if(list != null) {
					for(String s : list) {
						if(!compare.contains(s))
							compare.remove(s);
					}
				}
				
				List<String> delete = new ArrayList<>();
				for(String s : compare) {
					boolean has = false;
					for(CardDataStructure c : master) {
						if(c.getTags().contains(s)) {
							has = true;
							break;
						}
					}
					if(!has)
						delete.add(s);
				}
				
				return delete;
			}
		};
	}
	
	public Callable<List<String>> getDeleteTask(List<String> tags, List<CardDataStructure> master){
		return new Callable<List<String>>() {
			public List<String> call(){
				List<String> delete = new ArrayList<>();
				for(String s : tags) {
					boolean has = false;
					for(CardDataStructure c : master) {
						if(c.getTags().contains(s)) {
							has = true;
							break;
						}
					}
					if(!has)
						delete.add(s);
				}
				
				return delete;
			}
		};
	}
}

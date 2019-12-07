package application;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import application.model.CardDataStructure;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JsonLoader {
	
	public Path loadPath(File file) {
		Path path = null;
		
		if(file != null) {
			JSONParser parser = new JSONParser();
			
			try(BufferedReader reader = new BufferedReader(new FileReader(file))){
				Object obj = parser.parse(reader);
				JSONArray items = (JSONArray) obj;
				JSONObject objPath = (JSONObject) items.get(items.size()-1);
				path = Paths.get((String)objPath.get("path"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return path;
	}
	
	public ArrayList<String> loadTags(File file) {
		ArrayList<String> load = new ArrayList<String>();
		
		if(file != null) {
			JSONParser parser = new JSONParser();
			
			try(BufferedReader reader = new BufferedReader(new FileReader(file))){
				Object obj = parser.parse(reader);
				JSONArray items = (JSONArray) obj;
				JSONArray tags = (JSONArray) items.get(items.size()-2);
				for(Object o : tags) {
					load.add((String)o);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(load.isEmpty())
			return null;
		return load;
	}
	
	public ArrayList<CardDataStructure> load(File file){
		ArrayList<CardDataStructure> load = new ArrayList<CardDataStructure>();
		
		if(file != null) {
			JSONParser parser = new JSONParser();
			
			try (BufferedReader reader = new BufferedReader(new FileReader(file))){
				Object obj = parser.parse(reader);
				JSONArray cards = (JSONArray) obj;
				for(int i=0; i<cards.size(); i++) {
					if(i < cards.size()-2) {
						JSONObject card = (JSONObject) cards.get(i);
						JSONArray tags = (JSONArray) card.get("tags");
						List<String> tagList = new ArrayList<>();
						for(Object o : tags) {
							String tag = (String) o;
							tagList.add(tag);
						}
						
						CardDataStructure newCard = new CardDataStructure(card.get("question").toString(),card.get("answer").toString(),tagList);
						load.add(newCard);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(load.isEmpty())
			return null;
		return load;
	}
}

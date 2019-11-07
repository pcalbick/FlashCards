package application;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import application.model.CardDataStructure;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JsonWriter {
	
	public void write(Stage stage, Iterator<CardDataStructure> list, Iterator<String> tagsList) {
		JSONArray cards = new JSONArray();
		while(list.hasNext()) {
			JSONObject card = new JSONObject();
			CardDataStructure c = (CardDataStructure) list.next();
			
			JSONArray tags = new JSONArray();
			for(String s : c.getTags())
				tags.add(s);
			
			card.put("tags", tags);
			card.put("question", c.getQuestion());
			card.put("answer", c.getAnswer());
			cards.add(card);
		}
		
		JSONArray tags = new JSONArray();
		while(tagsList.hasNext()) {
			tags.add((String)tagsList.next());
		}
		cards.add(tags);
	
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extensions = new FileChooser.ExtensionFilter("JSON", "*.json");
		chooser.getExtensionFilters().add(extensions);
		chooser.setTitle("Save Cards");
		
		File file = chooser.showSaveDialog(stage);
		if(file != null) {
			try	(BufferedWriter out = Files.newBufferedWriter(file.toPath(),StandardCharsets.UTF_8,StandardOpenOption.CREATE_NEW);){
				out.write(cards.toString());
			} catch(FileAlreadyExistsException e) {
				File f = new File(file.toString());
				file.delete();
				try(BufferedWriter out = Files.newBufferedWriter(f.toPath(),StandardCharsets.UTF_8,StandardOpenOption.CREATE_NEW);) {
					out.write(cards.toString());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}

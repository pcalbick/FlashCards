package application;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import application.model.CardDataStructure;
import application.model.CardsModel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JsonWriter {
	public void save(Path path, Iterator<CardDataStructure> list, Iterator<String> tagsList) {
		JSONArray cards = createJSON(path,list,tagsList);
		File file = new File(path.toString());
		file.delete();
		
		File f = new File(path.toString());
		try(BufferedWriter out = Files.newBufferedWriter(f.toPath(),StandardCharsets.UTF_8,StandardOpenOption.CREATE_NEW);) {
			out.write(cards.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void saveAs(Stage stage, CardsModel model, Iterator<CardDataStructure> list, Iterator<String> tagsList) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extensions = new FileChooser.ExtensionFilter("JSON", "*.json");
		chooser.getExtensionFilters().add(extensions);
		chooser.setTitle("Save Cards");
		
		File file = chooser.showSaveDialog(stage);
		if(file != null) {
			JSONArray cards = createJSON(file.toPath(),list,tagsList);
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
			} finally {
				model.setPath(file.toPath());
			}
		}
	}
	
	private JSONArray createJSON(Path path, Iterator<CardDataStructure> list, Iterator<String> tagsList) {
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
		
		JSONObject pathObject = new JSONObject();
		pathObject.put("path", path.toString());
		cards.add(pathObject);
		
		return cards;
	}
}

package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;
import org.json.*;
import model.*;

/*
 * Represents a reader that reads farm from JSON data stored in file
 * Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */
public class JsonReader {
    private String source;

    /*
     * EFFECTS: constructs reader to read from source file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /*
     * EFFECTS: reads farm from file and returns it, throws IOException if an error
     * occurs reading data from file
     * 
     */
    public Farm read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFarm(jsonObject);
    }

    /*
     * EFFECTS: reads source file as string and returns it
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    /*
     * EFFECTS: parses farm from JSON object and returns it
     */
    private Farm parseFarm(JSONObject jsonObject) {
        int area = jsonObject.getInt("area");
        int time = jsonObject.getInt("time");
        int revenue = jsonObject.getInt("revenue");
        Farm farm = new Farm(area);
        addPlantedCrops(farm, jsonObject);
        addPlantableCrops(farm, jsonObject);
        farm.setTime(time);
        farm.setRevenue(revenue);
        return farm;
    }

    /*
     * MODIFIES: farm
     * EFFECTS: parses plantedCrops from JSON object and adds them to farm
     */
    private void addPlantedCrops(Farm farm, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("crops");
        for (Object json : jsonArray) {
            JSONObject nextPlantedCrop = (JSONObject) json;
            addPlantedCrop(farm, nextPlantedCrop);
        }
    }

    /*
     * MODIFIES: farm
     * EFFECTS: parses crop from JSON object and adds it to farm
     */
    private void addPlantedCrop(Farm farm, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int revenue = jsonObject.getInt("revenue");
        int seeds = jsonObject.getInt("seeds");
        int timeRequired = jsonObject.getInt("timeRequired");
        int plantArea = jsonObject.getInt("plantedArea");
        int timeGrown = jsonObject.getInt("timeGrown");
        PlantedCrop plantedCrop = new PlantedCrop(new Crop(name, revenue, seeds, timeRequired), plantArea);
        plantedCrop.setTimeGrown(timeGrown);
        farm.addPlantedCrop(plantedCrop);
    }

    /*
     * MODIFIES: farm
     * EFFECTS: parses plantableCrops from JSON object and adds them to farm
     */
    private void addPlantableCrops(Farm farm, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("plantableCrops");
        for (Object json : jsonArray) {
            JSONObject nextPlantableCrop = (JSONObject) json;
            addPlantableCrop(farm, nextPlantableCrop);
        }
    }

    /*
     * MODIFIES: farm
     * EFFECTS: parses plantableCrop from JSON object and adds it to farm
     */
    private void addPlantableCrop(Farm farm, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int revenue = jsonObject.getInt("revenue");
        int seeds = jsonObject.getInt("seeds");
        int timeRequired = jsonObject.getInt("timeRequired");
        Crop crop = new Crop(name, revenue, seeds, timeRequired);
        farm.addPlantableCrop(crop);
    }
}
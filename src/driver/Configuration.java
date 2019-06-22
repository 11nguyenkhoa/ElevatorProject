package driver;

import exceptions.ElevatorInvalidFileException;
import exceptions.ElevatorParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Configuration {
    public static final int NUMBER_FLOORS;
    public static final int NUM_ELEV;
    public static final int MAX_PERSONS;
    public static final int FLOOR_TIME;
    public static final int DOOR_TIME;
    public static final int IDLE_TIME;
    public static final int DURATION_TIME;
    public static final int CREATION_RATE;


    static { //static Configuration class only runs the very first time
        FileReader reader;
        try {
            // Create a FileReader object using your filename
            reader = new FileReader("/Users/ERplus/OneDrive/Masters Data Science/SE 450 Objected Oriented Software Development/Project/ElevatorBase/input.json");
        } catch (FileNotFoundException e) {
            throw new ElevatorInvalidFileException("Invalid file", e);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jObj;

        try {
            // Create a JSONParser using the FileReader
            jObj = (JSONObject) jsonParser.parse(reader);

            NUMBER_FLOORS = ((Number) jObj.get("numFloors")).intValue();
            NUM_ELEV = ((Number) jObj.get("numElev")).intValue();
            MAX_PERSONS = ((Number) jObj.get("maxPersons")).intValue();
            FLOOR_TIME = ((Number) jObj.get("floorTime")).intValue();
            DOOR_TIME = ((Number) jObj.get("doorTime")).intValue();
            IDLE_TIME = ((Number) jObj.get("idleTime")).intValue();
            DURATION_TIME = ((Number) jObj.get("durationTime")).intValue();
            CREATION_RATE = ((Number) jObj.get("creationRate")).intValue();
        } catch (IOException | ParseException e) {
            throw new ElevatorParseException("Parsing Error", e);
        }
    }
}

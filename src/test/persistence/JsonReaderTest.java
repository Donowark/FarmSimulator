package persistence;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import model.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    void testReaderEmptyFarm() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFarm.json");
        try {
            Farm farm = reader.read();
            assertEquals(500, farm.getArea());
            assertEquals(0, farm.getPlantedCrops().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralFarm() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFarm.json");
        try {
            Farm farm = reader.read();
            assertEquals(500, farm.getArea());
            List<PlantedCrop> crops = farm.getPlantedCrops();
            assertEquals(2, crops.size());
            checkPlantedCrop(new Crop("apple", 100, 300, 2), 100, crops.get(0));
            checkPlantedCrop(new Crop("banana", 50, 500, 1), 300, crops.get(1));
            checkPlantableCrop("apple", 100, 300, 2, new Crop("apple", 100, 300, 2));
            checkPlantableCrop("banana", 50, 500, 1, new Crop("banana", 50, 500, 1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
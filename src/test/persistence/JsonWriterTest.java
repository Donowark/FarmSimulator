package persistence;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import exception.*;
import model.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFarm() {
        try {
            Farm farm = new Farm(500);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFarm.json");
            writer.open();
            writer.write(farm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFarm.json");
            farm = reader.read();
            assertEquals(500, farm.getArea());
            assertEquals(0, farm.getPlantedCrops().size());
            assertEquals(0, farm.getRevenue());
            assertEquals(0, farm.getTime());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralFarm() {
        try {
            Farm farm = createAndPlantTestFarm();
            writeAndReadFarmFromJson(farm);
            verifyFarmData(farm);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    /*
     * EFFECTS: creates a farm and plants test crops, returns the farm
     */
    private Farm createAndPlantTestFarm() {
        Farm farm = new Farm(500);
        plantCropSafely(farm, "apple", 100, 300, 2, 100);
        plantCropSafely(farm, "banana", 50, 500, 1, 300);
        return farm;
    }

    /*
     * MODIFIES: farm
     * EFFECTS: plants a crop on the farm, fails test if exception occurs
     */
    private void plantCropSafely(Farm farm, String name, int revenue, int seeds,
            int timeRequired, int plantArea) {
        try {
            farm.plant(new Crop(name, revenue, seeds, timeRequired), plantArea);
        } catch (NonPositiveAreaException | FilledFarmException e) {
            fail("Planting should not throw exception: " + e.getClass().getSimpleName());
        }
    }

    /*
     * EFFECTS: writes farm to JSON file and reads it back, returns the read farm
     */
    private Farm writeAndReadFarmFromJson(Farm farm) throws IOException {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralFarm.json");
        writer.open();
        writer.write(farm);
        writer.close();

        JsonReader reader = new JsonReader("./data/testWriterGeneralFarm.json");
        return reader.read();
    }

    /*
     * EFFECTS: verifies the farm data after JSON serialization/deserialization
     */
    private void verifyFarmData(Farm farm) {
        assertEquals(500, farm.getArea());
        List<PlantedCrop> crops = farm.getPlantedCrops();
        assertEquals(2, crops.size());
        checkPlantedCrop(new Crop("apple", 100, 300, 2), 100, crops.get(0));
        checkPlantedCrop(new Crop("banana", 50, 500, 1), 300, crops.get(1));
    }
}
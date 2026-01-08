package model;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.json.*;
import org.junit.jupiter.api.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import exception.*;

@ExcludeFromJacocoGeneratedReport
public class FarmTest {
    private Farm testFarm;
    private Crop testCrop1;
    private Crop testCrop2;
    private PlantedCrop testPlantedCrop1;
    private PlantedCrop testPlantedCrop2;
    private ArrayList<PlantedCrop> testCrops;

    @BeforeEach
    void runBefore() {
        testFarm = new Farm(500);
        testCrop1 = new Crop("testName1", 10, 100, 2);
        testCrop2 = new Crop("testName2", 20, 300, 3);
        testPlantedCrop1 = new PlantedCrop(new Crop("testName1", 10, 100, 2), 100);
        testPlantedCrop2 = new PlantedCrop(new Crop("testName2", 20, 300, 3), 200);
        testCrops = new ArrayList<>();
    }

    @Test
    void testConstructor() {
        assertEquals(500, testFarm.getArea());
        assertEquals(0, testFarm.getPlantedCrops().size());
        assertEquals(0, testFarm.getTime());
        assertEquals(0, testFarm.getRevenue());
    }

    @Test
    void testIsFilled() {
        assertFalse(testFarm.isFilled());
    }

    @Test
    void testIsFilledFalse() {
        try {
            testFarm.plant(testCrop1, 200);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        assertFalse(testFarm.isFilled());
    }

    @Test
    void testIsFilledTrue() {
        try {
            testFarm.plant(testCrop1, 500);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        assertTrue(testFarm.isFilled());
    }

    @Test
    void testPlant() {
        testCrops.add(testPlantedCrop1);
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        assertEquals("testName1", testFarm.getPlantedCrops().get(0).getName());
        assertEquals(400, testFarm.remainingArea());
        assertEquals(0, testCrop1.getSeeds());
    }

    @Test
    void testPlantMultiple() {
        testCrops.add(testPlantedCrop1);
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        testCrops.add(testPlantedCrop2);
        try {
            testFarm.plant(testCrop2, 200);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        assertEquals("testName1", testFarm.getPlantedCrops().get(0).getName());
        assertEquals("testName2", testFarm.getPlantedCrops().get(1).getName());
        assertEquals(200, testFarm.remainingArea());
        assertEquals(0, testCrop1.getSeeds());
        assertEquals(100, testCrop2.getSeeds());
    }

    @Test
    void testTimePassNoPlantedCropIsRipe() {
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        testFarm.timeLapses();
        assertEquals(1, testFarm.getPlantedCrops().get(0).getTimeGrown());
        assertEquals(1, testFarm.getTime());
    }

    @Test
    void testTimePassMultiple() {
        try {
            testFarm.plant(testCrop2, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        testFarm.timeLapses();
        testFarm.timeLapses();
        assertEquals(2, testFarm.getPlantedCrops().get(0).getTimeGrown());
        assertEquals(2, testFarm.getTime());
    }

    @Test
    void testTimePassSomePlantedCropIsRipe() {
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        try {
            testFarm.plant(testCrop2, 200);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        testFarm.timeLapses();
        testFarm.timeLapses();
        assertEquals(1, testFarm.getPlantedCrops().size());
        assertEquals("testName2", testFarm.getPlantedCrops().get(0).getName());
        assertEquals(2, testFarm.getPlantedCrops().get(0).getTimeGrown());
    }

    @Test
    void testTimePassAllPlantedCropIsRipe() {
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        try {
            testFarm.plant(testCrop2, 200);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        testFarm.timeLapses();
        testFarm.timeLapses();
        testFarm.timeLapses();
        assertEquals(0, testFarm.getPlantedCrops().size());
    }

    @Test
    void testHarvestOnePlantedCropIsRipe() {
        testPlantedCrop1.grow();
        testPlantedCrop1.grow();
        testFarm.getPlantedCrops().add(testPlantedCrop1);
        testFarm.harvest();
        assertEquals(0, testFarm.getPlantedCrops().size());
        assertEquals(1000, testFarm.getRevenue());
    }

    @Test
    void testHarvestNoPlantedCropIsRipe() {
        testPlantedCrop1.grow();
        testPlantedCrop2.grow();
        testFarm.getPlantedCrops().add(testPlantedCrop1);
        testFarm.getPlantedCrops().add(testPlantedCrop2);
        testFarm.harvest();
        assertEquals(2, testFarm.getPlantedCrops().size());
        assertEquals("testName1", testFarm.getPlantedCrops().get(0).getName());
        assertEquals("testName2", testFarm.getPlantedCrops().get(1).getName());
        assertEquals(0, testFarm.getRevenue());
    }

    @Test
    void testHarvestSomePlantedCropIsRipe() {
        testPlantedCrop1.grow();
        testPlantedCrop1.grow();
        testPlantedCrop2.grow();
        testPlantedCrop2.grow();
        testFarm.getPlantedCrops().add(testPlantedCrop1);
        testFarm.getPlantedCrops().add(testPlantedCrop2);
        testFarm.harvest();
        assertEquals(1, testFarm.getPlantedCrops().size());
        assertEquals("testName2", testFarm.getPlantedCrops().get(0).getName());
        assertEquals(1000, testFarm.getRevenue());
    }

    @Test
    void testHarvestMulitiplePlantedCropIsRipe() {
        testPlantedCrop1.grow();
        testPlantedCrop1.grow();
        testPlantedCrop2.grow();
        testPlantedCrop2.grow();
        testPlantedCrop2.grow();
        testFarm.getPlantedCrops().add(testPlantedCrop1);
        testFarm.getPlantedCrops().add(testPlantedCrop2);
        testFarm.harvest();
        assertEquals(0, testFarm.getPlantedCrops().size());
        assertEquals(5000, testFarm.getRevenue());
    }

    @Test
    void testReset() {
        testFarm.timeLapses();
        testFarm.timeLapses();
        testFarm.timeLapses();
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        testFarm.timeLapses();
        testFarm.reset();
        assertEquals(0, testFarm.getPlantedCrops().size());
        assertEquals(0, testFarm.getTime());
    }

    @Test
    void testRemainingArea() {
        assertEquals(500, testFarm.remainingArea());
    }

    @Test
    void testRemainingAreaWithOnePlantedCrop() {
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        assertEquals(400, testFarm.remainingArea());
    }

    @Test
    void testRemainingAreaWithMultiplePlantedCrop() {
        try {
            testFarm.plant(testCrop1, 100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        try {
            testFarm.plant(testCrop2, 200);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (FilledFarmException e) {
            fail("Unexpected FilledFarmException thrown");
        }
        assertEquals(200, testFarm.remainingArea());
    }

    @Test
    void testAddPlantedCrop() {
        testFarm.addPlantedCrop(testPlantedCrop1);
        assertEquals(1, testFarm.getPlantedCrops().size());
        assertEquals(testPlantedCrop1, testFarm.getPlantedCrops().get(0));
    }

    @Test
    void testAddPlantedCropMultiple() {
        testFarm.addPlantedCrop(testPlantedCrop1);
        testFarm.addPlantedCrop(testPlantedCrop2);
        assertEquals(2, testFarm.getPlantedCrops().size());
        assertEquals(testPlantedCrop1, testFarm.getPlantedCrops().get(0));
        assertEquals(testPlantedCrop2, testFarm.getPlantedCrops().get(1));
    }

    @Test
    void testAddPlantableCrop() {
        testFarm.addPlantableCrop(testCrop1);
        assertEquals(1, testFarm.getPlantableCrops().size());
        assertEquals(testCrop1, testFarm.getPlantableCrops().get(0));
    }

    @Test
    void testAddPlantableCropMultiple() {
        testFarm.addPlantableCrop(testCrop1);
        testFarm.addPlantableCrop(testCrop2);
        assertEquals(2, testFarm.getPlantableCrops().size());
        assertEquals(testCrop1, testFarm.getPlantableCrops().get(0));
        assertEquals(testCrop2, testFarm.getPlantableCrops().get(1));
    }

    @Test
    void testPlantWithNegativeArea() {
        try {
            testFarm.plant(testCrop1, -100);
            fail("Expected NonPositiveAreaException");
        } catch (NonPositiveAreaException e) {
            // Expected
        } catch (FilledFarmException e) {
            fail("Wrong exception type");
        }
    }

    @Test
    void testPlantWhenFarmFilled() {
        try {
            testFarm.plant(testCrop1, 500);
            testFarm.plant(testCrop2, 1);
            fail("Expected FilledFarmException");
        } catch (NonPositiveAreaException e) {
            fail("Wrong exception type");
        } catch (FilledFarmException e) {
            // Expected
        }
    }

    @Test
    void testSetArea() {
        testFarm.setArea(600);
        assertEquals(600, testFarm.getArea());
    }

    @Test
    void testSetTime() {
        testFarm.setTime(3);
        assertEquals(3, testFarm.getTime());
    }

    @Test
    void testSetRevenue() {
        testFarm.setRevenue(5000);
        assertEquals(5000, testFarm.getRevenue());
    }

    @Test
    void testToJsonEmptyFarm() {
        JSONObject json = testFarm.toJson();
        assertEquals(500, json.getInt("area"));
        assertEquals(0, json.getInt("time"));
        assertEquals(0, json.getInt("revenue"));
        JSONArray cropsArray = json.getJSONArray("crops");
        assertTrue(cropsArray.isEmpty());
        JSONArray plantableCropsArray = json.getJSONArray("plantableCrops");
        assertTrue(plantableCropsArray.isEmpty());
    }

    @Test
    void testToJsonFarmWithAddedCrops() {
        testFarm.addPlantableCrop(testCrop1);
        testFarm.addPlantableCrop(testCrop2);
        testFarm.addPlantedCrop(testPlantedCrop1);
        testFarm.addPlantedCrop(testPlantedCrop2);
        JSONObject json = testFarm.toJson();
        assertEquals(500, json.getInt("area"));
        assertEquals(0, json.getInt("time"));
        assertEquals(0, json.getInt("revenue"));
        JSONArray cropsArray = json.getJSONArray("crops");
        assertEquals(2, cropsArray.length());
        JSONArray plantableCropsArray = json.getJSONArray("plantableCrops");
        assertEquals(2, plantableCropsArray.length());
    }

    @Test
    void testToJsonFarmAfterTimeLapse() throws NonPositiveAreaException, FilledFarmException {
        testFarm.plant(testCrop1, 50);
        testFarm.timeLapses();
        JSONObject json = testFarm.toJson();
        assertEquals(500, json.getInt("area"));
        assertEquals(1, json.getInt("time"));
        assertEquals(0, json.getInt("revenue"));
        JSONArray cropsArray = json.getJSONArray("crops");
        assertEquals(1, cropsArray.length());
        JSONObject plantedCropJson = cropsArray.getJSONObject(0);
        assertEquals(1, plantedCropJson.getInt("timeGrown"));
    }

    @Test
    void testToJsonFarmWithRevenue() throws NonPositiveAreaException, FilledFarmException {
        testFarm.plant(testCrop1, 100);
        testFarm.timeLapses();
        testFarm.timeLapses();
        JSONObject json = testFarm.toJson();
        assertEquals(1000, json.getInt("revenue"));
        assertEquals(2, json.getInt("time"));
        JSONArray cropsArray = json.getJSONArray("crops");
        assertTrue(cropsArray.isEmpty());
    }
}
package model;

import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class PlantedCropTest {
    private PlantedCrop testPlantedCrop;

    @BeforeEach
    void runBefore() {
        testPlantedCrop = new PlantedCrop(new Crop("testName", 10, 100, 2), 100);
    }

    @Test
    void testGrow() {
        testPlantedCrop.grow();
        assertEquals(1, testPlantedCrop.getTimeGrown());
    }

    @Test
    void testGrowMultiple() {
        testPlantedCrop.grow();
        testPlantedCrop.grow();
        assertEquals(2, testPlantedCrop.getTimeGrown());
    }

    @Test
    void testIsRipe() {
        assertFalse(testPlantedCrop.isRipe());
    }

    @Test
    void testIsRipeFalse() {
        testPlantedCrop.grow();
        assertFalse(testPlantedCrop.isRipe());
    }

    @Test
    void testIsRipeTrue() {
        testPlantedCrop.grow();
        testPlantedCrop.grow();
        assertTrue(testPlantedCrop.isRipe());
    }

    @Test
    void testPlantedRevenue() {
        assertEquals(1000, testPlantedCrop.plantedRevenue());
    }

    @Test
    void testSetTimeGrown() {
        testPlantedCrop.setTimeGrown(1);
        assertEquals(1, testPlantedCrop.getTimeGrown());
    }

    @Test
    void testToString() {
        String expected = "testName: 10: 100: 2: 100: 0";
        assertEquals(expected, testPlantedCrop.toString());
    }

    @Test
    void testToJsonNewPlantedCrop() {
        JSONObject json = testPlantedCrop.toJson();
        assertEquals("testName", json.getString("name"));
        assertEquals(10, json.getInt("revenue"));
        assertEquals(100, json.getInt("seeds"));
        assertEquals(2, json.getInt("timeRequired"));
        assertEquals(0, json.getInt("timeGrown"));
        assertEquals(100, json.getInt("plantedArea"));
    }

    @Test
    void testToJsonAfterGrowth() {
        testPlantedCrop.grow();
        JSONObject json = testPlantedCrop.toJson();
        assertEquals(1, json.getInt("timeGrown"));
        assertFalse(testPlantedCrop.isRipe());
    }

    @Test
    void testToJsonRipeCrop() {
        testPlantedCrop.grow();
        testPlantedCrop.grow();
        JSONObject json = testPlantedCrop.toJson();
        assertEquals(2, json.getInt("timeGrown"));
        assertTrue(testPlantedCrop.isRipe());
    }

    @Test
    void testToJsonWithSetTimeGrown() {
        PlantedCrop newPlantedCrop = new PlantedCrop(
                new Crop("testName3", 15, 150, 3), 75);
        newPlantedCrop.setTimeGrown(2);
        JSONObject json = newPlantedCrop.toJson();
        assertEquals(2, json.getInt("timeGrown"));
        assertFalse(newPlantedCrop.isRipe());
    }

    @Test
    void testToJsonRevenueCalculation() {
        int expectedRevenue = testPlantedCrop.getPlantedArea() * testPlantedCrop.getRevenue();
        assertEquals(1000, expectedRevenue);
        assertEquals(expectedRevenue, testPlantedCrop.plantedRevenue());
        JSONObject json = testPlantedCrop.toJson();
        assertEquals(testPlantedCrop.getPlantedArea(), json.getInt("plantedArea"));
        assertEquals(testPlantedCrop.getRevenue(), json.getInt("revenue"));
    }
}
package model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import exception.*;

@ExcludeFromJacocoGeneratedReport
public class CropTest {
    private Crop testCrop1;
    private Crop testCrop2;
    private ArrayList<Crop> testCrops;

    @BeforeEach
    void runBefore() {
        testCrop1 = new Crop("testName1", 10, 100, 2);
        testCrop2 = new Crop("testName2", 20, 300, 3);
        testCrops = new ArrayList<Crop>();
    }

    @Test
    void testConstructor() {
        assertEquals("testName1", testCrop1.getName());
        assertEquals(10, testCrop1.getRevenue());
        assertEquals(100, testCrop1.getSeeds());
        assertEquals(2, testCrop1.getTimeRequired());
    }

    @Test
    void testAddSeeds() {
        try {
            testCrop1.addSeeds(50);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        }
        assertEquals(150, testCrop1.getSeeds());
    }

    @Test
    void testAddSeedsMultiple() {
        try {
            testCrop1.addSeeds(50);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        }
        try {
            testCrop1.addSeeds(100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        }
        assertEquals(250, testCrop1.getSeeds());
    }

    @Test
    void testAddSeedsBoundary() {
        try {
            testCrop1.addSeeds(1);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        }
        assertEquals(101, testCrop1.getSeeds());
    }

    @Test
    void testUseSeeds() {
        try {
            testCrop1.useSeeds(50);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (InsufficientSeedsException e) {
            fail("Unexpected InsufficientSeedsException thrown");
        }
        assertEquals(50, testCrop1.getSeeds());
    }

    @Test
    void testUseSeedsMultiple() {
        try {
            testCrop1.useSeeds(50);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (InsufficientSeedsException e) {
            fail("Unexpected InsufficientSeedsException thrown");
        }
        try {
            testCrop1.useSeeds(20);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (InsufficientSeedsException e) {
            fail("Unexpected InsufficientSeedsException thrown");
        }
        assertEquals(30, testCrop1.getSeeds());
    }

    @Test
    void testUseSeedsLowerBoundary() {
        try {
            testCrop1.useSeeds(1);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (InsufficientSeedsException e) {
            fail("Unexpected InsufficientSeedsException thrown");
        }
        assertEquals(99, testCrop1.getSeeds());
    }

    @Test
    void testUseSeedsUpperBoundary() {
        try {
            testCrop1.useSeeds(100);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (InsufficientSeedsException e) {
            fail("Unexpected InsufficientSeedsException thrown");
        }
        assertEquals(0, testCrop1.getSeeds());
    }

    @Test
    void testFindCropByNameNotFound() {
        testCrops.add(testCrop1);
        assertEquals(null, Crop.findCropByName(testCrops, "testName2"));
    }

    @Test
    void testFindCropByNameFirstElement() {
        testCrops.add(testCrop1);
        testCrops.add(testCrop2);
        assertEquals("testName1", Crop.findCropByName(testCrops, "testName1").getName());
    }

    @Test
    void testFindCropByNameNotFirstElement() {
        testCrops.add(testCrop1);
        testCrops.add(testCrop2);
        assertEquals("testName2", Crop.findCropByName(testCrops, "testName2").getName());
    }

    @Test
    void testToString() {
        String expected = "testName1 - $10 - 100 seeds - 2q";
        assertEquals(expected, testCrop1.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Crop sameCrop = new Crop("testName1", 10, 100, 2);
        Crop differentCrop = new Crop("testName1", 15, 100, 2);
        assertTrue(testCrop1.equals(sameCrop));
        assertFalse(testCrop1.equals(differentCrop));
        assertEquals(testCrop1.hashCode(), sameCrop.hashCode());
    }

    @Test
    void testToJson() {
        JSONObject json = testCrop1.toJson();
        assertEquals("testName1", json.getString("name"));
        assertEquals(10, json.getInt("revenue"));
        assertEquals(100, json.getInt("seeds"));
        assertEquals(2, json.getInt("timeRequired"));
    }

    @Test
    void testToJsonDifferentCrop() {
        JSONObject json = testCrop2.toJson();
        assertEquals("testName2", json.getString("name"));
        assertEquals(20, json.getInt("revenue"));
        assertEquals(300, json.getInt("seeds"));
        assertEquals(3, json.getInt("timeRequired"));
    }

    @Test
    void testToJsonAfterSeedOperations() {
        try {
            testCrop1.addSeeds(50);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        }
        try {
            testCrop1.useSeeds(75);
        } catch (NonPositiveAreaException e) {
            fail("Unexpected NonPositiveAreaException thrown");
        } catch (InsufficientSeedsException e) {
            fail("Unexpected InsufficientSeedsException thrown");
        }
        JSONObject json = testCrop1.toJson();
        assertEquals(75, json.getInt("seeds"));
    }

    @Test
    void testToJsonMultipleCropsConsistency() {
        JSONObject json1 = testCrop1.toJson();
        JSONObject json2 = testCrop2.toJson();
        assertNotEquals(json1.getString("name"), json2.getString("name"));
        assertNotEquals(json1.getInt("revenue"), json2.getInt("revenue"));
        assertNotEquals(json1.getInt("seeds"), json2.getInt("seeds"));
        assertNotEquals(json1.getInt("timeRequired"), json2.getInt("timeRequired"));
    }
}
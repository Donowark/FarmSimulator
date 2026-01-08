package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;

@ExcludeFromJacocoGeneratedReport
public class JsonTest {

    protected void checkPlantedCrop(Crop crop, int plantedArea, PlantedCrop plantedCrop) {
        assertEquals(crop, new Crop(plantedCrop.getName(), plantedCrop.getRevenue(), plantedCrop.getSeeds(),
                plantedCrop.getTimeRequired()));
        assertEquals(plantedArea, plantedCrop.getPlantedArea());
        assertEquals(
                plantedCrop.getName() + ": " + plantedCrop.getRevenue() + ": " + plantedCrop.getSeeds() + ": "
                        + plantedCrop.getTimeRequired() + ": " + plantedCrop.getPlantedArea()
                        + ": " + plantedCrop.getTimeGrown(),
                plantedCrop.toString());
    }

    protected void checkPlantableCrop(String name, int revenue, int seeds, int timeRequired, Crop crop) {
        assertEquals(name, crop.getName());
        assertEquals(revenue, crop.getRevenue());
        assertEquals(seeds, crop.getSeeds());
        assertEquals(timeRequired, crop.getTimeRequired());
        assertEquals(String.format("%s - $%d - %d seeds - %dq", crop.getName(), crop.getRevenue(), crop.getSeeds(),
                crop.getTimeRequired()), crop.toString());
    }
}
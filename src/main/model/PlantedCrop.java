package model;

import org.json.JSONObject;

/*
 * Represents a planted crop with corresponding name, revenue, seeds, and timeRequired. On top of that, timeGrown and
 * plantedArea are recorded.
 */
public class PlantedCrop extends Crop {
    private int timeGrown; // the time that the crop has grown
    private int plantedArea; // the planted area of the crop

    /*
     * REQUIRES: plantedArea >= 0
     * EFFECTS: crop data is set to given crop, plantedArea is set to given
     * plantedArea, timeGrown is set to 0
     */
    public PlantedCrop(Crop crop, int plantedArea) {
        super(crop.name, crop.revenue, crop.seeds, crop.timeRequired);
        this.plantedArea = plantedArea;
        this.timeGrown = 0;
    }

    /*
     * MODIFIES: this
     * EFFECTS: increments this.timeGrown
     */
    public void grow() throws IllegalStateException {
        if (this.isRipe()) {
            throw new IllegalStateException("Cannot grow a ripe crop");
        }
        this.timeGrown++;
    }

    /*
     * REQUIRES: this.timeGrown <= this.timeRequired
     * EFFECTS: if this.timeGrown == this.timeRequired return true, else return
     * false
     */
    public Boolean isRipe() {
        return this.timeGrown == this.timeRequired;
    }

    /*
     * EFFECTS: returns the revenue for this planted crop instance
     */
    public int plantedRevenue() {
        return this.plantedArea * this.revenue;
    }

    public int getTimeGrown() {
        return this.timeGrown;
    }

    public int getPlantedArea() {
        return this.plantedArea;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("revenue", revenue);
        json.put("seeds", seeds);
        json.put("timeRequired", timeRequired);
        json.put("timeGrown", timeGrown);
        json.put("plantedArea", plantedArea);
        return json;
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets this.timeGrown to given timeGrown
     */
    public void setTimeGrown(int timeGrown) {
        this.timeGrown = timeGrown;
    }

    /*
     * EFFECTS: returns string representation of this plantedCrop
     */
    public String toString() {
        return this.name + ": " + this.revenue + ": " + this.seeds + ": " + this.timeRequired + ": " + this.plantedArea
                + ": " + this.timeGrown;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + timeGrown;
        result = prime * result + plantedArea;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PlantedCrop other = (PlantedCrop) obj;
        if (timeGrown != other.timeGrown) {
            return false;
        }
        if (plantedArea != other.plantedArea) {
            return false;
        }
        return true;
    }
}
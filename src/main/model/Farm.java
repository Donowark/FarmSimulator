package model;

import java.util.ArrayList;
import org.json.*;
import exception.*;
import persistence.Writable;

/*
 * Represents a farm with given area (in square kilometres), a list of planted crops with the area of each of them.
 */
public class Farm implements Writable {
    private int area; // the total area of the farm
    private ArrayList<PlantedCrop> crops; // a list of planted crops
    private int time; // the time past in the year (in quarter)
    private int revenue; // the total revenue so far
    private ArrayList<Crop> plantableCrops; // a list of created plantable crops

    /*
     * REQUIRES: area >= 0
     * EFFECTS: area of the farm is set to given number, the list of planted crops
     * is set to empty, the plantedArea and time are set to 0
     */
    public Farm(int area) {
        this.area = area;
        this.crops = new ArrayList<PlantedCrop>();
        this.time = 0;
        this.revenue = 0;
        this.plantableCrops = new ArrayList<Crop>();
        EventLog.getInstance().logEvent(new Event("Created new farm with area: " + area + " sq km"));
    }

    /*
     * REQUIRES: the sum of the area of all the planted crops <= this.area
     * EFFECTS: returns true if the sum of the area of all the planted crops ==
     * this.area, else return false
     */
    public Boolean isFilled() {
        int area;
        area = 0;

        for (PlantedCrop p : this.crops) {
            area += p.getPlantedArea();
        }

        return area == this.area;
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds the the given crop to this.crops (if the crop is already
     * planted, does NOT add the given area to the existing crop, because their
     * timeGrown can be different), decreases crop.seeds by plantArea, throws
     * NonPositiveAreaException if plantArea <= 0, throws FilledFarmException if
     * this.isFilled()
     */
    public void plant(Crop crop, int plantArea) throws NonPositiveAreaException, FilledFarmException {
        if (plantArea <= 0) {
            throw new NonPositiveAreaException();
        }
        if (this.isFilled()) {
            throw new FilledFarmException();
        }
        this.crops.add(new PlantedCrop(crop, plantArea));
        crop.seeds -= plantArea;
        EventLog.getInstance().logEvent(new Event("Planted " + plantArea + " sq km of " + crop.getName()));
    }

    /*
     * MODIFIES: this
     * EFFECTS: increments this.time, passes every PlantedCrop from this.crops to
     * PlantedCrop.grow(), then calls
     * this.harvest()
     */
    public void timeLapses() {
        this.time++;

        for (PlantedCrop p : this.crops) {
            p.grow();
        }

        EventLog.getInstance().logEvent(new Event("Time advanced to quarter " + this.time));
        this.harvest();
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes all the PlantedCrop where PlantedCrop.isRipe() == true from
     * this.crops, add PlantedCrop.plantedRevenue to this.revenue(), does nothing if
     * there is no such PlantedCrop
     */
    public void harvest() {
        int harvestedRevenue = 0;
        int harvestedCrops = 0;

        for (int i = this.crops.size() - 1; i >= 0; i--) {
            PlantedCrop p = this.crops.get(i);
            if (p.isRipe()) {
                harvestedRevenue += p.plantedRevenue();
                harvestedCrops++;
                this.crops.remove(i);
            }
        }

        if (harvestedCrops > 0) {
            this.revenue += harvestedRevenue;
            EventLog.getInstance()
                    .logEvent(new Event("Harvested " + harvestedCrops + " crops, revenue: $" + harvestedRevenue));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes all the PlantedCrop in this.crops, set this.time and
     * this.revenue to 0
     */
    public void reset() {
        this.crops = new ArrayList<PlantedCrop>();
        this.time = 0;
        this.revenue = 0;
        EventLog.getInstance().logEvent(new Event("Farm reset to initial state"));
    }

    /*
     * REQUIRES: the sum of the area of all the planted crops <= this.area
     * EFFECTS: returns the remaining available area of the farm
     */
    public int remainingArea() {
        int totalPlantedArea;
        totalPlantedArea = 0;

        for (PlantedCrop p : this.crops) {
            totalPlantedArea += p.getPlantedArea();
        }

        return this.area - totalPlantedArea;
    }

    public int getArea() {
        return this.area;
    }

    public ArrayList<PlantedCrop> getPlantedCrops() {
        return this.crops;
    }

    public int getTime() {
        return this.time;
    }

    public int getRevenue() {
        return this.revenue;
    }

    public ArrayList<Crop> getPlantableCrops() {
        return this.plantableCrops;
    }

    public void setArea(int area) {
        this.area = area;
        EventLog.getInstance().logEvent(new Event("Farm area set to: " + area + " sq km"));
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("area", this.area);
        json.put("time", this.time);
        json.put("revenue", this.revenue);
        JSONArray jsonArray1 = new JSONArray();

        for (PlantedCrop p : this.crops) {
            jsonArray1.put(p.toJson());
        }

        json.put("crops", jsonArray1);
        JSONArray jsonArray2 = new JSONArray();

        for (Crop c : this.plantableCrops) {
            jsonArray2.put(c.toJson());
        }

        json.put("plantableCrops", jsonArray2);
        return json;
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds plantedCrop to farm
     */
    public void addPlantedCrop(PlantedCrop plantedCrop) {
        this.crops.add(plantedCrop);
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds plantableCrop to farm
     */
    public void addPlantableCrop(Crop plantableCrop) {
        this.plantableCrops.add(plantableCrop);
        EventLog.getInstance().logEvent(new Event("Created new crop: " + plantableCrop.getName() + " - Revenue: $"
                + plantableCrop.getRevenue() + " - Seeds: " + plantableCrop.getSeeds() + " sq km" + " - Time: "
                + plantableCrop.getTimeRequired() + " quarters"));
    }
}
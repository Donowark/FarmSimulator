package model;

import java.util.ArrayList;
import org.json.JSONObject;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import exception.*;
import persistence.Writable;

/*
 * Represents a crop with its name, revenue per square kilometre (in dollar), remaining amount of seeds (in square
 * kilometres), time required to ripe (in quarter).
 */
public class Crop implements Writable {
    protected String name; // the name of the crop
    protected int revenue; // the revenue of the crop
    protected int seeds; // the remaining amount of seeds of the crop
    protected int timeRequired; // the required time for the crop to ripe

    /*
     * EFFECTS: name of the crop is set to cropName not assigned to any other crop;
     * revenue per square kilometre, remaining amount of seeds, and timeRequired are
     * set to given numbers
     */
    public Crop(String name, int revenue, int seeds, int timeRequired) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Crop name cannot be null or empty");
        }
        if (revenue < 0) {
            throw new IllegalArgumentException("Revenue cannot be negative");
        }
        if (seeds < 0) {
            throw new IllegalArgumentException("Seeds amount cannot be negative");
        }
        if (timeRequired < 1 || timeRequired > 4) {
            throw new IllegalArgumentException("Time required must be between 1 and 4 quarters");
        }
        this.name = name;
        this.revenue = revenue;
        this.seeds = seeds;
        this.timeRequired = timeRequired;
    }

    /*
     * REQUIRES: amount > 0
     * MODIFIES: this
     * EFFECTS: increases this.seeds by amount
     */
    public void addSeeds(int amount) throws NonPositiveAreaException {
        if (amount <= 0) {
            throw new NonPositiveAreaException();
        }
        this.seeds += amount;
        EventLog.getInstance().logEvent(new Event(
                "Added " + amount + " sq km seeds to " + this.name + " - Total seeds now: " + this.seeds + " sq km"));
    }

    /*
     * MODIFIES: this
     * EFFECTS: decreases this.seeds by amount
     */
    public void useSeeds(int amount) throws NonPositiveAreaException, InsufficientSeedsException {
        if (amount <= 0) {
            throw new NonPositiveAreaException();
        }
        if (amount > this.seeds) {
            throw new InsufficientSeedsException();
        }
        this.seeds -= amount;
    }

    /*
     * EFFECTS: returns the Crop where Crop.name == name in crops, returns null if
     * such Crop does not exist
     */
    public static Crop findCropByName(ArrayList<Crop> crops, String name) {

        for (Crop c : crops) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }

        return null;
    }

    public String getName() {
        return this.name;
    }

    public int getRevenue() {
        return this.revenue;
    }

    public int getSeeds() {
        return this.seeds;
    }

    public int getTimeRequired() {
        return this.timeRequired;
    }

    /*
     * EFFECTS: returns string representation of this Crop
     */
    public String toString() {
        return String.format("%s - $%d - %d seeds - %dq",
                this.name, this.revenue, this.seeds, this.timeRequired);
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + revenue;
        result = prime * result + seeds;
        result = prime * result + timeRequired;
        return result;
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        Crop other = (Crop) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        } else if (revenue != other.revenue) {
            return false;
        } else if (seeds != other.seeds) {
            return false;
        } else if (timeRequired != other.timeRequired) {
            return false;
        }
        return true;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("revenue", this.revenue);
        json.put("seeds", this.seeds);
        json.put("timeRequired", this.timeRequired);
        return json;
    }
}
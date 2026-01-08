package ui;

import model.*;
import persistence.*;
import java.io.*;
import java.util.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import exception.*;

@ExcludeFromJacocoGeneratedReport
public class FarmApp {
    private static final String JSON_STORE = "./data/farm.json";
    private Farm farm;
    private Scanner input;
    private JsonReader jsonReader;

    /*
     * EFFECTS: runs farm application
     */
    public FarmApp() {
        runFarm();
    }

    /*
     * MODIFIES: this
     * EFFECTS: processes user input
     */
    public void runFarm() {
        Boolean keepGoing = true;
        String command = null;
        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes farm
     */
    public void init() {
        farm = new Farm(0);
        input = new Scanner(System.in);
        jsonReader = new JsonReader(JSON_STORE);
    }

    /*
     * EFFECTS: displays menu of options to user
     */
    public void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> Set farm area");
        System.out.println("\t2 -> Create new crop");
        System.out.println("\t3 -> Add seeds to crop");
        System.out.println("\t4 -> Plant crop");
        System.out.println("\t5 -> Advance time");
        System.out.println("\t6 -> Check farm status");
        System.out.println("\t7 -> Reset farm");
        System.out.println("\ts -> Save farm to file");
        System.out.println("\tl -> Load farm to file");
        System.out.println("\tq -> Quit");
    }

    /*
     * MODIFIES: this
     * EFFECTS: processes user command
     */
    public void processCommand(String command) {
        if (command.equals("1")) {
            doSetArea();
        } else if (command.equals("2")) {
            doCreateCrop();
        } else if (command.equals("3")) {
            doAddSeeds();
        } else if (command.equals("4")) {
            doPlant();
        } else if (command.equals("5")) {
            doTimeLapses();
        } else if (command.equals("6")) {
            doCheckStatus();
        } else if (command.equals("7")) {
            doResetFarm();
        } else if (command.equals("s")) {
            doSaveFarm();
        } else if (command.equals("l")) {
            doLoadFarm();
        } else {
            System.out.println("Invalid selection. Please try again.");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: set this.area to given area
     */
    public void doSetArea() {
        System.out.println("Enter farm area (square kilometers):");
        int area = input.nextInt();
        if (area <= 0) {
            System.out.println("Error: Area must be a positive number.");
        } else {
            farm = new Farm(area);
            System.out.println("Farm area set to " + area + " square kilometers.");
        }
    }

    /*
     * MODIFIES: crops
     * EFFECTS: adds the new created Crop into crops
     */
    public void doCreateCrop() {
        System.out.println("Enter crop name:");
        String name = input.next();
        System.out.println("Enter revenue (dollars per square kilometer):");
        int revenue = input.nextInt();
        System.out.println("Enter available seeds (square kilometers):");
        int seeds = input.nextInt();
        System.out.println("Enter time required to mature (quarters):");
        int timeRequired = input.nextInt();

        try {
            Crop crop = new Crop(name, revenue, seeds, timeRequired);
            farm.addPlantableCrop(crop);
            System.out.println("Crop '" + name + "' created successfully!");
            System.out.println("Revenue: " + revenue + " dollars per square kilometer");
            System.out.println("Available seeds: " + seeds + " square kilometers");
            System.out.println("Time to mature: " + timeRequired + " quarters");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating crop: " + e.getMessage());
            System.out.println("Please ensure:");
            System.out.println("- Crop name is not empty");
            System.out.println("- Revenue is non-negative");
            System.out.println("- Seeds amount is non-negative");
            System.out.println("- Time required is between 1 and 4 quarters");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds seeds to an existing crop
     */
    public void doAddSeeds() {
        System.out.println("Enter crop name to add seeds to:");
        Crop crop = Crop.findCropByName(farm.getPlantableCrops(), input.next());
        if (crop != null) {
            System.out.println("Enter amount of seeds to add (square kilometers):");
            int amount = input.nextInt();
            try {
                crop.addSeeds(amount);
                System.out.println("Added " + amount + " square kilometers of seeds to " + crop.getName() + ".");
                System.out.println("Total seeds now: " + crop.getSeeds() + " square kilometers");
            } catch (NonPositiveAreaException e) {
                System.out.println("Error: Amount must be positive.");
            }
        } else {
            System.out.println("Error: Crop not found.");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: plant the given crop with given area into the farm
     */
    public void doPlant() {
        System.out.println("Enter crop name to plant:");
        Crop cropToPlant = Crop.findCropByName(farm.getPlantableCrops(), input.next());
        if (cropToPlant != null) {
            System.out.println("Enter area to plant (square kilometers):");
            int areaToPlant = input.nextInt();
            if (areaToPlant > farm.getArea()) {
                System.out.println("Error: Not enough farm area available.");
            } else if (areaToPlant > cropToPlant.getSeeds()) {
                System.out.println("Error: Not enough seeds available.");
            } else {
                try {
                    farm.plant(cropToPlant, areaToPlant);
                    System.out
                            .println("Planted " + areaToPlant + " square kilometers of " + cropToPlant.getName() + ".");
                } catch (NonPositiveAreaException e) {
                    System.out.println("Please enter a positive area.");
                } catch (FilledFarmException e) {
                    System.out.println("Your farm is already filled.");
                }
            }
        } else {
            System.out.println("Error: Crop not found. Please create it first.");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: call farm.timePass(), print the harvested crop in this quarter and
     * the revenue get
     */
    public void doTimeLapses() {
        ArrayList<PlantedCrop> cropsToHarvest = getCropsThatWillMatureNextQuarter();

        try {
            advanceTime();
            reportHarvestResults(cropsToHarvest);
        } catch (IllegalStateException e) {
            System.out.println("Error during growth: " + e.getMessage());
        }
    }

    /*
     * EFFECTS: returns a list of crops that will mature in the next quarter
     */
    private ArrayList<PlantedCrop> getCropsThatWillMatureNextQuarter() {
        ArrayList<PlantedCrop> cropsToHarvest = new ArrayList<>();

        for (PlantedCrop crop : farm.getPlantedCrops()) {
            if (willCropMatureNextQuarter(crop)) {
                cropsToHarvest.add(crop);
            }
        }

        return cropsToHarvest;
    }

    /*
     * EFFECTS: returns true if the crop will mature in the next quarter
     */
    private boolean willCropMatureNextQuarter(PlantedCrop crop) {
        return crop.getTimeGrown() + 1 == crop.getTimeRequired();
    }

    /*
     * MODIFIES: this
     * EFFECTS: advances time by one quarter and processes crop growth
     */
    private void advanceTime() {
        farm.timeLapses();
    }

    /*
     * EFFECTS: reports which crops were harvested and the revenue generated
     */
    private void reportHarvestResults(ArrayList<PlantedCrop> cropsThatWouldMature) {
        if (cropsThatWouldMature.isEmpty()) {
            System.out.println("No crops harvested this quarter.");
        } else {
            printHarvestedCrops(cropsThatWouldMature);
            printHarvestRevenue(cropsThatWouldMature);
        }
    }

    /*
     * EFFECTS: prints the list of harvested crops
     */
    private void printHarvestedCrops(ArrayList<PlantedCrop> harvestedCrops) {
        ArrayList<String> harvestedCropNames = new ArrayList<>();
        for (PlantedCrop crop : harvestedCrops) {
            harvestedCropNames.add(crop.getName());
        }
        System.out.println("Harvested crops: " + harvestedCropNames);
    }

    /*
     * EFFECTS: calculates and prints the revenue from harvested crops
     */
    private void printHarvestRevenue(ArrayList<PlantedCrop> harvestedCrops) {
        int totalRevenue = calculateHarvestRevenue(harvestedCrops);
        System.out.println("Revenue from harvest: " + totalRevenue + " dollars");
    }

    /*
     * EFFECTS: calculates total revenue from the list of harvested crops
     */
    private int calculateHarvestRevenue(ArrayList<PlantedCrop> harvestedCrops) {
        int totalRevenue = 0;
        for (PlantedCrop crop : harvestedCrops) {
            totalRevenue += crop.plantedRevenue();
        }
        return totalRevenue;
    }

    /*
     * EFFECTS: prints current season, total revenue, farm area and remaining area,
     * and the current planted crops with time grown
     */
    public void doCheckStatus() {
        printFarmBasicStatus();
        printPlantedCrops();
        printPlantableCrops();
        System.out.println("===================");
    }

    /*
     * EFFECTS: prints the basic farm status including time, revenue, area and
     * available area.
     */
    private void printFarmBasicStatus() {
        System.out.println("\n=== FARM STATUS ===");
        System.out.println("Current quarter: " + farm.getTime());
        System.out.println("Total revenue: " + farm.getRevenue() + " dollars");
        System.out.println("Farm area: " + farm.getArea() + " square kilometers");
        System.out.println("Available area: " + farm.remainingArea() + " square kilometers");
    }

    /*
     * EFFECTS: prints the list of planted crops with their growth progress.
     */
    private void printPlantedCrops() {
        ArrayList<PlantedCrop> plantedCrops = farm.getPlantedCrops();
        if (plantedCrops.isEmpty()) {
            System.out.println("No crops currently planted.");
        } else {
            System.out.println("\nPLANTED CROPS:");
            for (PlantedCrop crop : plantedCrops) {
                System.out.println("- " + crop.getName()
                        + " (" + crop.getPlantedArea() + " square kilometers): "
                        + "Growth: " + crop.getTimeGrown() + "/" + crop.getTimeRequired() + " quarters");
            }
        }
    }

    /*
     * EFFECTS: prints the list of plantable crops with their details.
     */
    private void printPlantableCrops() {
        if (farm.getPlantableCrops().isEmpty()) {
            System.out.println("No crops available for planting.");
        } else {
            System.out.println("\nAVAILABLE CROPS:");
            for (Crop c : farm.getPlantableCrops()) {
                System.out.println("- " + c.getName()
                        + " (Revenue: " + c.getRevenue() + " dollars/sq km, "
                        + "Seeds: " + c.getSeeds() + " sq km, "
                        + "Time to mature: " + c.getTimeRequired() + " quarters)");
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: resets the farm
     */
    public void doResetFarm() {
        farm.reset();
        System.out.println("Farm was successfully reset.");
    }

    /*
     * MODIFIES: this
     * EFFECTS: saves farm to file
     */
    public void doSaveFarm() {
        JsonWriter jsonWriter = new JsonWriter("./data/farm.json");
        try {
            jsonWriter.open();
            jsonWriter.write(farm);
            jsonWriter.close();
            System.out.println("Farm saved successfully to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: loads farm from file
     */
    private void doLoadFarm() {
        try {
            farm = jsonReader.read();
            System.out.println("Farm loaded successfully from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
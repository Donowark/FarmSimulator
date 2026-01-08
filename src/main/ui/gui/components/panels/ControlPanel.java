package ui.gui.components.panels;

import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;
import model.Event;
import persistence.*;
import ui.gui.components.FarmAppGUI;
import ui.gui.managers.*;
import ui.gui.utils.*;
import java.awt.*;
import java.io.*;

@ExcludeFromJacocoGeneratedReport
public class ControlPanel extends Panel {
    private JComboBox<Crop> cropComboBox;
    private JTextField areaField;
    private JButton plantButton;
    private JButton advanceButton;
    private JButton resetButton;
    private JButton setAreaButton;
    private JButton createCropButton;
    private JButton addSeedsButton;
    private JButton saveButton;
    private JButton loadButton;
    private FarmAppGUI farmAppGUI;
    private CropManager cropManager;
    private PlantingManager plantingManager;

    /*
     * EFFECTS: constructs control panel with farm reference
     */
    public ControlPanel(Farm farm, FarmAppGUI farmAppGUI) {
        super(farm);
        this.farmAppGUI = farmAppGUI;
        this.cropManager = new CropManager(farm, this::notifyFarmChanged);
        this.plantingManager = new PlantingManager(farm, this::notifyFarmChanged);
        initializeComponents();
        updateDisplay();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes all GUI components for farm control with proper spacing
     */
    private void initializeComponents() {
        setupPanelLayout();
        setupFarmAreaSection();
        setupCropManagementSection();
        setupPlantingSection();
        setupTimeControlSection();
        setupFarmManagementSection();
        setupDataManagementSection();
        setupEventHandlers();
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up the main panel layout
     */
    private void setupPanelLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Farm Control"));
        setPreferredSize(new Dimension(250, 600));
        add(Box.createVerticalStrut(10));
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up farm area control section
     */
    private void setupFarmAreaSection() {
        add(createSectionTitle("Farm Area"));
        setAreaButton = new JButton("Set Farm Area");
        add(setAreaButton);
        add(Box.createVerticalStrut(15));
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up crop management section
     */
    private void setupCropManagementSection() {
        add(createSectionTitle("Crop Management"));
        createCropButton = new JButton("Create New Crop");
        add(createCropButton);
        addSeedsButton = new JButton("Add Seeds to Crop");
        add(addSeedsButton);
        add(Box.createVerticalStrut(15));
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up planting control section
     */
    private void setupPlantingSection() {
        add(createSectionTitle("Planting"));
        add(new JLabel("Select Crop:"));
        cropComboBox = new JComboBox<>();
        add(cropComboBox);
        add(new JLabel("Planted Area:"));
        areaField = new JTextField(10);
        areaField.setMaximumSize(new Dimension(200, 25));
        add(areaField);
        plantButton = new JButton("Plant");
        add(plantButton);
        add(Box.createVerticalStrut(15));
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up time control section
     */
    private void setupTimeControlSection() {
        add(createSectionTitle("Time Control"));
        advanceButton = new JButton("Advance Time");
        add(advanceButton);
        add(Box.createVerticalStrut(15));
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up farm management section
     */
    private void setupFarmManagementSection() {
        add(createSectionTitle("Farm Management"));
        resetButton = new JButton("Reset Farm");
        add(resetButton);
        add(Box.createVerticalStrut(15));
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up data management section
     */
    private void setupDataManagementSection() {
        add(createSectionTitle("Data Management"));
        saveButton = new JButton("Save Farm");
        add(saveButton);
        loadButton = new JButton("Load Farm");
        add(loadButton);
        add(Box.createVerticalGlue());
    }

    /*
     * EFFECTS: creates a titled section label with proper formatting
     */
    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up event handlers for all control buttons
     */
    private void setupEventHandlers() {
        plantButton.addActionListener(e -> handlePlanting());
        advanceButton.addActionListener(e -> handleTimeAdvance());
        resetButton.addActionListener(e -> handleReset());
        setAreaButton.addActionListener(e -> handleSetArea());
        createCropButton.addActionListener(e -> cropManager.handleCreateCrop(this));
        addSeedsButton.addActionListener(e -> cropManager.handleAddSeeds(this));
        saveButton.addActionListener(e -> handleSaveFarm());
        loadButton.addActionListener(e -> handleLoadFarm());
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: plants selected crop with specified area, shows error message on
     * failure
     */
    private void handlePlanting() {
        try {
            Crop selectedCrop = (Crop) cropComboBox.getSelectedItem();
            int area = Integer.parseInt(areaField.getText());
            plantingManager.handlePlanting(selectedCrop, area, this);
            areaField.setText("");
        } catch (NumberFormatException e) {
            // Handled by PlantingManager
        }
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: advances time by one quarter and updates all panels
     */
    private void handleTimeAdvance() {
        int previousRevenue = farm.getRevenue();
        farm.timeLapses();
        int revenueGained = farm.getRevenue() - previousRevenue;
        String message = "Time advanced to quarter " + farm.getTime();
        if (revenueGained > 0) {
            message += "\nHarvested crops worth: " + FormatUtils.formatCurrency(revenueGained);
        }
        updateAndNotify();
        DialogUtils.showMessageDialog(this, message, "Time Advanced", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: resets the farm to initial state and updates display
     */
    private void handleReset() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure to reset the farm?\nThis will remove all planted crops and reset time and revenue.",
                "Confirm Reset", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            farm.reset();
            updateAndNotify();
            DialogUtils.showMessageDialog(this, "Farm has been reset to initial state", "Farm Reset",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: sets farm area based on user input
     */
    private void handleSetArea() {
        Integer area = getAreaFromUser();
        if (area != null) {
            setFarmAreaAndNotify(area);
        }
    }

    /*
     * EFFECTS: shows dialog to get area input from user, returns area or null if
     * cancelled
     */
    private Integer getAreaFromUser() {
        JTextField areaField = new JTextField();
        Object[] message = {
                "Enter farm area (square kilometers):", areaField
        };
        String[] options = { "Set Area", "Cancel" };
        int result = DialogUtils.showCustomConfirmDialog(this, message, "Set Farm Area", options, options[1]);
        if (result == 0) {
            try {
                return Integer.parseInt(areaField.getText());
            } catch (NumberFormatException e) {
                DialogUtils.showMessageDialog(this, "Please enter a valid number.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: validates and sets farm area, shows appropriate messages
     */
    private void setFarmAreaAndNotify(int area) {
        if (area <= 0) {
            DialogUtils.showMessageDialog(this, "Area must be a positive number.", "Invalid Area",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        farm.setArea(area);
        updateAndNotify();
        showSuccessMessage(area);
    }

    /*
     * EFFECTS: shows success message for area update
     */
    private void showSuccessMessage(int area) {
        String[] okOptions = { "OK" };
        DialogUtils.showCustomConfirmDialog(this, "Farm area set to " + area + " square kilometers.", "Area Updated",
                okOptions, okOptions[0]);
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: saves farm data to file
     */
    private void handleSaveFarm() {
        try {
            JsonWriter jsonWriter = new JsonWriter("./data/farm.json");
            jsonWriter.open();
            jsonWriter.write(farm);
            jsonWriter.close();
            EventLog.getInstance().logEvent(new Event("Farm saved to file: ./data/farm.json"));
            String[] options = { "OK" };
            DialogUtils.showCustomConfirmDialog(this, "Farm saved successfully to ./data/farm.json", "Save Successful",
                    options, options[0]);
        } catch (FileNotFoundException e) {
            DialogUtils.showMessageDialog(this, "Unable to save farm data: " + e.getMessage(), "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * MODIFIES: farm, this
     * EFFECTS: loads farm data from file and ensures proper synchronization
     */
    private void handleLoadFarm() {
        try {
            JsonReader jsonReader = new JsonReader("./data/farm.json");
            Farm loadedFarm = jsonReader.read();
            farmAppGUI.updateAllPanelsFarmReference(loadedFarm);
            EventLog.getInstance().logEvent(new Event("Farm loaded from file: ./data/farm.json"));
            SwingUtilities.invokeLater(() -> {
                updateDisplay();
                notifyFarmChanged();
            });
            String[] options = { "OK" };
            DialogUtils.showCustomConfirmDialog(this, "Farm loaded successfully from ./data/farm.json",
                    "Load Successful", options, options[0]);
        } catch (IOException e) {
            DialogUtils.showMessageDialog(this, "Unable to load farm data: " + e.getMessage(), "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates display and notifies other panels
     */
    private void updateAndNotify() {
        updateDisplay();
        notifyFarmChanged();
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates crop selection list and button states
     */
    @Override
    public void updateDisplay() {
        updateCropComboBox();
        updateButtonStates();
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates the crop selection combo box with current plantable crops
     */
    private void updateCropComboBox() {
        Crop selected = (Crop) cropComboBox.getSelectedItem();
        cropComboBox.removeAllItems();
        cropComboBox.setToolTipText("Format: Name [Revenue per sq km, Seeds available, Time to mature in quarters]");

        for (Crop crop : farm.getPlantableCrops()) {
            cropComboBox.addItem(crop);
        }

        if (selected != null && farm.getPlantableCrops().contains(selected)) {
            cropComboBox.setSelectedItem(selected);
        } else if (cropComboBox.getItemCount() > 0) {
            cropComboBox.setSelectedIndex(0);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates enabled state of buttons based on current farm state
     */
    private void updateButtonStates() {
        boolean hasCrops = !farm.getPlantableCrops().isEmpty();
        boolean hasArea = farm.remainingArea() > 0;
        plantButton.setEnabled(hasCrops && hasArea);
        addSeedsButton.setEnabled(hasCrops);
        advanceButton.setEnabled(true);
        resetButton.setEnabled(true);
        setAreaButton.setEnabled(true);
        createCropButton.setEnabled(true);
        saveButton.setEnabled(true);
        loadButton.setEnabled(true);
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates the farm reference for this panel and its managers
     */
    @Override
    public void setFarm(Farm farm) {
        this.farm = farm;
        this.cropManager = new CropManager(farm, this::notifyFarmChanged);
        this.plantingManager = new PlantingManager(farm, this::notifyFarmChanged);
        updateDisplay();
    }
}
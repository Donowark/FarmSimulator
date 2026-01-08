package ui.gui.managers;

import model.*;
import ui.gui.dialogs.*;
import ui.gui.utils.DialogUtils;
import exception.*;
import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
/*
 * Manages crop-related operations
 */
public class CropManager {
    private final Farm farm;
    private final Runnable updateCallback;

    public CropManager(Farm farm, Runnable updateCallback) {
        this.farm = farm;
        this.updateCallback = updateCallback;
    }

    /*
     * EFFECTS: creates a new crop based on user input
     */
    public void handleCreateCrop(JComponent parent) {
        CropInputDialog dialog = new CropInputDialog();
        Crop crop = dialog.showDialog(parent);
        if (crop != null) {
            try {
                createCrop(crop, parent);
            } catch (CropDuplicateException e) {
                handleDuplicateCrop(parent, crop);
            } catch (IllegalArgumentException e) {
                DialogUtils.showMessageDialog(parent, "Error: " + e.getMessage(), "Invalid Crop Data",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * EFFECTS: adds seeds to selected crop
     */
    public void handleAddSeeds(JComponent parent) {
        if (farm.getPlantableCrops().isEmpty()) {
            DialogUtils.showMessageDialog(parent,
                    "No crops available. Please create a crop first.",
                    "No Crops", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String message = "<html><body>" + "<b>Select crop to add seeds to:</b><br>"
                + "Format: <i>Name [Revenue per sq km, Seeds available, Time to mature]</i><br><br>" + "Where:<br>"
                + "- <b>Revenue per sq km</b>: Income when harvested<br>"
                + "- <b>Seeds available</b>: Amount available for planting (sq km)<br>"
                + "- <b>Time to mature</b>: Quarters needed to grow (1-4)<br><br>" + "Select a crop:";
        Object[] crops = farm.getPlantableCrops().toArray();
        Object initialSelection = crops.length > 0 ? crops[0] : null;
        Object selection = JOptionPane.showInputDialog(parent,
                message,
                "Add Seeds to Crop",
                JOptionPane.QUESTION_MESSAGE,
                null,
                crops,
                initialSelection);
        if (selection != null) {
            addSeedsToCrop((Crop) selection, parent);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: creates a new crop, throws CropDuplicateException if crop with same
     * name already exists
     */
    private void createCrop(Crop crop, JComponent parent) throws CropDuplicateException {

        for (Crop existingCrop : farm.getPlantableCrops()) {
            if (existingCrop.getName().equalsIgnoreCase(crop.getName())) {
                throw new CropDuplicateException("Crop with name '" + crop.getName() + "' already exists");
            }
        }

        try {
            Crop newCrop = new Crop(crop.getName(), crop.getRevenue(), crop.getSeeds(),
                    crop.getTimeRequired());
            farm.addPlantableCrop(newCrop);
            notifyUpdate();
            showSuccessMessage(parent, crop);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: handles duplicate crop situation by asking user whether to replace
     * existing crop
     */
    private void handleDuplicateCrop(JComponent parent, Crop crop) {
        String message = String.format(
                "A crop with the name '%s' already exists.\nDo you want to replace the existing crop?",
                crop.getName());
        String[] options = { "Replace", "Cancel" };
        int result = DialogUtils.showCustomConfirmDialog(parent, message, "Crop Already Exists", options, options[1]);
        if (result == 0) {
            replaceExistingCrop(parent, crop);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: replaces existing crop with new crop data
     */
    private void replaceExistingCrop(JComponent parent, Crop crop) {
        try {
            farm.getPlantableCrops().removeIf(lambdaCrop -> crop.getName().equalsIgnoreCase(crop.getName()));
            Crop newCrop = new Crop(crop.getName(), crop.getRevenue(), crop.getSeeds(),
                    crop.getTimeRequired());
            farm.addPlantableCrop(newCrop);
            notifyUpdate();
            String message = String.format(
                    "Crop '%s' has been replaced successfully!%n" + "Revenue: %d per sq km%n" + "Seeds: %d sq km%n"
                            + "Time to mature: %d quarters",
                    crop.getName(), crop.getRevenue(), crop.getSeeds(), crop.getTimeRequired());
            DialogUtils.showMessageDialog(parent, message, "Crop Replaced", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            DialogUtils.showMessageDialog(parent, "Error: " + e.getMessage(), "Invalid Crop Data",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * MODIFIES: this, crop
     * EFFECTS: adds the specified amount of seeds to the given crop, displays an
     * error message if amount is not positive, otherwise updates the farm display
     * and shows success message
     */
    private void addSeedsToCrop(Crop crop, JComponent parent) {
        SeedsInputDialog dialog = new SeedsInputDialog(crop.getName());
        Integer amount = dialog.showDialog(parent);
        if (amount != null) {
            try {
                crop.addSeeds(amount);
                notifyUpdate();
                showSeedsAddedMessage(parent, crop, amount);
            } catch (NonPositiveAreaException e) {
                DialogUtils.showMessageDialog(parent, "Amount must be positive.", "Invalid Amount",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * EFFECTS: displays a success message dialog for crop creation
     */
    private void showSuccessMessage(JComponent parent, Crop crop) {
        String message = String.format(
                "Crop '%s' created successfully!\nRevenue: %d per sq km\nSeeds: %d sq km\nTime to mature: %d quarters",
                crop.getName(), crop.getRevenue(), crop.getSeeds(), crop.getTimeRequired());
        DialogUtils.showMessageDialog(parent, message, "Crop Created", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * EFFECTS: displays a success message dialog for seeds addition
     */
    private void showSeedsAddedMessage(JComponent parent, Crop crop, int amount) {
        String message = String.format(
                "Added %d square kilometers of seeds to %s.\nTotal seeds now: %d sq km",
                amount, crop.getName(), crop.getSeeds());
        DialogUtils.showMessageDialog(parent, message, "Seeds Added", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * MODIFIES: this
     * EFFECTS: notifies all registered components that farm state has changed
     */
    private void notifyUpdate() {
        if (updateCallback != null) {
            updateCallback.run();
        }
    }
}
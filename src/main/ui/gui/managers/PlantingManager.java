package ui.gui.managers;

import model.*;
import ui.gui.utils.DialogUtils;
import exception.*;
import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
/*
 * Manages planting operations
 */
public class PlantingManager {
    private final Farm farm;
    private final Runnable updateCallback;

    public PlantingManager(Farm farm, Runnable updateCallback) {
        this.farm = farm;
        this.updateCallback = updateCallback;
    }

    /*
     * EFFECTS: plants selected crop with specified area
     */
    public void handlePlanting(Crop selectedCrop, int area, JComponent parent) {
        if (selectedCrop == null) {
            DialogUtils.showMessageDialog(parent, "Please select a crop first.",
                    "No Crop Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (!validatePlanting(selectedCrop, area, parent)) {
                return;
            }
            plantCrop(selectedCrop, area, parent);
        } catch (NumberFormatException e) {
            DialogUtils.showMessageDialog(parent, "Please enter a valid number for area.",
                    "Invalid Area", JOptionPane.ERROR_MESSAGE);
        } catch (NonPositiveAreaException e) {
            DialogUtils.showMessageDialog(parent, "Planted area must be positive.",
                    "Invalid Area", JOptionPane.ERROR_MESSAGE);
        } catch (FilledFarmException e) {
            DialogUtils.showMessageDialog(parent, "Farm is already full. Cannot plant more crops.",
                    "Farm Full", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * EFFECTS: validates planting conditions and returns true if valid
     */
    private boolean validatePlanting(Crop selectedCrop, int area, JComponent parent) {
        if (area > farm.remainingArea()) {
            DialogUtils.showMessageDialog(parent,
                    "Not enough farm area available. Available: " + farm.remainingArea() + " sq km",
                    "Insufficient Area", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (area > selectedCrop.getSeeds()) {
            DialogUtils.showMessageDialog(parent,
                    "Not enough seeds available. Available: " + selectedCrop.getSeeds() + " sq km",
                    "Insufficient Seeds", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /*
     * MODIFIES: this, farm, selectedCrop
     * EFFECTS: plants the specified crop with the given area, updates the farm
     * state and displays success message, throws NonPositiveAreaException if area
     * <= 0, throws FilledFarmException if farm is already full
     */
    private void plantCrop(Crop selectedCrop, int area, JComponent parent)
            throws NonPositiveAreaException, FilledFarmException {
        farm.plant(selectedCrop, area);
        notifyUpdate();
        showSuccessMessage(parent, selectedCrop, area);
    }

    /*
     * EFFECTS: displays a success message dialog for crop planting
     */
    private void showSuccessMessage(JComponent parent, Crop crop, int area) {
        DialogUtils.showMessageDialog(parent, "Successfully planted " + area + " sq km of " + crop.getName(),
                "Planting Successful", JOptionPane.INFORMATION_MESSAGE);
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
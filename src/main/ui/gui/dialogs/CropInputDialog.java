package ui.gui.dialogs;

import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.Crop;
import ui.gui.utils.DialogUtils;

@ExcludeFromJacocoGeneratedReport
/*
 * Dialog for creating new crops
 */
public class CropInputDialog {
    private final JTextField nameField = new JTextField();
    private final JTextField revenueField = new JTextField();
    private final JTextField seedsField = new JTextField();
    private final JTextField timeRequiredField = new JTextField();

    /*
     * EFFECTS: shows the crop creation dialog and returns the input data
     */
    public Crop showDialog(JComponent parent) {
        Object[] message = {
                "Create New Crop - Enter crop details:",
                "Revenue: Income per square kilometer when harvested",
                "Seeds: Initial amount available for planting (sq km)",
                "Time Required: Quarters needed to mature (1-4)",
                "",
                "Crop Name:", nameField,
                "Revenue per sq km:", revenueField,
                "Initial Seeds (sq km):", seedsField,
                "Time Required (quarters 1-4):", timeRequiredField
        };
        String[] options = { "Create Crop", "Cancel" };
        int option = DialogUtils.showCustomConfirmDialog(parent, message,
                "Create New Crop", options, options[1]);
        if (option == 0) {
            return parseInputData();
        }
        return null;
    }

    /*
     * EFFECTS: parses and validates input data from dialog fields
     */
    private Crop parseInputData() {
        try {
            String name = nameField.getText().trim();
            int revenue = Integer.parseInt(revenueField.getText());
            int seeds = Integer.parseInt(seedsField.getText());
            int timeRequired = Integer.parseInt(timeRequiredField.getText());
            Crop cropData = new Crop(name, revenue, seeds, timeRequired);
            return cropData;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter valid numbers for all fields.");
        }
    }
}
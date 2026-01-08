package ui.gui.dialogs;

import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import ui.gui.utils.DialogUtils;

@ExcludeFromJacocoGeneratedReport
/*
 * Dialog for adding seeds to crops
 */
public class SeedsInputDialog {
    private final JTextField seedsField = new JTextField();
    private final String cropName;

    public SeedsInputDialog(String cropName) {
        this.cropName = cropName;
    }

    /*
     * EFFECTS: shows the seeds input dialog and returns the amount
     */
    public Integer showDialog(JComponent parent) {
        Object[] message = {
                "Enter amount of seeds to add (square kilometers):",
                "Note: This adds to the available seeds for planting",
                seedsField
        };
        String[] options = { "Add Seeds", "Cancel" };
        int result = DialogUtils.showCustomConfirmDialog(parent, message,
                "Add Seeds to " + cropName, options, options[1]);
        if (result == 0) {
            try {
                return Integer.parseInt(seedsField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Please enter a valid number.");
            }
        }
        return null;
    }
}
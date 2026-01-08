package ui.gui.utils;

import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
/*
 * Utility class for creating dialogs
 */
public class DialogUtils {

    /*
     * EFFECTS: displays a confirmation dialog with custom button options and
     * returns the user's choice
     */
    public static int showCustomConfirmDialog(JComponent parent, Object message, String title, String[] options,
            String defaultOption) {
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        return JOptionPane.showOptionDialog(parent, message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, defaultOption);
    }

    /*
     * EFFECTS: displays a message dialog with a single OK button and specified
     * message type
     */
    public static void showMessageDialog(JComponent parent, Object message, String title, int messageType) {
        UIManager.put("OptionPane.okButtonText", "OK");
        String[] options = { "OK" };
        JOptionPane.showOptionDialog(parent, message, title, JOptionPane.DEFAULT_OPTION,
                messageType, null, options, options[0]);
    }
}
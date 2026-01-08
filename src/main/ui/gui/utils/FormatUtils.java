package ui.gui.utils;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class FormatUtils {

    private FormatUtils() {

    }

    /*
     * EFFECTS: formats the given amount as currency string with comma separators
     */
    public static String formatCurrency(int amount) {
        return String.format("$%,d", amount);
    }

    /*
     * EFFECTS: formats the given area as a string with "sq km" suffix
     */
    public static String formatArea(int area) {
        return area + " sq km";
    }
}
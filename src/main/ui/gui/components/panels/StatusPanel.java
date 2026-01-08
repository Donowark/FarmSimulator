package ui.gui.components.panels;

import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.Farm;
import ui.gui.utils.FormatUtils;
import java.awt.*;

@ExcludeFromJacocoGeneratedReport
public class StatusPanel extends Panel {
    private JLabel timeLabel;
    private JLabel revenueLabel;
    private JLabel areaLabel;
    private JLabel availableLabel;

    /*
     * EFFECTS: constructs status panel with farm reference and initializes
     * components
     */
    public StatusPanel(Farm farm) {
        super(farm);
        initializeComponents();
        updateDisplay();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes and arranges all GUI components for status display
     */
    private void initializeComponents() {
        setLayout(new GridLayout(4, 2));
        setBorder(BorderFactory.createTitledBorder("Farm Status"));
        add(new JLabel("Current Quarter:"));
        timeLabel = new JLabel();
        add(timeLabel);
        add(new JLabel("Total Revenue:"));
        revenueLabel = new JLabel();
        add(revenueLabel);
        add(new JLabel("Farm Area:"));
        areaLabel = new JLabel();
        add(areaLabel);
        add(new JLabel("Available Area:"));
        availableLabel = new JLabel();
        add(availableLabel);
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates all status labels with current farm data
     */
    @Override
    public void updateDisplay() {
        SwingUtilities.invokeLater(() -> {
            if (farm != null) {
                timeLabel.setText("Quarter " + farm.getTime());
                revenueLabel.setText(FormatUtils.formatCurrency(farm.getRevenue()));
                areaLabel.setText(FormatUtils.formatArea(farm.getArea()));
                availableLabel.setText(FormatUtils.formatArea(farm.remainingArea()));
            }
        });
    }
}
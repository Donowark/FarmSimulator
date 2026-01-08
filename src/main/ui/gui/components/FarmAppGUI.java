package ui.gui.components;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import model.*;
import ui.gui.components.panels.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
/*
 * Represents the main window in which the farm is simulated
 */
public class FarmAppGUI extends JFrame {
    private Farm farm;
    private StatusPanel statusPanel;
    private FarmPanel farmPanel;
    private ControlPanel controlPanel;

    /*
     * EFFECTS: constructs main farm simulator window and initializes all components
     */
    public FarmAppGUI() {
        super("Farm Simulator");
        this.farm = new Farm(0);
        initializeGUI();
        setupWindowListener();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes all GUI components and sets up window layout
     */
    private void initializeGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createPanels();
        setupLayout();
        setupPanelCommunication();
        updateAllPanels();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up window listener to print event log when application closes
     */
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
            }
        });
    }

    /*
     * EFFECTS: prints all events from the event log to console when application
     * closes
     */
    private void printEventLog() {
        System.out.println("=== FARM SIMULATOR EVENT LOG ===");
        System.out.println("Application closed at: " + java.time.LocalDateTime.now());
        System.out.println("--------------------------------");
        EventLog eventLog = EventLog.getInstance();
        
        for (Event event : eventLog) {
            System.out.println(event.toString());
            System.out.println("--------------------------------");
        }

        System.out.println("Total events recorded: " + getEventCount(eventLog));
        System.out.println("=== END OF EVENT LOG ===");
    }

    /*
     * EFFECTS: returns the number of events in the event log
     */
    private int getEventCount(EventLog eventLog) {
        int count = 0;
        for (Event event : eventLog) {
            count++;
        }
        return count;
    }

    /*
     * MODIFIES: this
     * EFFECTS: creates all farm panels
     */
    private void createPanels() {
        statusPanel = new StatusPanel(farm);
        farmPanel = new FarmPanel(farm);
        controlPanel = new ControlPanel(farm, this);
    }

    /*
     * MODIFIES: this
     * EFFECTS: arranges panels in the main window layout
     */
    private void setupLayout() {
        add(statusPanel, BorderLayout.NORTH);
        add(farmPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up communication between panels so they update together
     */
    private void setupPanelCommunication() {
        Runnable updateAllCallback = this::updateAllPanels;
        statusPanel.setUpdateCallback(updateAllCallback);
        farmPanel.setUpdateCallback(updateAllCallback);
        controlPanel.setUpdateCallback(updateAllCallback);
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates all panels to reflect current farm state
     */
    public void updateAllPanels() {
        SwingUtilities.invokeLater(() -> {
            statusPanel.updateDisplay();
            farmPanel.updateDisplay();
            controlPanel.updateDisplay();
            revalidate();
            repaint();
        });
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates farm reference for all panels and ensures complete
     * synchronization
     */
    public void updateAllPanelsFarmReference(Farm newFarm) {
        this.farm = newFarm;
        statusPanel.setFarm(newFarm);
        farmPanel.setFarm(newFarm);
        controlPanel.setFarm(newFarm);
        SwingUtilities.invokeLater(() -> {
            updateAllPanels();
            revalidate();
            repaint();
        });
    }

    /*
     * EFFECTS: returns the current farm instance
     */
    public Farm getFarm() {
        return farm;
    }
}
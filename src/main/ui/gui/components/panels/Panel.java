package ui.gui.components.panels;

import javax.swing.JPanel;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.Farm;

@ExcludeFromJacocoGeneratedReport
public abstract class Panel extends JPanel {
    protected Farm farm;
    protected Runnable updateCallback;

    /*
     * EFFECTS: constructs a panel with reference to the farm model
     */
    public Panel(Farm farm) {
        this.farm = farm;
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets the callback to be called when farm state changes
     */
    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }

    /*
     * MODIFIES: this
     * EFFECTS: notifies all panels that farm state has changed
     */
    protected void notifyFarmChanged() {
        if (updateCallback != null) {
            updateCallback.run();
        }
    }

    /*
     * EFFECTS: updates the display of this panel to reflect current farm state
     */
    public abstract void updateDisplay();

    /*
     * MODIFIES: this
     * EFFECTS: notifies the panel that farm state has changed and triggers display
     * update
     */
    public void onFarmChanged() {
        updateDisplay();
    }

    /*
     * MODIFIES: this
     * EFFECTS: updates the farm reference for this panel
     */
    public void setFarm(Farm farm) {
        this.farm = farm;
        updateDisplay();
    }
}
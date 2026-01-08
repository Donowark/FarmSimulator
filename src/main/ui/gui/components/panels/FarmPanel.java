package ui.gui.components.panels;

import javax.swing.*;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import java.awt.*;
import model.*;
import java.util.List;
import java.util.*;

@ExcludeFromJacocoGeneratedReport
public class FarmPanel extends Panel {
    private static final int PANEL_PADDING = 10;
    private static final int CROP_MARGIN = 3;
    private static final int MIN_CROP_HEIGHT = 25;
    private final CropDrawer cropDrawer;

    /*
     * EFFECTS: constructs a farm panel with the given farm reference, initializes
     * the CropDrawer and sets the preferred size
     */
    public FarmPanel(Farm farm) {
        super(farm);
        this.cropDrawer = new CropDrawer();
        setPreferredSize(new Dimension(600, 400));
        setBorder(BorderFactory.createTitledBorder("Farm"));
    }

    /*
     * MODIFIES: this
     * EFFECTS: paints the farm panel component, drawing the farm visualization with
     * all crops and status information
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFarm(g);
    }

    /*
     * EFFECTS: draws the complete farm visualization including farm information and
     * farm area with crops
     */
    private void drawFarm(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        drawFarmInfo(g);
        drawFarmArea(g);
    }

    /*
     * EFFECTS: draws the farm information text at the top of the panel including
     * total area, planted area, and available area
     */
    private void drawFarmInfo(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Farm Area: " + farm.getArea() + " sq km", PANEL_PADDING, PANEL_PADDING + 12);
        g2d.drawString("Planted: " + getTotalPlantedArea() + " sq km", PANEL_PADDING + 150, PANEL_PADDING + 12);
        g2d.drawString("Available: " + farm.remainingArea() + " sq km", PANEL_PADDING + 300, PANEL_PADDING + 12);
    }

    /*
     * EFFECTS: calculates and returns the total planted area across all crops
     */
    private int getTotalPlantedArea() {
        return farm.getPlantedCrops().stream()
                .mapToInt(PlantedCrop::getPlantedArea)
                .sum();
    }

    /*
     * EFFECTS: draws the farm area visualization including crop rectangles and
     * empty area, or displays appropriate messages if farm has no area or no crops
     * planted
     */
    private void drawFarmArea(Graphics g) {
        int farmArea = farm.getArea();
        if (farmArea == 0) {
            drawNoAreaMessage(g);
            return;
        }
        List<PlantedCrop> crops = farm.getPlantedCrops();
        if (crops.isEmpty()) {
            drawNoCropsMessage(g);
            return;
        }
        List<PlantedCrop> mergedCrops = mergeCrops(crops);
        int farmVisualHeight = getHeight() - PANEL_PADDING * 3 - 20;
        int farmWidth = getWidth() - PANEL_PADDING * 2;
        int startY = PANEL_PADDING * 2 + 15;
        drawFarmBoundary(g, PANEL_PADDING, startY, farmWidth, farmVisualHeight);
        drawCropsAndEmptyArea(g, PANEL_PADDING, startY, farmWidth, farmVisualHeight, mergedCrops, farmArea);
    }

    /*
     * EFFECTS: merges crops with the same name and growth stage to reduce visual
     * duplication, returns a list of merged crops
     */
    private List<PlantedCrop> mergeCrops(List<PlantedCrop> crops) {
        Map<String, PlantedCrop> mergedMap = new HashMap<>();

        for (PlantedCrop crop : crops) {
            String key = crop.getName() + "_" + crop.getTimeGrown();
            if (mergedMap.containsKey(key)) {
                PlantedCrop existing = mergedMap.get(key);
                int mergedArea = existing.getPlantedArea() + crop.getPlantedArea();
                PlantedCrop mergedCrop = new PlantedCrop(
                        new Crop(crop.getName(), crop.getRevenue(), crop.getSeeds(), crop.getTimeRequired()),
                        mergedArea);
                mergedCrop.setTimeGrown(crop.getTimeGrown());
                mergedMap.put(key, mergedCrop);
            } else {
                mergedMap.put(key, crop);
            }
        }

        return new ArrayList<>(mergedMap.values());
    }

    /*
     * EFFECTS: draws the farm boundary rectangle at the specified position and
     * dimensions
     */
    private void drawFarmBoundary(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);
    }

    /*
     * EFFECTS: draws all crops and empty area within the farm boundary, scaled
     * proportionally to their area
     */
    private void drawCropsAndEmptyArea(Graphics g, int x, int y, int width, int height, List<PlantedCrop> crops,
            int farmArea) {
        int currentY = y;
        int totalPlantedArea = getTotalPlantedArea();

        for (PlantedCrop crop : crops) {
            int cropHeight = calculateCropHeight(crop.getPlantedArea(), farmArea, height);
            cropDrawer.drawCropRectangle(g, crop, x, currentY, width, cropHeight);
            currentY += cropHeight + CROP_MARGIN;
        }

        int emptyArea = farmArea - totalPlantedArea;
        if (emptyArea > 0) {
            int emptyHeight = calculateCropHeight(emptyArea, farmArea, height);
            drawEmptyRectangle(g, x, currentY, width, emptyHeight, emptyArea);
        }
    }

    /*
     * EFFECTS: calculates the visual height for a crop based on its area proportion
     * to total farm area, ensures minimum height for visibility
     */
    private int calculateCropHeight(int cropArea, int farmArea, int totalHeight) {
        double proportion = (double) cropArea / farmArea;
        int height = (int) (proportion * totalHeight);
        return Math.max(MIN_CROP_HEIGHT, height);
    }

    /*
     * EFFECTS: draws the empty (unplanted) area rectangle with dashed border and
     * area information text
     */
    private void drawEmptyRectangle(Graphics g, int x, int y, int width, int height, int emptyArea) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.GRAY);
        float[] dashPattern = { 5, 5 };
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));
        g2d.drawRect(x, y, width, height);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        String areaText = "Empty Area: " + emptyArea + " sq km";
        double percentage = (double) emptyArea / farm.getArea() * 100;
        String percentText = String.format("%.1f%% of farm", percentage);
        int areaTextWidth = g2d.getFontMetrics().stringWidth(areaText);
        int percentTextWidth = g2d.getFontMetrics().stringWidth(percentText);
        int areaTextX = x + (width - areaTextWidth) / 2;
        int percentTextX = x + (width - percentTextWidth) / 2;
        g2d.drawString(areaText, areaTextX, y + height / 2 - 5);
        g2d.drawString(percentText, percentTextX, y + height / 2 + 10);
    }

    /*
     * EFFECTS: draws a message indicating no crops are planted
     */
    private void drawNoCropsMessage(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.ITALIC, 16));
        String message = "No crops planted";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String instruction = "Use the control panel to plant crops";
        int instX = (getWidth() - fm.stringWidth(instruction)) / 2;
        g.drawString(instruction, instX, y + 25);
    }

    /*
     * EFFECTS: draws a warning message when farm area is zero
     */
    private void drawNoAreaMessage(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String message = "Farm area is zero - please set farm area first";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }

    @Override
    public void updateDisplay() {
        SwingUtilities.invokeLater(() -> {
            repaint();
        });
    }
}
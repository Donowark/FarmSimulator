package ui.gui.components.panels;

import java.awt.*;
import javax.swing.ImageIcon;
import model.PlantedCrop;
import ui.gui.utils.FormatUtils;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class CropDrawer {
    private static final int INFO_PADDING = 5;
    private static final int IMAGE_AREA_WIDTH = 50;

    public CropDrawer() {

    }

    /*
     * EFFECTS: draws a crop rectangle with image and information at the specified
     * position and dimensions
     */
    public void drawCropRectangle(Graphics g, PlantedCrop crop, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Color cropColor = getCropColor(crop.getName());
        g2d.setColor(cropColor);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, width, height);
        drawCropImage(g2d, crop, x, y, width, height);
        drawCropInfoHorizontal(g2d, crop, x, y, width, height);
    }

    /*
     * EFFECTS: draws the crop image within the specified rectangle, centered
     * vertically
     */
    private void drawCropImage(Graphics2D g, PlantedCrop crop, int x, int y, int width, int height) {
        ImageIcon cropImage = ui.gui.managers.CropImageManager.getCropImage(crop.getName());
        if (cropImage != null && height >= 40) {
            int imgX = x + INFO_PADDING;
            int imgY = y + (height - cropImage.getIconHeight()) / 2;
            g.drawImage(cropImage.getImage(), imgX, imgY, null);
        }
    }

    /*
     * EFFECTS: draws crop information (name, growth progress, area, revenue)
     * horizontally within the rectangle
     */
    private void drawCropInfoHorizontal(Graphics2D g, PlantedCrop crop, int x, int y, int width, int height) {
        int textX = x + IMAGE_AREA_WIDTH;
        int textY = y + height / 2;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String name = crop.getName();
        int nameWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, textX, textY - 8);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        String growthText = "Growth: " + crop.getTimeGrown() + "/" + crop.getTimeRequired();
        String areaText = "Area: " + crop.getPlantedArea() + " sq km";
        int expectedRevenue = crop.plantedRevenue();
        String revenueText = "Revenue: " + FormatUtils.formatCurrency(expectedRevenue);
        FontMetrics fm = g.getFontMetrics();
        int growthWidth = fm.stringWidth(growthText);
        int areaWidth = fm.stringWidth(areaText);
        int detailStartX = textX + nameWidth + 15;
        g.drawString(growthText, detailStartX, textY - 8);
        g.drawString(areaText, detailStartX + growthWidth + 10, textY - 8);
        g.drawString(revenueText, detailStartX + growthWidth + areaWidth + 20, textY - 8);
    }

    /*
     * EFFECTS: generates a consistent color for the given crop name based on its
     * hash code
     */
    private Color getCropColor(String cropName) {
        int hash = cropName.hashCode();
        float hue = Math.abs(hash % 1000) / 1000.0f;
        return Color.getHSBColor(hue, 0.15f, 0.95f);
    }
}
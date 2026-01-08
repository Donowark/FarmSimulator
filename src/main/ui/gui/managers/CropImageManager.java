package ui.gui.managers;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.InputStream;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class CropImageManager {
    private static final Map<String, String> CROP_IMAGE_MAP = new HashMap<>();
    private static final Map<String, ImageIcon> IMAGE_CACHE = new HashMap<>();
    private static final int IMAGE_WIDTH = 40;
    private static final int IMAGE_HEIGHT = 40;
    private static final String DEFAULT_IMAGE_PATH = "/images/crops/default.png";

    static {
        initializeCropImageMapping();
    }

    private static void initializeCropImageMapping() {
        CROP_IMAGE_MAP.put("wheat", "/images/crops/wheat.png");
        CROP_IMAGE_MAP.put("corn", "/images/crops/corn.png");
        CROP_IMAGE_MAP.put("soybean", "/images/crops/soybean.png");
        CROP_IMAGE_MAP.put("rice", "/images/crops/rice.png");
        CROP_IMAGE_MAP.put("potato", "/images/crops/potato.png");
        CROP_IMAGE_MAP.put("carrot", "/images/crops/carrot.png");
        CROP_IMAGE_MAP.put("tomato", "/images/crops/tomato.png");
        CROP_IMAGE_MAP.put("barley", "/images/crops/barley.png");
        CROP_IMAGE_MAP.put("canola", "/images/crops/canola.png");
        CROP_IMAGE_MAP.put("oat", "/images/crops/oat.png");
    }

    /*
     * EFFECTS: returns a resized ImageIcon for the given crop name, returns default
     * image if crop image not found
     */
    public static ImageIcon getCropImage(String cropName) {
        if (cropName == null || cropName.trim().isEmpty()) {
            return getDefaultImage();
        }
        String normalizedName = cropName.toLowerCase().trim();
        if (IMAGE_CACHE.containsKey(normalizedName)) {
            return IMAGE_CACHE.get(normalizedName);
        }
        ImageIcon image = loadCropImage(normalizedName);
        IMAGE_CACHE.put(normalizedName, image);
        return image;
    }

    /*
     * EFFECTS: tries to load image, return default image if not found
     */
    private static ImageIcon loadCropImage(String cropName) {
        String imagePath = CROP_IMAGE_MAP.get(cropName);
        if (imagePath != null) {
            ImageIcon icon = loadImage(imagePath);
            if (icon != null) {
                return resizeIcon(icon);
            }
        }
        return getDefaultImage();
    }

    /*
     * EFFECTS: tries to load image from the given path, returns null if image
     * cannot be loaded
     */
    private static ImageIcon loadImage(String imagePath) {
        try {
            String[] paths = {
                    imagePath,
                    imagePath.startsWith("/") ? imagePath.substring(1) : "/" + imagePath,
                    "resources" + imagePath,
                    "src/main/resources" + imagePath
            };

            for (String path : paths) {
                InputStream stream = CropImageManager.class.getClassLoader().getResourceAsStream(path);
                if (stream != null) {
                    byte[] imageData = stream.readAllBytes();
                    return new ImageIcon(imageData);
                }
            }

        } catch (Exception e) {
            System.err.println("Failed to load image from: " + imagePath + ", Error: " + e.getMessage());
        }
        return null;
    }

    /*
     * EFFECTS: returns the default crop image, caches it for future use
     */
    private static ImageIcon getDefaultImage() {
        ImageIcon resizedIcon = resizeIcon(loadImage(DEFAULT_IMAGE_PATH));
        IMAGE_CACHE.put("default", resizedIcon);
        return resizedIcon;
    }

    /*
     * EFFECTS: resizes the given ImageIcon to standard dimensions (IMAGE_WIDTH x
     * IMAGE_HEIGHT)
     */
    private static ImageIcon resizeIcon(ImageIcon original) {
        Image scaledImage = original.getImage().getScaledInstance(
                IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /*
     * EFFECTS: returns the standard image dimensions used for crop images
     */
    public static Dimension getImageSize() {
        return new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT);
    }
}
package vastraveda.features.feature5_occasions;

import vastraveda.core.utils.BaseUI;

import java.awt.Color;

/**
 * Feature 5 — Occasion Guide Service.
 * Provides styling tips and accent colours for each occasion.
 */
public class Feature5Service {

    /**
     * Returns a short styling tip for the given occasion.
     * Occasion values are case-insensitive (e.g. "wedding", "Wedding").
     */
    public String getTipForOccasion(String occasion) {
        if (occasion == null || occasion.trim().isEmpty()) {
            return "Select an occasion above to see styling guidance.";
        }

        String key = occasion.trim().toLowerCase();
        switch (key) {
            case "wedding":
                return "Brides traditionally wear red or maroon silk. "
                    + "Grooms often choose a sherwani with a coordinating dupatta.";
            case "festival":
                return "Choose bright colours and rich fabrics. Avoid plain black or white "
                    + "during most Hindu festivals.";
            case "religious":
                return "Prefer modest, covered clothing. Cotton or silk in soft, muted tones "
                    + "is ideal for temple and mosque visits.";
            case "formal":
                return "For professional events, pick a structured kurta set or neatly draped "
                    + "saree with minimal embellishment.";
            case "casual":
                return "Light cotton kurtas, churidars, and simple salwar suits are everyday "
                    + "staples across many regions.";
            case "dance":
                return "Classical dance forms have specific costumes — for example, Bharatanatyam "
                    + "often uses bright silk with a bold gold border.";
            case "winter":
                return "Layer a warm shawl or stole over your outfit. Woollen angrakhas and "
                    + "shawls are popular for North India winters.";
            default:
                return "Dress to balance comfort, climate, and cultural context for the occasion.";
        }
    }

    /**
     * Returns an accent colour to theme buttons or cards for a given occasion.
     */
    public Color getAccentColor(String occasion) {
        if (occasion == null) {
            return BaseUI.COLOR_PRIMARY;
        }
        String key = occasion.trim().toLowerCase();
        switch (key) {
            case "wedding":
                return BaseUI.COLOR_ACCENT;
            case "festival":
                return BaseUI.COLOR_SECONDARY;
            case "religious":
                return new Color(80, 120, 160); // calm blue
            case "formal":
                return new Color(90, 60, 40);   // muted brown
            case "casual":
                return new Color(46, 139, 87);  // soft green
            case "dance":
                return new Color(186, 85, 211); // vibrant purple
            case "winter":
                return new Color(70, 130, 180); // cool blue
            default:
                return BaseUI.COLOR_PRIMARY;
        }
    }
}

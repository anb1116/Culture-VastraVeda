package vastraveda.features.feature7_timeline;

import vastraveda.core.data.DataStore;
import vastraveda.core.models.ClothingItem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Feature 7 — Historical Timeline Service
 * Provides era metadata and data filtering logic.
 */
public class Feature7Service {

    // { eraName, timePeriod, description, colorHex, filterKeywordCsv }
    private static final String[][] ERAS = {
        {
            "Ancient / Vedic",
            "3000 BCE - 600 CE",
            "Unstitched draped garments dominated this period, with early cotton cultivation and hand-spun textiles shaping identity.",
            "#4CAF50",
            "ancient,vedic,indus"
        },
        {
            "Classical",
            "600 CE - 1200 CE",
            "Regional dressing styles expanded, temple traditions influenced aesthetics, and silk weaving gained prominence.",
            "#2196F3",
            "classical,medieval,early medieval"
        },
        {
            "Mughal Era",
            "1526 - 1857",
            "Tailored silhouettes, zari and brocade craftsmanship, and court fashion deeply influenced Indian attire.",
            "#9C27B0",
            "mughal,sultanate,persian"
        },
        {
            "Colonial Period",
            "1858 - 1947",
            "Indian and Western clothing practices blended, while khadi emerged as a symbol of resistance and self-reliance.",
            "#FF9800",
            "colonial,british,pre-independence,khadi"
        },
        {
            "Post-Independence",
            "1947 - 1990",
            "National identity and state handloom boards strengthened traditional weaves in everyday and ceremonial use.",
            "#F44336",
            "post-independence,independent,handloom"
        },
        {
            "Modern Era",
            "1990 - Present",
            "Contemporary Indian fashion blends heritage garments with modern cuts, global influence, and designer experimentation.",
            "#00BCD4",
            "modern,contemporary,present,current"
        }
    };

    public String[][] getEras() {
        return ERAS;
    }

    public Color getColorForEra(String eraName) {
        for (String[] era : ERAS) {
            if (era[0].equalsIgnoreCase(eraName)) {
                return decodeColor(era[3]);
            }
        }
        return decodeColor("#8B4513");
    }

    public List<ClothingItem> getItemsForEra(String eraName) {
        List<ClothingItem> result = new ArrayList<>();
        String keywordCsv = getKeywordCsv(eraName);
        if (keywordCsv.isEmpty()) {
            return result;
        }

        String[] keywords = keywordCsv.split(",");
        for (ClothingItem item : DataStore.getAllItems()) {
            String eraText = safeLower(item.getEra());
            for (String keyword : keywords) {
                String trimmed = keyword.trim().toLowerCase();
                if (!trimmed.isEmpty() && eraText.contains(trimmed)) {
                    result.add(item);
                    break;
                }
            }
        }
        return result;
    }

    private String getKeywordCsv(String eraName) {
        for (String[] era : ERAS) {
            if (era[0].equalsIgnoreCase(eraName)) {
                return era[4];
            }
        }
        return "";
    }

    private Color decodeColor(String colorHex) {
        try {
            return Color.decode(colorHex);
        } catch (Exception ignored) {
            return new Color(139, 69, 19);
        }
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }
}

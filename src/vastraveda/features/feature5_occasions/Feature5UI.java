package vastraveda.features.feature5_occasions;

import vastraveda.core.data.DataStore;
import vastraveda.core.models.ClothingItem;
import vastraveda.core.utils.BaseUI;
import vastraveda.core.utils.Feature;
import vastraveda.core.utils.FilterUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  FEATURE 5 — Occasion Guide                                      ║
 * ║  CONTRIBUTOR: Your Name (@github_handle)                         ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  📋 WHAT TO BUILD:                                               ║
 * ║  • Occasion buttons: Wedding, Festival, Religious, Formal,       ║
 * ║    Casual, Dance, Winter.                                        ║
 * ║  • Clicking filters DataStore items by occasion and renders      ║
 * ║    result cards.                                                 ║
 * ║  • Show a styling tip panel below results (hardcode tips in      ║
 * ║    Feature5Service).                                             ║
 * ║  • Use FilterUtils.filterByOccasion() and GridLayout(0, 2)       ║
 * ║    for the card grid.                                            ║
 * ║  • See README.md in this folder for tip text and layout diagram. ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  📁 ONLY MODIFY THESE FILES IN THIS FOLDER:                      ║
 * ║     Feature5UI.java       ← Swing UI code                        ║
 * ║     Feature5Service.java  ← Data/logic                           ║
 * ║     README.md             ← Full spec + layout diagram           ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class Feature5UI extends BaseUI implements Feature {

    private static final String OCC_WEDDING = "Wedding";
    private static final String OCC_FESTIVAL = "Festival";
    private static final String OCC_RELIGIOUS = "Religious";
    private static final String OCC_FORMAL = "Formal";
    private static final String OCC_CASUAL = "Casual";
    private static final String OCC_DANCE = "Dance";
    private static final String OCC_WINTER = "Winter";

    private final Feature5Service service = new Feature5Service();

    private JLabel occasionLabel;
    private JPanel resultsGrid;
    private JTextArea tipArea;

    public Feature5UI() {
        super("Occasion Guide");
        buildUI();
    }

    @Override
    public void render() {
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(createHeader("🎊  Occasion Guide",
            "Find the right outfit for every event — from weddings to winter gatherings."),
            BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(COLOR_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Occasion buttons row
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        buttonRow.setOpaque(false);
        addOccasionButton(buttonRow, "💍 Wedding", OCC_WEDDING);
        addOccasionButton(buttonRow, "🎉 Festival", OCC_FESTIVAL);
        addOccasionButton(buttonRow, "🕌 Religious", OCC_RELIGIOUS);
        addOccasionButton(buttonRow, "👔 Formal", OCC_FORMAL);
        addOccasionButton(buttonRow, "👕 Casual", OCC_CASUAL);
        addOccasionButton(buttonRow, "💃 Dance", OCC_DANCE);
        addOccasionButton(buttonRow, "❄️ Winter", OCC_WINTER);

        // Results + tip area
        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.setOpaque(false);

        occasionLabel = new JLabel("Select an occasion above to get started.");
        occasionLabel.setFont(FONT_LABEL);
        occasionLabel.setForeground(COLOR_TEXT);
        centerPanel.add(occasionLabel, BorderLayout.NORTH);

        resultsGrid = new JPanel(new GridLayout(0, 2, 12, 12));
        resultsGrid.setBackground(COLOR_BG);

        JScrollPane scrollPane = new JScrollPane(resultsGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_BG);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Tip panel
        JPanel tipPanel = new JPanel(new BorderLayout(6, 4));
        tipPanel.setBackground(new Color(255, 253, 220)); // light yellow
        tipPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 210, 150)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel tipTitle = new JLabel("💡 Occasion Tip");
        tipTitle.setFont(FONT_LABEL);
        tipPanel.add(tipTitle, BorderLayout.NORTH);

        tipArea = new JTextArea("Styling guidance will appear here once you pick an occasion.");
        tipArea.setEditable(false);
        tipArea.setLineWrap(true);
        tipArea.setWrapStyleWord(true);
        tipArea.setFont(FONT_BODY.deriveFont(Font.ITALIC));
        tipArea.setForeground(COLOR_TEXT);
        tipArea.setBackground(new Color(255, 253, 220));
        tipArea.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        tipPanel.add(tipArea, BorderLayout.CENTER);

        centerPanel.add(tipPanel, BorderLayout.SOUTH);

        content.add(buttonRow, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // Optionally show all items summary when first opened
        if (!DataStore.getAllItems().isEmpty()) {
            occasionLabel.setText("Select an occasion above to explore "
                + DataStore.getAllItems().size() + " traditional outfits.");
        }
    }

    private void addOccasionButton(JPanel container, String label, String occasionKey) {
        JButton button = createStyledButton(label,
            service.getAccentColor(occasionKey),
            COLOR_TEXT_LIGHT);
        button.addActionListener(e -> showOccasion(occasionKey));
        container.add(button);
    }

    private void showOccasion(String occasion) {
        occasionLabel.setText("Occasion: " + occasion.toUpperCase());

        List<ClothingItem> items = FilterUtils.filterByOccasion(occasion);
        resultsGrid.removeAll();

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel(
                "No outfits are tagged for this occasion yet. Check back as the wardrobe grows.");
            emptyLabel.setFont(FONT_BODY);
            emptyLabel.setForeground(Color.DARK_GRAY);
            resultsGrid.add(emptyLabel);
        } else {
            for (ClothingItem item : items) {
                resultsGrid.add(createItemCard(item, occasion));
            }
        }

        tipArea.setText(service.getTipForOccasion(occasion));

        resultsGrid.revalidate();
        resultsGrid.repaint();
    }

    private JPanel createItemCard(ClothingItem item, String occasion) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout(4, 4));

        String titleText = item.getImageIcon() + " " + item.getName();
        JLabel title = new JLabel(titleText);
        title.setFont(FONT_LABEL);
        title.setForeground(service.getAccentColor(occasion));
        card.add(title, BorderLayout.NORTH);

        String meta = item.getRegion() + " · " + item.getFabricType();
        JLabel metaLabel = new JLabel(meta);
        metaLabel.setFont(FONT_SMALL);
        metaLabel.setForeground(COLOR_TEXT);
        card.add(metaLabel, BorderLayout.CENTER);

        JButton detailsBtn = createStyledButton("Details", COLOR_PRIMARY, COLOR_TEXT_LIGHT);
        detailsBtn.setFont(FONT_SMALL);
        detailsBtn.addActionListener(e -> showDetails(item));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        south.setOpaque(false);
        south.add(detailsBtn);
        card.add(south, BorderLayout.SOUTH);

        return card;
    }

    private void showDetails(ClothingItem item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getImageIcon()).append(" ").append(item.getName()).append("\n\n")
          .append("Region: ").append(item.getRegion()).append("\n")
          .append("Fabric: ").append(item.getFabricType()).append("\n")
          .append("Occasion: ").append(item.getOccasion()).append("\n")
          .append("Gender: ").append(item.getGender()).append("\n")
          .append("Era: ").append(item.getEra()).append("\n\n")
          .append(item.getDescription()).append("\n\n")
          .append("Care: ").append(item.getCareInstructions());

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(FONT_BODY);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(420, 260));

        JOptionPane.showMessageDialog(
            this,
            scroll,
            item.getName(),
            JOptionPane.PLAIN_MESSAGE
        );
    }

    /** Optional standalone entry point for testing this feature directly. */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Feature5UI ui = new Feature5UI();
            ui.render();
        });
    }
}
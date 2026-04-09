package vastraveda.features.feature7_timeline;

import vastraveda.core.models.ClothingItem;
import vastraveda.core.utils.BaseUI;
import vastraveda.core.utils.Feature;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  FEATURE 7 — Historical Timeline                                   ║
 * ║  CONTRIBUTOR: Your Name (@github_handle)                         ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  📋 WHAT TO BUILD:                                              ║
 * ║  • Vertical scrollable timeline of 5+ Indian clothing eras.  ║
 * ║  • Left: JList era selector. Right: JScrollPane with stacked era cards.║
 * ║  • Each card: era name, time period, description + matching DataStore items.║
 * ║  • Match items using item.getEra() field. Color-accent each era card.║
 * ║  • See README.md in this folder for era data, colors, and layout.║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  📁 ONLY MODIFY THESE FILES IN THIS FOLDER:                     ║
 * ║     Feature7UI.java       ← Your Swing UI code here              ║
 * ║     Feature7Service.java  ← Your data/logic here                ║
 * ║     README.md               ← Full spec + layout diagram        ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class Feature7UI extends BaseUI implements Feature {

    private final Feature7Service service = new Feature7Service();
    private final Map<String, JPanel> eraAnchors = new LinkedHashMap<>();

    private JList<String> eraList;
    private JScrollPane timelineScroll;
    private JPanel timelinePanel;

    public Feature7UI() {
        super("Historical Timeline");
        buildUI();
    }

    @Override
    public void render() {
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(createHeader("📜  Historical Timeline", "Explore clothing through Indian history."), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(COLOR_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        String[] eraNames = getEraNames();
        eraList = new JList<>(eraNames);
        eraList.setFont(FONT_BODY);
        eraList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eraList.setVisibleRowCount(8);
        eraList.setFixedCellHeight(30);
        eraList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        eraList.setBackground(COLOR_CARD);
        eraList.setForeground(COLOR_TEXT);
        eraList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = eraList.getSelectedValue();
                if (selected != null) {
                    scrollToEra(selected);
                }
            }
        });

        JPanel leftPanel = createCard();
        leftPanel.setLayout(new BorderLayout(6, 6));
        leftPanel.setPreferredSize(new Dimension(220, 100));
        JLabel selectorTitle = new JLabel("Era Selector");
        selectorTitle.setFont(FONT_LABEL);
        leftPanel.add(selectorTitle, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(eraList), BorderLayout.CENTER);

        timelinePanel = new JPanel();
        timelinePanel.setLayout(new BoxLayout(timelinePanel, BoxLayout.Y_AXIS));
        timelinePanel.setBackground(COLOR_BG);

        buildTimelineCards();

        timelineScroll = new JScrollPane(timelinePanel);
        timelineScroll.setBorder(BorderFactory.createEmptyBorder());
        timelineScroll.getViewport().setBackground(COLOR_BG);
        timelineScroll.getVerticalScrollBar().setUnitIncrement(16);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, timelineScroll);
        splitPane.setDividerLocation(240);
        splitPane.setResizeWeight(0);
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(splitPane, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        if (eraNames.length > 0) {
            eraList.setSelectedIndex(0);
        }
    }

    private String[] getEraNames() {
        String[][] eras = service.getEras();
        String[] names = new String[eras.length];
        for (int i = 0; i < eras.length; i++) {
            names[i] = eras[i][0];
        }
        return names;
    }

    private void buildTimelineCards() {
        eraAnchors.clear();
        timelinePanel.removeAll();

        for (String[] era : service.getEras()) {
            String eraName = era[0];
            String period = era[1];
            String description = era[2];
            Color accent = service.getColorForEra(eraName);

            JPanel row = new JPanel(new BorderLayout(0, 0));
            row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

            JPanel timelineLine = new JPanel();
            timelineLine.setPreferredSize(new Dimension(6, 1));
            timelineLine.setBackground(accent);
            row.add(timelineLine, BorderLayout.WEST);

            JPanel card = createCard();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, accent),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));

            JLabel nameLabel = new JLabel(eraName);
            nameLabel.setFont(FONT_LABEL);
            nameLabel.setForeground(accent.darker());
            card.add(nameLabel);

            card.add(Box.createRigidArea(new Dimension(0, 4)));
            JLabel periodLabel = new JLabel(period);
            periodLabel.setFont(FONT_SMALL);
            periodLabel.setForeground(COLOR_TEXT);
            card.add(periodLabel);

            card.add(Box.createRigidArea(new Dimension(0, 6)));
            JTextArea descArea = createTextArea(description);
            descArea.setBackground(COLOR_CARD);
            descArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
            card.add(descArea);

            List<ClothingItem> items = service.getItemsForEra(eraName);
            JLabel itemsTitle = new JLabel("Items:");
            itemsTitle.setFont(FONT_SMALL.deriveFont(Font.BOLD));
            itemsTitle.setForeground(COLOR_TEXT);
            card.add(itemsTitle);
            card.add(Box.createRigidArea(new Dimension(0, 4)));

            if (items.isEmpty()) {
                JLabel noneLabel = new JLabel("No garments mapped to this era yet.");
                noneLabel.setFont(FONT_SMALL);
                noneLabel.setForeground(Color.DARK_GRAY);
                card.add(noneLabel);
            } else {
                for (ClothingItem item : items) {
                    JLabel itemLabel = new JLabel(item.getImageIcon() + " " + item.getName()
                        + "  ·  " + item.getRegion() + "  ·  " + item.getFabricType());
                    itemLabel.setFont(FONT_SMALL);
                    itemLabel.setForeground(COLOR_TEXT);
                    card.add(itemLabel);
                    card.add(Box.createRigidArea(new Dimension(0, 2)));
                }
            }

            row.add(card, BorderLayout.CENTER);
            timelinePanel.add(row);
            eraAnchors.put(eraName, row);
        }

        timelinePanel.add(Box.createVerticalGlue());
        timelinePanel.revalidate();
        timelinePanel.repaint();
    }

    private void scrollToEra(String eraName) {
        JPanel target = eraAnchors.get(eraName);
        if (target == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            Rectangle bounds = target.getBounds();
            timelineScroll.getVerticalScrollBar().setValue(Math.max(bounds.y - 12, 0));
        });
    }

    /** Optional standalone entry point for direct feature testing. */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Feature7UI ui = new Feature7UI();
            ui.render();
        });
    }
}

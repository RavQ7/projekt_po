import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Panel legendy wyświetlający objaśnienie kolorów używanych w symulacji pożaru lasu.
 * Panel ten pokazuje mapowanie między elementami terenu a ich kolorami w interfejsie graficznym.
 */
public class LegendPanel extends JPanel {
    private Map<String, Color> legendItems;
    private int squareSize = 15;
    private int padding = 5;
    private int lineHeight = 20;

    /**
     * Tworzy nowy panel legendy z domyślnymi elementami.
     */
    public LegendPanel() {
        legendItems = new LinkedHashMap<>();
        setPreferredSize(new Dimension(150, 220));
        setBorder(BorderFactory.createTitledBorder("Legenda"));

        // Domyślna inicjalizacja legendy z elementami terenu
        initDefaultLegend();
    }

    /**
     * Inicjalizuje legendę domyślnymi elementami terenu i ich kolorami.
     */
    private void initDefaultLegend() {
        legendItems.put("Sosna", Color.GREEN);
        legendItems.put("Dąb", new Color(0, 100, 0));
        legendItems.put("Trawa", new Color(240, 230, 140));
        legendItems.put("Woda", Color.BLUE);
        legendItems.put("Płonące", new Color(247, 121, 37));
        legendItems.put("Spalone", Color.DARK_GRAY);
        legendItems.put("Puste", Color.WHITE);
    }

    /**
     * Dodaje nowy element do legendy.
     *
     * @param label Etykieta elementu
     * @param color Kolor reprezentujący element
     */
    public void addLegendItem(String label, Color color) {
        legendItems.put(label, color);
        repaint();
    }

    /**
     * Rysuje legendę na panelu.
     *
     * @param g Kontekst graficzny
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = padding + 15;

        for (Map.Entry<String, Color> entry : legendItems.entrySet()) {
            // Rysowanie kolorowego kwadratu
            g.setColor(entry.getValue());
            g.fillRect(padding, y, squareSize, squareSize);

            // Rysowanie obramowania kwadratu
            g.setColor(Color.BLACK);
            g.drawRect(padding, y, squareSize, squareSize);

            // Rysowanie etykiety
            g.drawString(entry.getKey(), padding + squareSize + 5, y + squareSize - 2);

            y += lineHeight;
        }
    }
}
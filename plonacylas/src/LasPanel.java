import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class LasPanel extends JPanel {
    private Las las;
    private int cellSize = 20; // Rozmiar komórki
    private Map<Character, Color> kolorMap = new HashMap<>();

    public LasPanel(Las las) {
        this.las = las;
        setPreferredSize(new Dimension(las.getSzerokosc() * cellSize, las.getWysokosc() * cellSize));
        ustawKolory();
    }

    private void ustawKolory() {
        kolorMap.put('S', Color.GREEN);   // Sosna
        kolorMap.put('D', new Color(0, 100, 0)); // Dąb (ciemnozielony)
        kolorMap.put('.', new Color(240, 230, 140)); // Trawa (Khaki)
        kolorMap.put('~', Color.BLUE);    // Woda
        kolorMap.put('*', Color.RED);     // Ogień
        kolorMap.put('#', Color.DARK_GRAY); // Spalone
        kolorMap.put(' ', Color.WHITE);   // Puste
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < las.getWysokosc(); row++) {
            for (int col = 0; col < las.getSzerokosc(); col++) {
                ElementTerenu element = las.getPole(row, col);
                if (element != null) {
                    g.setColor(kolorMap.getOrDefault(element.getSymbol(), Color.WHITE));
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize); // Poprawione
                }
            }
        }
    }

    public void updateLas(Las newLas) {
        this.las = newLas;
        repaint();
    }
}
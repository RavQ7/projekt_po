import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel odpowiedzialny za graficzną reprezentację lasu w symulacji.
 * Implementuje komponent Swing rysujący las jako siatkę kolorowych komórek,
 * gdzie każdy kolor odpowiada określonemu typowi elementu terenu.
 * Klasa realizuje wzorzec MVC jako część widoku dla modelu Las.
 */
class LasPanel extends JPanel {
    private Las las;                         // Referencja do modelu lasu
    private int cellSize = 20;               // Rozmiar pojedynczej komórki w pikselach
    private Map<Character, Color> kolorMap = new HashMap<>();  // Mapowanie symboli na kolory

    /**
     * Tworzy nowy panel wyświetlający podany las.
     * Inicjalizuje mapę kolorów dla różnych elementów terenu.
     *
     * @param las Obiekt lasu do wyświetlenia
     */
    public LasPanel(Las las) {
        this.las = las;
        setPreferredSize(new Dimension(las.getSzerokosc() * cellSize, las.getWysokosc() * cellSize));
        ustawKolory();
    }

    /**
     * Inicjalizuje mapę kolorów dla symboli wszystkich typów elementów terenu.
     * Każdy element terenu ma przypisany kolor używany przy renderowaniu.
     */
    private void ustawKolory() {
        kolorMap.put('S', Color.GREEN);   // Sosna
        kolorMap.put('D', new Color(0, 100, 0)); // Dąb (ciemnozielony)
        kolorMap.put('.', new Color(240, 230, 140)); // Trawa (Khaki)
        kolorMap.put('~', Color.BLUE);    // Woda
        kolorMap.put('!', Color.RED);     // Ogień
        kolorMap.put('*', new Color(247, 121, 37));   // Płonące (pomarańczowy)
        kolorMap.put('#', Color.DARK_GRAY); // Spalone
        kolorMap.put(' ', Color.WHITE);   // Puste
    }

    /**
     * Metoda odpowiedzialna za renderowanie komponentu.
     * Rysuje siatkę reprezentującą las, gdzie każda komórka ma kolor
     * odpowiadający typowi elementu terenu na danej pozycji.
     *
     * @param g Kontekst graficzny używany do renderowania
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Iteracja przez wszystkie pola w lesie
        for (int row = 0; row < las.getWysokosc(); row++) {
            for (int col = 0; col < las.getSzerokosc(); col++) {
                ElementTerenu element = las.getPole(row, col);
                if (element != null) {
                    // Ustawienie koloru na podstawie symbolu elementu
                    g.setColor(kolorMap.getOrDefault(element.getSymbol(), Color.WHITE));
                    // Wypełnienie prostokąta dla danej komórki
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    /**
     * Aktualizuje referencję do lasu i odświeża widok.
     * Wywoływana po każdym kroku symulacji, aby odzwierciedlić
     * zmiany w modelu danych.
     *
     * @param newLas Zaktualizowany obiekt lasu
     */
    public void updateLas(Las newLas) {
        this.las = newLas;
        repaint();  // Wywołuje ponowne renderowanie komponentu
    }
}
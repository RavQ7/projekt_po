import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Klasa reprezentująca główne okno aplikacji symulacji pożaru lasu.
 * Zarządza interfejsem użytkownika, prezentacją danych i interakcją z symulacją.
 * Implementuje wzorzec MVC jako widok i kontroler dla modelu Las.
 */
class LasFrame extends JFrame {
    private LasPanel lasPanel;          // Panel wyświetlający graficzną reprezentację lasu
    private JButton startButton;        // Przycisk rozpoczynający ciągłą symulację
    private JButton krokButton;         // Przycisk wykonujący pojedynczy krok symulacji
    private JLabel statystykiLabel;     // Etykieta pokazująca statystyki symulacji
    private Timer timer;                // Timer kontrolujący automatyczne wykonywanie kroków symulacji
    private Las las;                    // Referencja do modelu lasu

    /**
     * Tworzy nowe okno aplikacji dla podanego lasu.
     * Inicjalizuje komponenty GUI i konfiguruje ich zachowanie.
     *
     * @param las Obiekt lasu, który będzie wizualizowany i symulowany
     */
    public LasFrame(Las las) {
        this.las = las;
        this.lasPanel = new LasPanel(las);
        this.statystykiLabel = new JLabel("Epoka: 0, Zdrowe: 0, Płonące: 0, Spalone: 0");

        setTitle("Symulacja Pożaru Lasu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(lasPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        krokButton = new JButton("Krok");

        controlPanel.add(startButton);
        controlPanel.add(krokButton);
        controlPanel.add(statystykiLabel);

        add(controlPanel, BorderLayout.SOUTH);

        // Inicjalizacja timera z interwałem 500ms między krokami symulacji
        timer = new Timer(500, e -> wykonajKrokSymulacji());

        // Konfiguracja obsługi przycisków
        startButton.addActionListener(e -> timer.start());
        krokButton.addActionListener(e -> wykonajKrokSymulacji());

        pack();
        setLocationRelativeTo(null);  // Centrowanie okna na ekranie
        setVisible(true);
    }

    /**
     * Wykonuje pojedynczy krok symulacji, aktualizuje widok i sprawdza warunki zakończenia.
     * Metoda jest wywoływana zarówno przez timer, jak i przez przycisk "Krok".
     */
    private void wykonajKrokSymulacji() {
        las.symulujKrok();            // Aktualizacja modelu
        lasPanel.updateLas(las);      // Aktualizacja widoku
        aktualizujStatystyki();       // Aktualizacja danych statystycznych

        // Sprawdzenie warunku zakończenia symulacji
        if (!las.czyPozarAktywny()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Pożar ugaszony!");
        }
    }

    /**
     * Aktualizuje etykietę statystyk o aktualne dane z modelu lasu.
     * Pokazuje informacje o epoce i liczbie drzew w różnych stanach.
     */
    private void aktualizujStatystyki() {
        List<Integer> statystyki = las.zliczStany();
        statystykiLabel.setText(String.format("Epoka: %d, Zdrowe: %d, Płonące: %d, Spalone: %d",
                las.getEpoka(), statystyki.get(0), statystyki.get(1), statystyki.get(2)));
    }

    /**
     * Punkt wejścia do aplikacji z interfejsem graficznym.
     * Tworzy nowy las i okno symulacji.
     *
     * @param args Argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Las las = new Las(20, 20);
            new LasFrame(las);
        });
    }
}
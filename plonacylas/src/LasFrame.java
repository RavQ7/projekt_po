import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Zmodyfikowana klasa LasFrame z dodaną legendą kolorów.
 * Rozszerza pierwotną implementację o panel legendy wyjaśniający znaczenie poszczególnych kolorów w symulacji.
 */
class ModifiedLasFrame extends JFrame {
    private LasPanel lasPanel;          // Panel wyświetlający graficzną reprezentację lasu
    private LegendPanel legendPanel;    // Nowy panel legendy kolorów
    private JButton startButton;        // Przycisk rozpoczynający ciągłą symulację
    private JButton krokButton;         // Przycisk wykonujący pojedynczy krok symulacji
    private JLabel statystykiLabel;     // Etykieta pokazująca statystyki symulacji
    private JLabel wiatrLabel;          // Etykieta pokazująca informacje o kierunku i sile wiatru
    private Timer timer;                // Timer kontrolujący automatyczne wykonywanie kroków symulacji
    private Las las;                    // Referencja do modelu lasu

    /**
     * Tworzy nowe okno aplikacji dla podanego lasu.
     * Inicjalizuje komponenty GUI i konfiguruje ich zachowanie.
     *
     * @param las Obiekt lasu, który będzie wizualizowany i symulowany
     */
    public ModifiedLasFrame(Las las) {
        this.las = las;
        this.lasPanel = new LasPanel(las);
        this.legendPanel = new LegendPanel();
        this.statystykiLabel = new JLabel("Epoka: 0, Zdrowe: 0, Płonące: 0, Spalone: 0");

        // Inicjalizacja etykiety wiatru
        int[] wiatrWektor = las.getWiatr().getWektor();
        int wiatrSila = las.getWiatr().getSila();
        this.wiatrLabel = new JLabel(formatujInfoWiatru(wiatrWektor, wiatrSila));

        setTitle("Symulacja Pożaru Lasu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel dla wizualizacji lasu i legendy
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(lasPanel, BorderLayout.CENTER);
        mainPanel.add(legendPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        // Panel kontrolny na dole
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startButton = new JButton("Start");
        krokButton = new JButton("Krok");
        buttonsPanel.add(startButton);
        buttonsPanel.add(krokButton);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(statystykiLabel);
        infoPanel.add(Box.createHorizontalStrut(20)); // Separator
        infoPanel.add(wiatrLabel);

        controlPanel.add(buttonsPanel);
        controlPanel.add(infoPanel);

        add(controlPanel, BorderLayout.SOUTH);

        // Inicjalizacja timera z interwałem 500ms między krokami symulacji
        timer = new Timer(500, e -> wykonajKrokSymulacji());

        // Konfiguracja obsługi przycisków
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    startButton.setText("Start");
                } else {
                    timer.start();
                    startButton.setText("Stop");
                }
            }
        });

        krokButton.addActionListener(e -> wykonajKrokSymulacji());

        pack();
        setLocationRelativeTo(null);  // Centrowanie okna na ekranie
        setVisible(true);
    }

    /**
     * Formatuje informację o wietrze jako tekst.
     *
     * @param wiatrWektor Wektor kierunku wiatru [dr, dc]
     * @param wiatrSila Siła wiatru
     * @return Sformatowany tekst z informacją o wietrze
     */
    private String formatujInfoWiatru(int[] wiatrWektor, int wiatrSila) {
        String kierunek = "brak";
        if (wiatrWektor[0] < 0) {
            if (wiatrWektor[1] < 0) kierunek = "NW";
            else if (wiatrWektor[1] > 0) kierunek = "NE";
            else kierunek = "N";
        } else if (wiatrWektor[0] > 0) {
            if (wiatrWektor[1] < 0) kierunek = "SW";
            else if (wiatrWektor[1] > 0) kierunek = "SE";
            else kierunek = "S";
        } else {
            if (wiatrWektor[1] < 0) kierunek = "W";
            else if (wiatrWektor[1] > 0) kierunek = "E";
        }

        return String.format("Wiatr: kierunek %s, siła %d/5", kierunek, wiatrSila);
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
            startButton.setText("Start");
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

            // Konfiguracja wiatru dla lepszej demonstracji jego wpływu
            las.getWiatr().ustawKierunek(0, 1); // Wiatr ze wschodu
            las.getWiatr().ustawSile(3);

            new ModifiedLasFrame(las);
        });
    }
}
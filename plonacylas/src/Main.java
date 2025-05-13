import javax.swing.SwingUtilities;

/**
 * Główna klasa programu symulacji pożaru lasu.
 * Stanowi punkt wejścia do aplikacji i inicjalizuje interfejs graficzny.
 */
public class Main {
    /**
     * Metoda główna uruchamiająca aplikację symulacji pożaru lasu.
     * Tworzy instancję lasu i główne okno aplikacji w bezpieczny dla
     * wątków sposób przy użyciu SwingUtilities.invokeLater().
     *
     * @param args Argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {
        // Uruchomienie GUI w wątku dispatching thread Swinga
        SwingUtilities.invokeLater(() -> {
            // Utworzenie nowego lasu o wymiarach 20x20
            Las las = new Las(20, 20);
            // Utworzenie i wyświetlenie głównego okna aplikacji
            new LasFrame(las);
        });
    }
}
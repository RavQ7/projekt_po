import javax.swing.SwingUtilities;

/**
 * Zmodyfikowana główna klasa programu symulacji pożaru lasu.
 * Stanowi punkt wejścia do aplikacji i inicjalizuje interfejs graficzny z legendą kolorów.
 */
public class Main {
    /**
     * Metoda główna uruchamiająca zaktualizowaną aplikację symulacji pożaru lasu.
     * Tworzy instancję lasu i główne okno aplikacji ze zmodyfikowanym interfejsem
     * zawierającym legendę kolorów.
     *
     * @param args Argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {
        // Uruchomienie GUI w wątku dispatching thread Swinga
        SwingUtilities.invokeLater(() -> {
            // Utworzenie nowego lasu o wymiarach 20x20
            Las las = new Las(20, 20);

            // Konfiguracja wiatru do demonstracji jego wpływu
            las.getWiatr().ustawKierunek(0, 1); // Wiatr ze wschodu
            las.getWiatr().ustawSile(3);

            // Utworzenie i wyświetlenie głównego okna aplikacji z legendą
            new ModifiedLasFrame(las);
        });
    }
}
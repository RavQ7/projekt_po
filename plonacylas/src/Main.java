import javax.swing.SwingUtilities;
import java.util.Random;

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
            //Losowanie wartości dla wiatru
            Random rand = new Random();
            int losowyKierunek = -1 + rand.nextInt() * (1 + 1);
            int losowaSila = rand.nextInt(6);
            // Konfiguracja wiatru do demonstracji jego wpływu
            las.getWiatr().ustawKierunek(losowyKierunek, losowyKierunek); // Wiatr ze wschodu
            las.getWiatr().ustawSile(losowaSila);

            // Utworzenie i wyświetlenie głównego okna aplikacji z legendą
            new ModifiedLasFrame(las);
        });
    }
}
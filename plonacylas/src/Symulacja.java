import java.io.IOException;
import java.util.Random;

/**
 * Klasa demonstracyjna dla tekstowego trybu symulacji pożaru lasu.
 * Inicjalizuje las, uruchamia symulację krok po kroku i wyświetla
 * wyniki w konsoli bez interfejsu graficznego.
 */
public class Symulacja {
    /**
     * Główna metoda uruchamiająca symulację w trybie tekstowym.
     * Tworzy las, ustawia parametry wiatru i wykonuje kroki symulacji,
     * dopóki pożar jest aktywny.
     *
     * @param args Argumenty wiersza poleceń (nieużywane)
     * @throws InterruptedException jeśli wątek zostanie przerwany podczas usypiania
     */
    public static void main(String[] args) throws InterruptedException {
        // Inicjalizacja lasu o rozmiarze 20x40
        Las las = new Las(20, 40);

        // Konfiguracja wiatru
        Random rand = new Random();
        int losowyKierunek = -1 + rand.nextInt() * (1 + 1);
        int losowaSila = rand.nextInt(6);
        // Konfiguracja wiatru do demonstracji jego wpływu
        las.getWiatr().ustawKierunek(losowyKierunek, losowyKierunek); // Wiatr ze wschodu
        las.getWiatr().ustawSile(losowaSila);

        int epoka = 0;
        // Główna pętla symulacji
        while (las.czyPozarAktywny()) {
            System.out.println("Epoka: " + epoka);
            las.wyswietlLas();  // Wyświetlenie aktualnego stanu lasu w konsoli

            // Próba zapisu stanu do pliku CSV
            try {
                las.zapiszStanDoCSV(epoka);
            } catch (Exception e) {
                System.err.println("Błąd zapisu: " + e.getMessage());
            }

            // Wykonanie kolejnego kroku symulacji
            las.symulujKrok();
            epoka++;

            // Opóźnienie dla lepszej czytelności w konsoli
            Thread.sleep(500);
        }

        // Wyświetlenie raportu końcowego po zakończeniu symulacji
        System.out.println("\n=== RAPORT KOŃCOWY ===");
        System.out.printf("Procent spalonych drzew: %.2f%%\n", las.procentSpalonegoLasu());
        System.out.println("Czas trwania pożaru: " + epoka + " epok");
    }
}
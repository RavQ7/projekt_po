import java.io.IOException;

public class Symulacja {
    public static void main(String[] args) throws InterruptedException {
        Las las = new Las(20, 40);
        las.getWiatr().ustawKierunek(0, 1); // Wiatr ze wschodu
        las.getWiatr().ustawSile(3);

        int epoka = 0;
        while (las.czyPożarAktywny()) {
            System.out.println("Epoka: " + epoka);
            las.wyswietlLas();
            try {
                las.zapiszStanDoCSV(epoka);
            } catch (Exception e) {
                System.err.println("Błąd zapisu: " + e.getMessage());
            }
            las.symulujKrok();
            epoka++;
            Thread.sleep(500);
        }

        System.out.println("\n=== RAPORT KOŃCOWY ===");
        System.out.printf("Procent spalonych drzew: %.2f%%\n", las.procentSpalonegoLasu());
        System.out.println("Czas trwania pożaru: " + epoka + " epok");
    }
}

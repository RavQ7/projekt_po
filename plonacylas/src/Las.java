import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reprezentuje główny obszar symulacji pożaru lasu.
 * Zawiera tablicę wszystkich elementów terenu i zarządza symulacją.
 */
public class Las {
    private static final Logger LOGGER = Logger.getLogger(Las.class.getName());

    private ElementTerenu[][] pola;
    private int wysokosc;
    private int szerokosc;
    private final Wiatr wiatr = new Wiatr();
    private Random rand = new Random();
    private int epoka;

    /**
     * Tworzy nowy las o podanych wymiarach.
     *
     * @param wysokosc Liczba wierszy w lesie
     * @param szerokosc Liczba kolumn w lesie
     */
    public Las(int wysokosc, int szerokosc) {
        this.wysokosc = wysokosc;
        this.szerokosc = szerokosc;
        this.pola = new ElementTerenu[getWysokosc()][getSzerokosc()];
        this.epoka = 0;

        // Inicjalizacja pliku CSV
        try (FileWriter csvWriter = new FileWriter("symulacja.csv")) {
            csvWriter.append("Epoka,Zdrowe,Plonace,Spalone\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Błąd tworzenia pliku CSV", e);
        }

        inicjalizujLas();
    }

    /**
     * Inicjalizuje las losowymi elementami terenu i ustawia początkowy ogień.
     */
    private void inicjalizujLas() {
        for (int i = 0; i < getWysokosc(); i++) {
            for (int j = 0; j < getSzerokosc(); j++) {
                double los = rand.nextDouble();
                if (los < 0.35) pola[i][j] = new Sosna();
                else if (los < 0.7) pola[i][j] = new Dab();
                else if (los < 0.85) pola[i][j] = new Trawa();
                else pola[i][j] = new Woda();
            }
        }

        // Początkowy ogień
        int startRow = rand.nextInt(getWysokosc());
        int startCol = rand.nextInt(getSzerokosc());
        while (!(pola[startRow][startCol] instanceof Drzewo)) {
            startRow = rand.nextInt(getWysokosc());
            startCol = rand.nextInt(getSzerokosc());
        }

        if (pola[startRow][startCol] instanceof Drzewo) {
            ((Drzewo) pola[startRow][startCol]).setStan(Drzewo.StanDrzewa.PLONACE);
        }
    }

    /**
     * Wykonuje jeden krok symulacji.
     */
    public void symulujKrok() {
        try {
            ElementTerenu[][] nowaPlansza = new ElementTerenu[getWysokosc()][getSzerokosc()];

            for (int i = 0; i < getWysokosc(); i++) {
                for (int j = 0; j < getSzerokosc(); j++) {
                    if (pola[i][j] != null) {
                        ElementTerenu kopia = pola[i][j].stworzKopie();
                        if (kopia != null) {
                            kopia.nextStep(this, i, j);
                            nowaPlansza[i][j] = kopia;
                        } else {
                            // Awaryjne tworzenie pustego pola jeśli kopię się nie udało
                            LOGGER.warning("Nie udało się skopiować elementu na pozycji [" + i + "][" + j + "]");
                            nowaPlansza[i][j] = new Puste();
                        }
                    }
                }
            }
            pola = nowaPlansza;
            epoka++;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Błąd podczas symulacji kroku: " + e.getMessage(), e);
        }
    }

    /**
     * Zapisuje aktualny stan lasu do pliku CSV.
     * Korzysta z try-with-resources dla automatycznego zamykania zasobów.
     *
     * @param epoka Numer bieżącej epoki
     */
    public void zapiszStanDoCSV(int epoka) {
        try (FileWriter writer = new FileWriter("symulacja.csv", true)) {
            List<Integer> stany = zliczStany();
            writer.append(String.format("%d,%d,%d,%d\n",
                    epoka, stany.get(0), stany.get(1), stany.get(2)));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Błąd zapisu do CSV", e);
        }
    }

    /**
     * Zlicza liczbę drzew w poszczególnych stanach.
     *
     * @return Lista zawierająca kolejno liczbę drzew: zdrowych, płonących, spalonych
     */
    public List<Integer> zliczStany() {
        int zdrowe = 0, plonace = 0, spalone = 0;
        for (int i = 0; i < getWysokosc(); i++) {
            for (int j = 0; j < getSzerokosc(); j++) {
                if (pola[i][j] instanceof Drzewo) {
                    Drzewo.StanDrzewa stan = ((Drzewo) pola[i][j]).getStan();
                    if (stan == Drzewo.StanDrzewa.ZDROWE) zdrowe++;
                    else if (stan == Drzewo.StanDrzewa.PLONACE) plonace++;
                    else spalone++;
                }
            }
        }
        return List.of(zdrowe, plonace, spalone);
    }

    /**
     * Sprawdza, czy w lesie jest aktywny pożar.
     *
     * @return true jeśli w lesie są płonące drzewa lub ogień, false w przeciwnym przypadku
     */
    public boolean czyPozarAktywny() {
        for (int i = 0; i < getWysokosc(); i++) {
            for (int j = 0; j < getSzerokosc(); j++) {
                if (pola[i][j] instanceof Ogien ||
                        (pola[i][j] instanceof Drzewo &&
                                ((Drzewo) pola[i][j]).getStan() == Drzewo.StanDrzewa.PLONACE)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Oblicza procent spalonych drzew względem wszystkich drzew.
     *
     * @return Wartość procentowa spalonych drzew
     */
    public double procentSpalonegoLasu() {
        List<Integer> stany = zliczStany();
        int wszystkieDrzewa = stany.get(0) + stany.get(1) + stany.get(2);
        return wszystkieDrzewa > 0 ?
                (stany.get(2) * 100.0) / wszystkieDrzewa : 0;
    }

    /**
     * Zwraca obiekt wiatru dla symulacji.
     *
     * @return Referencja do obiektu wiatru
     */
    public Wiatr getWiatr() { return wiatr; }

    /**
     * Zwraca wysokość lasu.
     *
     * @return Liczba wierszy
     */
    public int getWysokosc() { return wysokosc; }

    /**
     * Zwraca szerokość lasu.
     *
     * @return Liczba kolumn
     */
    public int getSzerokosc() { return szerokosc; }

    /**
     * Zwraca element na podanej pozycji.
     *
     * @param row Numer wiersza
     * @param col Numer kolumny
     * @return Element znajdujący się na podanej pozycji
     */
    public ElementTerenu getPole(int row, int col) { return pola[row][col]; }

    /**
     * Ustawia element na podanej pozycji.
     *
     * @param row Numer wiersza
     * @param col Numer kolumny
     * @param element Element do ustawienia
     */
    public void setPole(int row, int col, ElementTerenu element) { pola[row][col] = element; }

    /**
     * Zwraca numer aktualnej epoki symulacji.
     *
     * @return Numer epoki
     */
    public int getEpoka() { return epoka; }

    /**
     * Wyświetla stan lasu w trybie tekstowym.
     */
    public void wyswietlLas() {
        for (int i = 0; i < getWysokosc(); i++) {
            for (int j = 0; j < getSzerokosc(); j++) {
                System.out.print(pola[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
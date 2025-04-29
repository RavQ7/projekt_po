import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Klasa abstrakcyjna reprezentująca element terenu
abstract class ElementTerenu {
    protected char symbol;

    public ElementTerenu(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    // Metoda abstrakcyjna określająca, czy element jest palny
    public abstract boolean isPalny();

    // Metoda abstrakcyjna określająca, czy ogień może się rozprzestrzenić na ten element
    public abstract boolean canBeIgnited();

    // Metoda abstrakcyjna do wykonania akcji w kroku symulacji (jeśli dotyczy)
    public void nextStep(Las las, int row, int col) {
        // Domyślna implementacja nie robi nic
    }
}

// Klasa reprezentująca puste pole lub trawę
class Puste extends ElementTerenu {
    private double podatnoscNaOgien = 0.1; // Prawdopodobieństwo zapłonu od sąsiada

    public Puste() {
        super('.');
    }

    @Override
    public boolean isPalny() {
        return true;
    }

    @Override
    public boolean canBeIgnited() {
        return true;
    }

    public double getPodatnoscNaOgien() {
        return podatnoscNaOgien;
    }
}

// Klasa abstrakcyjna reprezentująca drzewo
abstract class Drzewo extends ElementTerenu {
    public enum StanDrzewa {
        ZDROWE,
        PLONACE,
        SPALONE
    }

    protected StanDrzewa stan;
    protected int czasPalenia;
    protected int aktualnyCzasPalenia = 0;

    public Drzewo(char symbol, int czasPalenia) {
        super(symbol);
        this.stan = StanDrzewa.ZDROWE;
        this.czasPalenia = czasPalenia;
    }

    public StanDrzewa getStan() {
        return stan;
    }

    public void setStan(StanDrzewa stan) {
        this.stan = stan;
    }

    public int getCzasPalenia() {
        return czasPalenia;
    }

    @Override
    public boolean isPalny() {
        return true;
    }

    @Override
    public boolean canBeIgnited() {
        return stan == StanDrzewa.ZDROWE;
    }

    @Override
    public void nextStep(Las las, int row, int col) {
        if (stan == StanDrzewa.PLONACE) {
            aktualnyCzasPalenia++;
            if (aktualnyCzasPalenia >= czasPalenia) {
                stan = StanDrzewa.SPALONE;
                symbol = '#';
            }
            // Rozprzestrzenianie ognia do sąsiadów
            rozprzestrzenOgien(las, row, col);
        }
    }

    protected void rozprzestrzenOgien(Las las, int row, int col) {
        int rows = las.getWysokosc();
        int cols = las.getSzerokosc();
        Random random = new Random();

        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                ElementTerenu sasiad = las.getPole(newRow, newCol);
                if (sasiad != null && sasiad.canBeIgnited()) {
                    // Prawdopodobieństwo zapłonu (może zależeć od typu drzewa, wiatru itp.)
                    double prawdopodobienstwoZaplonu = 0.3;
                    if (random.nextDouble() < prawdopodobienstwoZaplonu) {
                        if (sasiad instanceof Drzewo) {
                            ((Drzewo) sasiad).setStan(StanDrzewa.PLONACE);
                            sasiad.symbol = '*';
                        } else if (sasiad instanceof Puste) {
                            // Ogień może przejść przez puste pole, ale niekoniecznie je "pali" na stałe
                            // Można dodać logikę, np. tymczasowy stan płonący dla pustego pola
                        }
                    }
                }
            }
        }
    }
}

// Klasa reprezentująca konkretny typ drzewa - Sosna
class Sosna extends Drzewo {
    public Sosna() {
        super('S', 5); // Sosna pali się przez 5 epok (przykładowa wartość)
    }
}

// Klasa reprezentująca konkretny typ drzewa - Dąb
class Dab extends Drzewo {
    public Dab() {
        super('D', 8); // Dąb pali się przez 8 epok (przykładowa wartość)
    }
}

// Klasa reprezentująca wodę lub rzekę
class Woda extends ElementTerenu {
    public Woda() {
        super('~');
    }

    @Override
    public boolean isPalny() {
        return false;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }
}

// Klasa reprezentująca ogień
class Ogien extends ElementTerenu {
    private int czasTrwania;
    public int aktualnyCzasTrwania = 0;

    public Ogien(int czasTrwania) {
        super('*');
        this.czasTrwania = czasTrwania;
    }
    public int getCzasTrwania() {
        return czasTrwania;
    }
    @Override
    public boolean isPalny() {
        return false;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }

    @Override
    public void nextStep(Las las, int row, int col) {
        aktualnyCzasTrwania++;
        if (aktualnyCzasTrwania >= czasTrwania) {
            // Ogień gaśnie, pole wraca do poprzedniego stanu (trzeba zapamiętać poprzedni stan)
            // W tym uproszczeniu, po prostu staje się puste
            las.setPole(row, col, new Puste());
        } else {
            // Rozprzestrzenianie ognia (podobnie jak w Drzewo)
            rozprzestrzenOgien(las, row, col);
        }
    }

    private void rozprzestrzenOgien(Las las, int row, int col) {
        int rows = las.getWysokosc();
        int cols = las.getSzerokosc();
        Random random = new Random();

        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                ElementTerenu sasiad = las.getPole(newRow, newCol);
                if (sasiad != null && sasiad.canBeIgnited()) {
                    double prawdopodobienstwoZaplonu = 0.4; // Inne prawdopodobieństwo dla ognia
                    if (random.nextDouble() < prawdopodobienstwoZaplonu) {
                        if (sasiad instanceof Drzewo) {
                            ((Drzewo) sasiad).setStan(Drzewo.StanDrzewa.PLONACE);
                            sasiad.symbol = '*';
                        } else if (sasiad instanceof Puste) {
                            las.setPole(newRow, newCol, new Ogien(3)); // Nowy ogień na pustym polu
                        }
                    }
                }
            }
        }
    }
}

// Klasa reprezentująca wiatr (jeśli zostanie zaimplementowany wpływ)
class Wiatr {
    public enum KierunekWiatru {
        POLNOC,
        POLUDNIE,
        WSCHOD,
        ZACHOD
    }

    private KierunekWiatru kierunek;
    private double sila;

    public Wiatr(KierunekWiatru kierunek, double sila) {
        this.kierunek = kierunek;
        this.sila = sila;
    }

    public KierunekWiatru getKierunek() {
        return kierunek;
    }

    public double getSila() {
        return sila;
    }

    // Metody do modyfikacji kierunku i siły wiatru
    public void zmienKierunek(KierunekWiatru nowyKierunek) {
        this.kierunek = nowyKierunek;
    }

    public void zmienSila(double nowaSila) {
        this.sila = nowaSila;
    }
}

// Klasa reprezentująca planszę lasu
class Las {
    private ElementTerenu[][] pola;
    private int wysokosc;
    private int szerokosc;

    public Las(int wysokosc, int szerokosc) {
        this.wysokosc = wysokosc;
        this.szerokosc = szerokosc;
        this.pola = new ElementTerenu[wysokosc][szerokosc];
        // Inicjalizacja planszy (np. losowe rozmieszczenie elementów)
        inicjalizujLas();
    }

    public int getWysokosc() {
        return wysokosc;
    }

    public int getSzerokosc() {
        return szerokosc;
    }

    public ElementTerenu getPole(int row, int col) {
        if (row >= 0 && row < wysokosc && col >= 0 && col < szerokosc) {
            return pola[row][col];
        }
        return null;
    }

    public void setPole(int row, int col, ElementTerenu element) {
        if (row >= 0 && row < wysokosc && col >= 0 && col < szerokosc) {
            this.pola[row][col] = element;
        }
    }

    public void inicjalizujLas() {
        Random random = new Random();
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                double losowa = random.nextDouble();
                if (losowa < 0.7) {
                    pola[i][j] = new Sosna(); // 70% szans na sosnę
                } else if (losowa < 0.85) {
                    pola[i][j] = new Puste(); // 15% szans na puste pole
                } else {
                    pola[i][j] = new Woda();   // 15% szans na wodę
                }
            }
        }
        // Dodanie początkowego ognia (przykładowo w losowym miejscu)
        int startRow = random.nextInt(wysokosc);
        int startCol = random.nextInt(szerokosc);
        if (pola[startRow][startCol] instanceof Drzewo) {
            ((Drzewo) pola[startRow][startCol]).setStan(Drzewo.StanDrzewa.PLONACE);
            pola[startRow][startCol].symbol = '*';
        } else {
            // Jeśli wylosowano wodę lub puste, można spróbować innego miejsca
            for (int i = 0; i < wysokosc; i++) {
                for (int j = 0; j < szerokosc; j++) {
                    if (pola[i][j] instanceof Drzewo) {
                        ((Drzewo) pola[i][j]).setStan(Drzewo.StanDrzewa.PLONACE);
                        pola[i][j].symbol = '*';
                        return;
                    }
                }
            }
        }
    }

    public void wyswietlLas() {
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                System.out.print(pola[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void symulujKrok() {
        ElementTerenu[][] nastepnePola = new ElementTerenu[wysokosc][szerokosc];
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] != null) {
                    // Tworzymy nowy obiekt, aby uniknąć modyfikacji w trakcie iteracji
                    if (pola[i][j] instanceof Drzewo) {
                        Drzewo stareDrzewo = (Drzewo) pola[i][j];
                        Drzewo noweDrzewo = null;
                        if (stareDrzewo instanceof Sosna) {
                            noweDrzewo = new Sosna();
                        } else if (stareDrzewo instanceof Dab) {
                            noweDrzewo = new Dab();
                        }
                        if (noweDrzewo != null) {
                            noweDrzewo.stan = stareDrzewo.stan;
                            noweDrzewo.aktualnyCzasPalenia = stareDrzewo.aktualnyCzasPalenia;
                            noweDrzewo.symbol = stareDrzewo.symbol;
                            noweDrzewo.nextStep(this, i, j);
                            nastepnePola[i][j] = noweDrzewo;
                        } else {
                            nastepnePola[i][j] = pola[i][j]; // Jeśli nie Drzewo, to bez zmian
                        }
                    } else if (pola[i][j] instanceof Ogien) {
                        Ogien staryOgien = (Ogien) pola[i][j];
                        Ogien nowyOgien = new Ogien(staryOgien.getCzasTrwania());
                        nowyOgien.aktualnyCzasTrwania = staryOgien.aktualnyCzasTrwania;
                        nowyOgien.nextStep(this, i, j);
                        nastepnePola[i][j] = nowyOgien;
                    } else {
                        nastepnePola[i][j] = pola[i][j];
                    }
                }
            }
        }
        this.pola = nastepnePola;
    }

    public List<Integer> zliczStany() {
        int zdrowe = 0;
        int plonace = 0;
        int spalone = 0;
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo) {
                    Drzewo.StanDrzewa stan = ((Drzewo) pola[i][j]).getStan();
                    if (stan == Drzewo.StanDrzewa.ZDROWE) {
                        zdrowe++;
                    } else if (stan == Drzewo.StanDrzewa.PLONACE) {
                        plonace++;
                    } else if (stan == Drzewo.StanDrzewa.SPALONE) {
                        spalone++;
                    }
                } else if (pola[i][j] instanceof Ogien) {
                    plonace++; // Traktujemy ogień jako płonące
                }
            }
        }
        List<Integer> counts = new ArrayList<>();
        counts.add(zdrowe);
        counts.add(plonace);
        counts.add(spalone);
        return counts;
    }

    public boolean czyPożarAktywny() {
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo && ((Drzewo) pola[i][j]).getStan() == Drzewo.StanDrzewa.PLONACE) {
                    return true;
                }
                if (pola[i][j] instanceof Ogien) {
                    return true;
                }
            }
        }
        return false;
    }

    public double procentSpalonegoLasu() {
        int spalone = 0;
        int wszystkieDrzewa = 0;
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo) {
                    wszystkieDrzewa++;
                    if (((Drzewo) pola[i][j]).getStan() == Drzewo.StanDrzewa.SPALONE) {
                        spalone++;
                    }
                }
            }
        }
        return 0;
    }
}
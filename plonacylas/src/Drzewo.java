import java.util.Random;

/**
 * Abstrakcyjna klasa reprezentująca drzewo w symulacji.
 * Definiuje wspólne cechy i zachowania dla różnych gatunków drzew.
 */
public abstract class Drzewo extends ElementTerenu {
    /**
     * Reprezentuje możliwe stany drzewa w trakcie symulacji pożaru.
     */
    public enum StanDrzewa {
        /**
         * Drzewo nienaruszone przez ogień
         */
        ZDROWE,
        /**
         * Drzewo aktualnie się palące
         */
        PLONACE,
        /**
         * Drzewo zniszczone przez ogień
         */
        SPALONE
    }

    protected StanDrzewa stan;
    protected int czasPalenia;
    protected int aktualnyCzasPalenia;
    protected double palnosc;
    private static final Random rand = new Random();

    /**
     * Konstruktor klasy bazowej dla drzew.
     *
     * @param symbol      Znak reprezentujący drzewo na mapie
     * @param czasPalenia Liczba kroków przez jakie drzewo będzie się palić
     * @param palnosc     Współczynnik łatwopalności drzewa (0-1)
     */
    public Drzewo(char symbol, int czasPalenia, double palnosc) {
        super(symbol);
        this.stan = StanDrzewa.ZDROWE;
        this.czasPalenia = czasPalenia;
        this.palnosc = palnosc;
        this.aktualnyCzasPalenia = 0;
    }

    /**
     * Zwraca aktualny stan drzewa.
     *
     * @return Stan drzewa (ZDROWE, PLONACE, SPALONE)
     */
    public StanDrzewa getStan() {
        return stan;
    }

    /**
     * Ustawia stan drzewa.
     *
     * @param stan Nowy stan drzewa
     */
    public void setStan(StanDrzewa stan) {
        this.stan = stan;
        // Aktualizacja symbolu na podstawie stanu
        updateSymbolBasedOnState();
    }

    /**
     * Aktualizuje symbol drzewa na podstawie jego stanu.
     */
    protected void updateSymbolBasedOnState() {
        switch (stan) {
            case ZDROWE:
                // Pozostawia oryginalny symbol gatunku drzewa
                break;
            case PLONACE:
                super.symbol = '*';
                break;
            case SPALONE:
                super.symbol = '#';
                break;
        }
    }

    /**
     * Zwraca aktualny czas palenia drzewa.
     *
     * @return Liczba kroków, przez które drzewo pali się
     */
    public int getAktualnyCzasPalenia() {
        return aktualnyCzasPalenia;
    }

    /**
     * Ustawia aktualny czas palenia drzewa.
     *
     * @param aktualnyCzasPalenia Liczba kroków, przez które drzewo pali się
     */
    public void setAktualnyCzasPalenia(int aktualnyCzasPalenia) {
        this.aktualnyCzasPalenia = aktualnyCzasPalenia;
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
                updateSymbolBasedOnState();
            }
            rozprzestrzenOgien(las, row, col);
        }
    }

    /**
     * Rozszerza ogień na sąsiednie pola.
     *
     * @param las Referencja do lasu
     * @param row Wiersz drzewa
     * @param col Kolumna drzewa
     */
    protected void rozprzestrzenOgien(Las las, int row, int col) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        Wiatr wiatr = las.getWiatr();
        int[] wiatrVector = wiatr.getWektor();
        int wiatrSila = wiatr.getSila();

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (newRow >= 0 && newRow < las.getWysokosc() &&
                    newCol >= 0 && newCol < las.getSzerokosc()) {

                ElementTerenu sasiad = las.getPole(newRow, newCol);
                if (sasiad != null && sasiad.canBeIgnited()) {
                    double prawdopodobienstwo = this.palnosc;

                    // Wpływ wiatru
                    if (wiatrSila > 0) {
                        double zgodnosc = (dc[i] * wiatrVector[1] + dr[i] * wiatrVector[0]) / 2.0;
                        prawdopodobienstwo += zgodnosc * 0.15 * wiatrSila;
                        prawdopodobienstwo = Math.max(0, Math.min(1, prawdopodobienstwo));
                    }

                    if (rand.nextDouble() < prawdopodobienstwo) {
                        if (sasiad instanceof Drzewo) {
                            ((Drzewo) sasiad).setStan(StanDrzewa.PLONACE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ElementTerenu stworzKopie() {
        try {
            Drzewo kopia = (Drzewo) this.getClass().getDeclaredConstructor().newInstance();
            kopia.setStan(this.stan);
            kopia.setAktualnyCzasPalenia(this.aktualnyCzasPalenia);
            return kopia;
        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia kopii drzewa: " + e.getMessage());
            return null;
        }
    }
}
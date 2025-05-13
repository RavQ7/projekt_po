import java.util.Random;

/**
 * Reprezentuje ogień w symulacji, który nie jest związany z żadnym palącym się obiektem.
 * Ogień ma określony czas trwania i może się rozprzestrzeniać.
 */
class Ogien extends ElementTerenu {
    private int czasTrwania;
    private int aktualnyCzasTrwania = 0;

    /**
     * Tworzy nowy ogień z określonym czasem trwania.
     *
     * @param czasTrwania Liczba kroków przez jakie ogień będzie się utrzymywał
     */
    public Ogien(int czasTrwania) {
        super('*');
        this.czasTrwania = czasTrwania;
    }

    /**
     * Zwraca całkowity czas trwania ognia.
     *
     * @return Liczba kroków symulacji, przez które ogień może się utrzymać
     */
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
            // Ogień gaśnie, pole staje się puste
            las.setPole(row, col, new Puste());
        } else {
            // Rozprzestrzenianie ognia
            rozprzestrzenOgien(las, row, col);
        }
    }

    /**
     * Rozszerza ogień na sąsiednie pola.
     *
     * @param las Referencja do lasu
     * @param row Wiersz ognia
     * @param col Kolumna ognia
     */
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
                    double prawdopodobienstwoZaplonu = 0.4; // Prawdopodobieństwo dla ognia
                    if (random.nextDouble() < prawdopodobienstwoZaplonu) {
                        if (sasiad instanceof Drzewo) {
                            ((Drzewo) sasiad).setStan(Drzewo.StanDrzewa.PLONACE);
                        } else if (sasiad instanceof Trawa) {
                            ((Trawa) sasiad).podpal();
                        } else if (sasiad instanceof Puste) {
                            las.setPole(newRow, newCol, new Ogien(3)); // Nowy ogień na pustym polu
                        }
                    }
                }
            }
        }
    }

    @Override
    public ElementTerenu stworzKopie() {
        Ogien kopia = new Ogien(this.czasTrwania);
        kopia.aktualnyCzasTrwania = this.aktualnyCzasTrwania;
        return kopia;
    }
}

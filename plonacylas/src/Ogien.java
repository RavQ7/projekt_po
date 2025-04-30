import java.util.Random;

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
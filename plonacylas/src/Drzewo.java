import java.util.Random;

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
            rozprzestrzenOgien(las, row, col);
        }
    }

    protected void rozprzestrzenOgien(Las las, int row, int col) {
        int rows = las.getWysokosc();
        int cols = las.getSzerokosc();
        Random random = new Random();

        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        Wiatr wiatr = las.getWiatr();
        int[] wiatrVector = wiatr.getWektor();
        int wiatrSila = wiatr.getSiła();

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                ElementTerenu sasiad = las.getPole(newRow, newCol);
                if (sasiad != null && sasiad.canBeIgnited()) {
                    double prawdopodobienstwo = 0.3;

                    if (wiatrSila > 0) {
                        int dx = dc[i];
                        int dy = dr[i];
                        double zgodnosc = (dx * wiatrVector[1] + dy * wiatrVector[0]) / 2.0;
                        prawdopodobienstwo += zgodnosc * 0.15 * wiatrSila;
                        prawdopodobienstwo = Math.max(0, Math.min(1, prawdopodobienstwo));
                    }

                    if (random.nextDouble() < prawdopodobienstwo) {
                        if (sasiad instanceof Drzewo) {
                            ((Drzewo) sasiad).setStan(StanDrzewa.PLONACE);
                            sasiad.symbol = '*';
                        }
                    }
                }
            }
        }
    }
}

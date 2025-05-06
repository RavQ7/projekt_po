import java.util.Random;

public abstract class Drzewo extends ElementTerenu {
    public enum StanDrzewa { ZDROWE, PLONACE, SPALONE }

    protected StanDrzewa stan;
    protected int czasPalenia;
    protected int aktualnyCzasPalenia;
    protected double palnosc;
    private static final Random rand = new Random();

    public Drzewo(char symbol, int czasPalenia, double palnosc) {
        super(symbol);
        this.stan = StanDrzewa.ZDROWE;
        this.czasPalenia = czasPalenia;
        this.palnosc = palnosc;
    }

    public StanDrzewa getStan() { return stan; }
    public void setStan(StanDrzewa stan) { this.stan = stan; }
    public int getAktualnyCzasPalenia() { return aktualnyCzasPalenia; }
    public void setAktualnyCzasPalenia(int AktualnyCzasPalenia) {this.aktualnyCzasPalenia = AktualnyCzasPalenia;}

    @Override
    public boolean isPalny() { return true; }

    @Override
    public boolean canBeIgnited() {
        return stan == StanDrzewa.ZDROWE;
    }

    @Override
    public void nextStep(Las las, int row, int col) {
        if (stan == StanDrzewa.PLONACE) {
            symbol = '!';
            aktualnyCzasPalenia++;
            if (aktualnyCzasPalenia >= czasPalenia) {
                stan = StanDrzewa.SPALONE;
                symbol = '#';
            }
            rozprzestrzenOgien(las, row, col);
        }
    }

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
                            sasiad.symbol = '*';
                        }
                    }
                }
            }
        }
    }
}

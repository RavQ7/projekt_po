/**
 * Reprezentuje trawę w symulacji.
 * Trawa jest palna i szybko spala się w całości.
 */
public class Trawa extends ElementTerenu {
    private int czasPalenia = 1;
    private boolean jestPalona = false;

    /**
     * Tworzy nowe pole trawy.
     */
    public Trawa() {
        super('.');
    }

    @Override
    public boolean isPalny() { return true; }

    @Override
    public boolean canBeIgnited() { return !jestPalona; }

    /**
     * Ustawia trawę jako płonącą.
     */
    public void podpal() {
        this.symbol = '*';
        this.jestPalona = true;
    }

    @Override
    public void nextStep(Las las, int row, int col) {
        if (this.symbol == '*') {
            czasPalenia--;
            if (czasPalenia <= 0) {
                las.setPole(row, col, new Puste());
            }
        }
    }

    @Override
    public ElementTerenu stworzKopie() {
        Trawa kopia = new Trawa();
        if (this.jestPalona) {
            kopia.podpal();
        }
        return kopia;
    }
}

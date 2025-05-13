/**
 * Reprezentuje zbiornik wodny w symulacji.
 * Woda nie może zostać podpalona i blokuje rozprzestrzenianie ognia.
 */
public class Woda extends ElementTerenu {
    /**
     * Tworzy nowe pole wody.
     */
    public Woda() {
        super('~');
    }

    @Override
    public boolean isPalny() { return false; }

    @Override
    public boolean canBeIgnited() { return false; }

    @Override
    public void nextStep(Las las, int row, int col) {
        // Woda nie wykonuje żadnych akcji
    }

    @Override
    public ElementTerenu stworzKopie() {
        return new Woda();
    }
}
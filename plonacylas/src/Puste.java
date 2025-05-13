/**
 * Reprezentuje puste pole w symulacji.
 * Nie może być podpalone ani nie rozprzestrzenia ognia.
 */
public class Puste extends ElementTerenu {
    /**
     * Tworzy nowe puste pole.
     */
    public Puste() {
        super(' ');
    }

    @Override
    public boolean isPalny() { return false; }

    @Override
    public boolean canBeIgnited() { return false; }

    @Override
    public void nextStep(Las las, int row, int col) {
        // Puste pole nie wykonuje żadnych akcji
    }

    @Override
    public ElementTerenu stworzKopie() {
        return new Puste();
    }
}

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

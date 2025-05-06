public abstract class ElementTerenu {
    protected char symbol;

    public ElementTerenu(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public abstract boolean isPalny();
    public abstract boolean canBeIgnited();
    public abstract void nextStep(Las las, int row, int col);
}

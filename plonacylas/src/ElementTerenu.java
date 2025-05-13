/**
 * Abstrakcyjna klasa reprezentująca dowolny element terenu w symulacji lasu.
 * Stanowi bazę dla wszystkich typów obiektów umieszczanych na mapie.
 */
public abstract class ElementTerenu {
    protected char symbol;

    /**
     * Konstruktor bazowy dla wszystkich elementów terenu.
     *
     * @param symbol Znak reprezentujący element na mapie tekstowej
     */
    public ElementTerenu(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Zwraca symbol reprezentujący element na mapie.
     *
     * @return Znak reprezentujący element
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Określa, czy element może się palić.
     *
     * @return true jeśli element jest palny, false w przeciwnym przypadku
     */
    public abstract boolean isPalny();

    /**
     * Określa, czy element może zostać podpalony.
     *
     * @return true jeśli element może zostać podpalony, false w przeciwnym przypadku
     */
    public abstract boolean canBeIgnited();

    /**
     * Wykonuje akcje dla elementu w kolejnym kroku symulacji.
     *
     * @param las Referencja do obiektu lasu, na którym przeprowadzana jest symulacja
     * @param row Numer wiersza, w którym znajduje się element
     * @param col Numer kolumny, w której znajduje się element
     */
    public abstract void nextStep(Las las, int row, int col);

    /**
     * Tworzy kopię elementu.
     * Implementacja wzorca Prototype.
     *
     * @return Nowy obiekt będący kopią bieżącego elementu
     */
    public abstract ElementTerenu stworzKopie();
}

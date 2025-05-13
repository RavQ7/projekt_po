/**
 * Reprezentuje wiatr w symulacji wpływający na rozprzestrzenianie ognia.
 * Wiatr ma kierunek i siłę, które modyfikują prawdopodobieństwo rozprzestrzeniania się ognia.
 */
public class Wiatr {
    private int kierunekDr;  // Składowa pionowa wektora kierunku
    private int kierunekDc;  // Składowa pozioma wektora kierunku
    private int sila;        // Siła wiatru (0-5)

    /**
     * Tworzy nowy obiekt wiatru bez kierunku i siły.
     */
    public Wiatr() {
        this.kierunekDr = 0;
        this.kierunekDc = 0;
        this.sila = 0;
    }

    /**
     * Ustawia kierunek wiatru.
     *
     * @param dr Składowa pionowa wektora kierunku (-1, 0, 1)
     * @param dc Składowa pozioma wektora kierunku (-1, 0, 1)
     */
    public void ustawKierunek(int dr, int dc) {
        this.kierunekDr = dr;
        this.kierunekDc = dc;
    }

    /**
     * Ustawia siłę wiatru (ograniczoną do zakresu 0-5).
     *
     * @param sila Wartość siły wiatru
     */
    public void ustawSile(int sila) {
        this.sila = Math.min(5, Math.max(0, sila));
    }

    /**
     * Zwraca wektor kierunku wiatru.
     *
     * @return Tablica dwuelementowa [dr, dc] reprezentująca wektor kierunku
     */
    public int[] getWektor() {
        return new int[]{kierunekDr, kierunekDc};
    }

    /**
     * Zwraca siłę wiatru.
     *
     * @return Wartość siły wiatru (0-5)
     */
    public int getSila() {
        return sila;
    }
}

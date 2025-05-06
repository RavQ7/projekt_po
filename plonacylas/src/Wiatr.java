public class Wiatr {
    private int kierunekDr;
    private int kierunekDc;
    private int sila;

    public Wiatr() {
        this.kierunekDr = 0;
        this.kierunekDc = 0;
        this.sila = 0;
    }

    public void ustawKierunek(int dr, int dc) {
        this.kierunekDr = dr;
        this.kierunekDc = dc;
    }

    public void ustawSile(int sila) {
        this.sila = Math.min(5, Math.max(0, sila));
    }

    public int[] getWektor() {
        return new int[]{kierunekDr, kierunekDc};
    }

    public int getSila() {
        return sila;
    }
}

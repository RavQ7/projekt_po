public class Wiatr {
    private int kierunekDr;
    private int kierunekDc;
    private int siła;

    public Wiatr() {
        this.kierunekDr = 0;
        this.kierunekDc = 0;
        this.siła = 0;
    }

    public void ustawKierunek(int dr, int dc) {
        this.kierunekDr = dr;
        this.kierunekDc = dc;
    }

    public void ustawSiłę(int siła) {
        this.siła = Math.max(0, Math.min(siła, 5));
    }

    public int[] getWektor() {
        return new int[]{kierunekDr, kierunekDc};
    }

    public int getSiła() {
        return siła;
    }
}

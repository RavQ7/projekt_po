class Wiatr {
    public enum KierunekWiatru {
        POLNOC,
        POLUDNIE,
        WSCHOD,
        ZACHOD
    }

    private KierunekWiatru kierunek;
    private double sila;

    public Wiatr(KierunekWiatru kierunek, double sila) {
        this.kierunek = kierunek;
        this.sila = sila;
    }

    public KierunekWiatru getKierunek() {
        return kierunek;
    }

    public double getSila() {
        return sila;
    }

    // Metody do modyfikacji kierunku i siły wiatru
    public void zmienKierunek(KierunekWiatru nowyKierunek) {
        this.kierunek = nowyKierunek;
    }

    public void zmienSila(double nowaSila) {
        this.sila = nowaSila;
    }
}

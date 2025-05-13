/**
 * Reprezentuje drzewo sosny w symulacji.
 * Sosna ma średni czas palenia i wysoką palność.
 */
public class Sosna extends Drzewo {
    /**
     * Tworzy nową sosnę ze standardowymi parametrami.
     */
    public Sosna() {
        super('S', 5, 0.4);
    }

    @Override
    public ElementTerenu stworzKopie() {
        Sosna kopia = new Sosna();
        kopia.setStan(this.getStan());
        kopia.setAktualnyCzasPalenia(this.getAktualnyCzasPalenia());
        return kopia;
    }
}
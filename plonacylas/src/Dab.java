/**
 * Reprezentuje drzewo dębu w symulacji.
 * Dąb ma długi czas palenia i niższą palność niż sosna.
 */
public class Dab extends Drzewo {
    /**
     * Tworzy nowy dąb ze standardowymi parametrami.
     */
    public Dab() {
        super('D', 8, 0.25);
    }

    @Override
    public ElementTerenu stworzKopie() {
        Dab kopia = new Dab();
        kopia.setStan(this.getStan());
        kopia.setAktualnyCzasPalenia(this.getAktualnyCzasPalenia());
        return kopia;
    }
}

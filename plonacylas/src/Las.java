import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Las {
    private ElementTerenu[][] pola;
    private int wysokosc;
    private int szerokosc;
    private final Wiatr wiatr = new Wiatr();
    private FileWriter csvWriter;
    private Random rand = new Random();
    private int epoka;

    public Las(int wysokosc, int szerokosc) {
        this.wysokosc = wysokosc;
        this.szerokosc = szerokosc;
        this.pola = new ElementTerenu[wysokosc][szerokosc];
        this.epoka = 0;

        try {
            csvWriter = new FileWriter("symulacja.csv");
            csvWriter.append("Epoka,Zdrowe,Plonace,Spalone\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        inicjalizujLas();
    }

    private void inicjalizujLas() {

        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                double los = rand.nextDouble();
                if (los < 0.35) pola[i][j] = new Sosna();
                else if (los < 0.7) pola[i][j] = new Dab();
                else if (los < 0.85) pola[i][j] = new Trawa();
                else pola[i][j] = new Woda();
            }
        }

        // Początkowy ogień
        int startRow = rand.nextInt(wysokosc);
        int startCol = rand.nextInt(szerokosc);
        while (!(pola[startRow][startCol] instanceof Drzewo)) {
            startRow = rand.nextInt(wysokosc);
            startCol = rand.nextInt(szerokosc);
        }

        if (pola[startRow][startCol] instanceof Drzewo) {
            ((Drzewo) pola[startRow][startCol]).setStan(Drzewo.StanDrzewa.PLONACE);
            pola[startRow][startCol].symbol = '*';
        }
    }

    public void symulujKrok() {
        ElementTerenu[][] nowaPlansza = new ElementTerenu[wysokosc][szerokosc];

        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] != null) {
                    ElementTerenu kopia = kopiujElement(pola[i][j]);
                    kopia.nextStep(this, i, j);
                    nowaPlansza[i][j] = kopia;
                }
            }
        }
        pola = nowaPlansza;
        epoka++;
    }

    private ElementTerenu kopiujElement(ElementTerenu element) {
        if (element instanceof Sosna) {
            Sosna kopia = new Sosna();
            kopia.setStan(((Sosna) element).getStan());
            kopia.setAktualnyCzasPalenia(((Sosna) element).getAktualnyCzasPalenia());
            kopia.symbol = element.symbol;
            return kopia;
        } else if (element instanceof Dab) {
            Dab kopia = new Dab();
            kopia.setStan(((Dab) element).getStan());
            kopia.setAktualnyCzasPalenia(((Dab) element).getAktualnyCzasPalenia());
            kopia.symbol = element.symbol;
            return kopia;
        } else if (element instanceof Trawa) {
            return new Trawa();
        } else if (element instanceof Woda) {
            return new Woda();
        } else {
            return new Puste();
        }
    }

    public void zapiszStanDoCSV(int epoka) {
        try {
            List<Integer> stany = zliczStany();
            csvWriter.append(String.format("%d,%d,%d,%d\n",
                    epoka, stany.get(0), stany.get(1), stany.get(2)));
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> zliczStany() {
        int zdrowe = 0, plonace = 0, spalone = 0;
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo) {
                    Drzewo.StanDrzewa stan = ((Drzewo) pola[i][j]).getStan();
                    if (stan == Drzewo.StanDrzewa.ZDROWE) zdrowe++;
                    else if (stan == Drzewo.StanDrzewa.PLONACE) plonace++;
                    else spalone++;
                }
            }
        }
        return List.of(zdrowe, plonace, spalone);
    }

    public boolean czyPożarAktywny() {
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Ogien || pola[i][j] instanceof Drzewo &&
                        ((Drzewo) pola[i][j]).getStan() == Drzewo.StanDrzewa.PLONACE) {
                    return true;
                }
            }
        }
        return false;
    }

    public double procentSpalonegoLasu() {
        List<Integer> stany = zliczStany();
        int wszystkieDrzewa = stany.get(0) + stany.get(1) + stany.get(2);
        return wszystkieDrzewa > 0 ?
                (stany.get(2) * 100.0) / wszystkieDrzewa : 0;
    }

    public Wiatr getWiatr() { return wiatr; }
    public int getWysokosc() { return wysokosc; }
    public int getSzerokosc() { return szerokosc; }
    public ElementTerenu getPole(int row, int col) { return pola[row][col]; }
    public void setPole(int row, int col, ElementTerenu element) { pola[row][col] = element; }
    public int getEpoka() { return epoka; }

    public void wyswietlLas() {
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                System.out.print(pola[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
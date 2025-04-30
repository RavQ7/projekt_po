import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Las {
    private ElementTerenu[][] pola;
    private int wysokosc;
    private int szerokosc;

    public Las(int wysokosc, int szerokosc) {
        this.wysokosc = wysokosc;
        this.szerokosc = szerokosc;
        this.pola = new ElementTerenu[wysokosc][szerokosc];
        // Inicjalizacja planszy (np. losowe rozmieszczenie elementów)
        inicjalizujLas();
    }

    public int getWysokosc() {
        return wysokosc;
    }

    public int getSzerokosc() {
        return szerokosc;
    }

    public ElementTerenu getPole(int row, int col) {
        if (row >= 0 && row < wysokosc && col >= 0 && col < szerokosc) {
            return pola[row][col];
        }
        return null;
    }

    public void setPole(int row, int col, ElementTerenu element) {
        if (row >= 0 && row < wysokosc && col >= 0 && col < szerokosc) {
            this.pola[row][col] = element;
        }
    }

    public void inicjalizujLas() {
        Random random = new Random();
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                double losowa = random.nextDouble();
                if (losowa < 0.7) {
                    pola[i][j] = new Sosna(); // 70% szans na sosnę
                } else if (losowa < 0.85) {
                    pola[i][j] = new Puste(); // 15% szans na puste pole
                } else {
                    pola[i][j] = new Woda();   // 15% szans na wodę
                }
            }
        }
        // Dodanie początkowego ognia (przykładowo w losowym miejscu)
        int startRow = random.nextInt(wysokosc);
        int startCol = random.nextInt(szerokosc);
        if (pola[startRow][startCol] instanceof Drzewo) {
            ((Drzewo) pola[startRow][startCol]).setStan(Drzewo.StanDrzewa.PLONACE);
            pola[startRow][startCol].symbol = '*';
        } else {
            // Jeśli wylosowano wodę lub puste, można spróbować innego miejsca
            for (int i = 0; i < wysokosc; i++) {
                for (int j = 0; j < szerokosc; j++) {
                    if (pola[i][j] instanceof Drzewo) {
                        ((Drzewo) pola[i][j]).setStan(Drzewo.StanDrzewa.PLONACE);
                        pola[i][j].symbol = '*';
                        return;
                    }
                }
            }
        }
    }

    public void wyswietlLas() {
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                System.out.print(pola[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void symulujKrok() {
        ElementTerenu[][] nastepnePola = new ElementTerenu[wysokosc][szerokosc];
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] != null) {
                    // Tworzymy nowy obiekt, aby uniknąć modyfikacji w trakcie iteracji
                    if (pola[i][j] instanceof Drzewo) {
                        Drzewo stareDrzewo = (Drzewo) pola[i][j];
                        Drzewo noweDrzewo = null;
                        if (stareDrzewo instanceof Sosna) {
                            noweDrzewo = new Sosna();
                        } else if (stareDrzewo instanceof Dab) {
                            noweDrzewo = new Dab();
                        }
                        if (noweDrzewo != null) {
                            noweDrzewo.stan = stareDrzewo.stan;
                            noweDrzewo.aktualnyCzasPalenia = stareDrzewo.aktualnyCzasPalenia;
                            noweDrzewo.symbol = stareDrzewo.symbol;
                            noweDrzewo.nextStep(this, i, j);
                            nastepnePola[i][j] = noweDrzewo;
                        } else {
                            nastepnePola[i][j] = pola[i][j]; // Jeśli nie Drzewo, to bez zmian
                        }
                    } else if (pola[i][j] instanceof Ogien) {
                        Ogien staryOgien = (Ogien) pola[i][j];
                        Ogien nowyOgien = new Ogien(staryOgien.getCzasTrwania());
                        nowyOgien.aktualnyCzasTrwania = staryOgien.aktualnyCzasTrwania;
                        nowyOgien.nextStep(this, i, j);
                        nastepnePola[i][j] = nowyOgien;
                    } else {
                        nastepnePola[i][j] = pola[i][j];
                    }
                }
            }
        }
        this.pola = nastepnePola;
    }

    public List<Integer> zliczStany() {
        int zdrowe = 0;
        int plonace = 0;
        int spalone = 0;
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo) {
                    Drzewo.StanDrzewa stan = ((Drzewo) pola[i][j]).getStan();
                    if (stan == Drzewo.StanDrzewa.ZDROWE) {
                        zdrowe++;
                    } else if (stan == Drzewo.StanDrzewa.PLONACE) {
                        plonace++;
                    } else if (stan == Drzewo.StanDrzewa.SPALONE) {
                        spalone++;
                    }
                } else if (pola[i][j] instanceof Ogien) {
                    plonace++; // Traktujemy ogień jako płonące
                }
            }
        }
        List<Integer> counts = new ArrayList<>();
        counts.add(zdrowe);
        counts.add(plonace);
        counts.add(spalone);
        return counts;
    }

    public boolean czyPożarAktywny() {
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo && ((Drzewo) pola[i][j]).getStan() == Drzewo.StanDrzewa.PLONACE) {
                    return true;
                }
                if (pola[i][j] instanceof Ogien) {
                    return true;
                }
            }
        }
        return false;
    }

    public double procentSpalonegoLasu() {
        int spalone = 0;
        int wszystkieDrzewa = 0;
        for (int i = 0; i < wysokosc; i++) {
            for (int j = 0; j < szerokosc; j++) {
                if (pola[i][j] instanceof Drzewo) {
                    wszystkieDrzewa++;
                    if (((Drzewo) pola[i][j]).getStan() == Drzewo.StanDrzewa.SPALONE) {
                        spalone++;
                    }
                }
            }
        }
        return 0;
    }
}
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

/**
 * Kompleksowe testy jednostkowe dla symulacji pożaru lasu.
 * Testy weryfikują działanie kluczowych klas i mechanizmów symulacji.
 */
public class ForestFireSimulationTest {

    private Las las;
    private final int TEST_WIDTH = 10;
    private final int TEST_HEIGHT = 10;

    @Before
    public void setUp() {
        // Inicjalizacja lasu testowego przed każdym testem
        las = new Las(TEST_HEIGHT, TEST_WIDTH);
    }

    @After
    public void tearDown() {
        // Usunięcie pliku CSV generowanego przez symulację
        File csvFile = new File("symulacja.csv");
        if (csvFile.exists()) {
            csvFile.delete();
        }
    }

    /**
     * Test sprawdzający, czy las jest poprawnie inicjalizowany
     */
    @Test
    public void testLasInitialization() {
        assertNotNull("Las nie powinien być null", las);
        assertEquals("Wysokość lasu powinna być zgodna z parametrem", TEST_HEIGHT, las.getWysokosc());
        assertEquals("Szerokość lasu powinna być zgodna z parametrem", TEST_WIDTH, las.getSzerokosc());

        // Sprawdzenie, czy wszystkie pola są zainicjalizowane
        for (int i = 0; i < las.getWysokosc(); i++) {
            for (int j = 0; j < las.getSzerokosc(); j++) {
                assertNotNull("Pole [" + i + "][" + j + "] nie powinno być null", las.getPole(i, j));
            }
        }

        // Sprawdzenie, czy wiatr jest zainicjalizowany
        assertNotNull("Wiatr nie powinien być null", las.getWiatr());
    }

    /**
     * Test sprawdzający, czy metoda czyPozarAktywny działa poprawnie
     */
    @Test
    public void testCzyPozarAktywny() {
        // Początkowo powinien być aktywny pożar (inicjalizacja lasu tworzy początkowy ogień)
        assertTrue("Pożar powinien być aktywny po inicjalizacji", las.czyPozarAktywny());

        // Symulacja kilku kroków
        for (int i = 0; i < 3; i++) {
            las.symulujKrok();
        }

        // Ręczne zgaszenie wszystkich płonących elementów
        for (int i = 0; i < las.getWysokosc(); i++) {
            for (int j = 0; j < las.getSzerokosc(); j++) {
                ElementTerenu element = las.getPole(i, j);
                if (element instanceof Drzewo && ((Drzewo) element).getStan() == Drzewo.StanDrzewa.PLONACE) {
                    las.setPole(i, j, new Puste());
                }
                if (element instanceof Ogien) {
                    las.setPole(i, j, new Puste());
                }
            }
        }

        // Teraz pożar nie powinien być aktywny
        assertFalse("Pożar nie powinien być aktywny po zgaszeniu wszystkich źródeł ognia", las.czyPozarAktywny());
    }

    /**
     * Test sprawdzający, czy metoda zliczStany działa poprawnie
     */
    @Test
    public void testZliczStany() {
        List<Integer> stany = las.zliczStany();
        assertNotNull("Lista stanów nie powinna być null", stany);
        assertEquals("Lista powinna zawierać 3 elementy", 3, stany.size());

        int suma = stany.get(0) + stany.get(1) + stany.get(2);
        int liczbaDrzew = 0;

        // Ręczne zliczenie drzew
        for (int i = 0; i < las.getWysokosc(); i++) {
            for (int j = 0; j < las.getSzerokosc(); j++) {
                if (las.getPole(i, j) instanceof Drzewo) {
                    liczbaDrzew++;
                }
            }
        }

        assertEquals("Suma stanów powinna być równa liczbie drzew", liczbaDrzew, suma);
    }

    /**
     * Test sprawdzający działanie metody procentSpalonegoLasu
     */
    @Test
    public void testProcentSpalonegoLasu() {
        // Początkowo procent powinien być bliski zeru
        double poczatkowyProcent = las.procentSpalonegoLasu();
        assertTrue("Początkowy procent spalonych drzew powinien być 0 lub bliski 0", poczatkowyProcent < 1.0);

        // Symulacja kilku kroków
        for (int i = 0; i < 5; i++) {
            las.symulujKrok();
        }

        // Procent powinien wzrosnąć
        double aktualnyProcent = las.procentSpalonegoLasu();
        assertTrue("Procent spalonych drzew powinien wzrosnąć po symulacji", aktualnyProcent > poczatkowyProcent);
    }

    /**
     * Test sprawdzający konstruktory i podstawowe metody dla Drzewa
     */
    @Test
    public void testDrzewo() {
        Sosna sosna = new Sosna();
        Dab dab = new Dab();

        // Test podstawowych właściwości
        assertTrue("Sosna powinna być palna", sosna.isPalny());
        assertTrue("Dąb powinien być palny", dab.isPalny());
        assertEquals("Sosna powinna mieć początkowy stan ZDROWE", Drzewo.StanDrzewa.ZDROWE, sosna.getStan());
        assertEquals("Dąb powinien mieć początkowy stan ZDROWE", Drzewo.StanDrzewa.ZDROWE, dab.getStan());

        // Test zmiany stanu
        sosna.setStan(Drzewo.StanDrzewa.PLONACE);
        assertEquals("Stan sosny powinien się zmienić na PLONACE", Drzewo.StanDrzewa.PLONACE, sosna.getStan());
        assertEquals("Symbol sosny powinien się zmienić na '*'", '*', sosna.getSymbol());

        // Test metody canBeIgnited
        assertTrue("Dąb w stanie ZDROWE powinien móc być podpalony", dab.canBeIgnited());
        dab.setStan(Drzewo.StanDrzewa.PLONACE);
        assertFalse("Dąb w stanie PLONACE nie powinien móc być podpalony", dab.canBeIgnited());
        dab.setStan(Drzewo.StanDrzewa.SPALONE);
        assertFalse("Dąb w stanie SPALONE nie powinien móc być podpalony", dab.canBeIgnited());
    }

    /**
     * Test sprawdzający konstruktory i podstawowe metody dla Trawy
     */
    @Test
    public void testTrawa() {
        Trawa trawa = new Trawa();

        assertTrue("Trawa powinna być palna", trawa.isPalny());
        assertTrue("Trawa powinna móc być podpalona", trawa.canBeIgnited());
        assertEquals("Symbol trawy powinien być '.'", '.', trawa.getSymbol());

        // Test podpalenia
        trawa.podpal();
        assertEquals("Symbol podpalonej trawy powinien być '*'", '*', trawa.getSymbol());
        assertFalse("Podpalona trawa nie powinna móc być podpalona ponownie", trawa.canBeIgnited());

        // Test kroku symulacji dla podpalonej trawy
        Las testLas = new Las(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                testLas.setPole(i, j, new Puste());
            }
        }
        testLas.setPole(1, 1, trawa);

        trawa.nextStep(testLas, 1, 1);
        assertEquals("Po jednym kroku podpalona trawa powinna zmienić się w puste pole",
                Puste.class, testLas.getPole(1, 1).getClass());
    }

    /**
     * Test sprawdzający konstruktory i podstawowe metody dla Wiatru
     */
    @Test
    public void testWiatr() {
        Wiatr wiatr = new Wiatr();

        // Test domyślnych wartości
        int[] wektor = wiatr.getWektor();
        assertEquals("Domyślny kierunek dr powinien być 0", 0, wektor[0]);
        assertEquals("Domyślny kierunek dc powinien być 0", 0, wektor[1]);
        assertEquals("Domyślna siła powinna być 0", 0, wiatr.getSila());

        // Test ustawienia kierunku
        wiatr.ustawKierunek(1, -1);
        wektor = wiatr.getWektor();
        assertEquals("Kierunek dr powinien być 1", 1, wektor[0]);
        assertEquals("Kierunek dc powinien być -1", -1, wektor[1]);

        // Test ustawienia siły
        wiatr.ustawSile(3);
        assertEquals("Siła powinna być 3", 3, wiatr.getSila());

        // Test ograniczenia siły wiatru
        wiatr.ustawSile(10);
        assertEquals("Siła powinna być ograniczona do 5", 5, wiatr.getSila());

        wiatr.ustawSile(-5);
        assertEquals("Siła nie powinna być ujemna", 0, wiatr.getSila());
    }

    /**
     * Test sprawdzający konstruktory i podstawowe metody dla ElementTerenu i jego podklas
     */
    @Test
    public void testElementyTerenu() {
        Sosna sosna = new Sosna();
        Dab dab = new Dab();
        Trawa trawa = new Trawa();
        Woda woda = new Woda();
        Puste puste = new Puste();
        Ogien ogien = new Ogien(3);

        // Test getSymbol
        assertEquals("Symbol sosny powinien być 'S'", 'S', sosna.getSymbol());
        assertEquals("Symbol dębu powinien być 'D'", 'D', dab.getSymbol());
        assertEquals("Symbol trawy powinien być '.'", '.', trawa.getSymbol());
        assertEquals("Symbol wody powinien być '~'", '~', woda.getSymbol());
        assertEquals("Symbol pustego pola powinien być ' '", ' ', puste.getSymbol());
        assertEquals("Symbol ognia powinien być '*'", '*', ogien.getSymbol());

        // Test isPalny
        assertTrue("Sosna powinna być palna", sosna.isPalny());
        assertTrue("Dąb powinien być palny", dab.isPalny());
        assertTrue("Trawa powinna być palna", trawa.isPalny());
        assertFalse("Woda nie powinna być palna", woda.isPalny());
        assertFalse("Puste pole nie powinno być palne", puste.isPalny());
        assertFalse("Ogień nie powinien być palny", ogien.isPalny());

        // Test canBeIgnited
        assertTrue("Sosna powinna móc być podpalona", sosna.canBeIgnited());
        assertTrue("Dąb powinien móc być podpalony", dab.canBeIgnited());
        assertTrue("Trawa powinna móc być podpalona", trawa.canBeIgnited());
        assertFalse("Woda nie powinna móc być podpalona", woda.canBeIgnited());
        assertFalse("Puste pole nie powinno móc być podpalone", puste.canBeIgnited());
        assertFalse("Ogień nie powinien móc być podpalony", ogien.canBeIgnited());

        // Test stworzKopie
        ElementTerenu kopiaSosny = sosna.stworzKopie();
        assertNotNull("Kopia sosny nie powinna być null", kopiaSosny);
        assertEquals("Kopia sosny powinna być klasy Sosna", Sosna.class, kopiaSosny.getClass());
        assertEquals("Symbol kopii sosny powinien być taki sam jak oryginału", sosna.getSymbol(), kopiaSosny.getSymbol());
    }

    /**
     * Test sprawdzający działanie metody setPole klasy Las
     */
    @Test
    public void testSetPole() {
        // Ustawienie nowego elementu na konkretnej pozycji
        Sosna nowaSosna = new Sosna();
        las.setPole(5, 5, nowaSosna);

        // Sprawdzenie, czy element został poprawnie ustawiony
        ElementTerenu pobranyElement = las.getPole(5, 5);
        assertNotNull("Pobrany element nie powinien być null", pobranyElement);
        assertEquals("Pobrany element powinien być klasy Sosna", Sosna.class, pobranyElement.getClass());
        assertEquals("Symbol pobranego elementu powinien być 'S'", 'S', pobranyElement.getSymbol());
    }

    /**
     * Test sprawdzający rozprzestrzenianie ognia bez wpływu wiatru
     */
    @Test
    public void testRozprzestrzenianieOgniaBezWiatru() {
        // Tworzenie testowego lasu z sosnami dookoła jednego płonącego drzewa
        Las testLas = new Las(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                testLas.setPole(i, j, new Sosna());
            }
        }

        // Środkowe drzewo podpalamy
        Sosna plonacaSosna = new Sosna();
        plonacaSosna.setStan(Drzewo.StanDrzewa.PLONACE);
        testLas.setPole(1, 1, plonacaSosna);

        // Ustawienie wiatru na 0
        testLas.getWiatr().ustawSile(0);
        testLas.getWiatr().ustawKierunek(0, 0);

        // Wykonanie kilku kroków symulacji
        int krokiSymulacji = 3;
        for (int i = 0; i < krokiSymulacji; i++) {
            testLas.symulujKrok();
        }

        // Sprawdzenie, czy ogień się rozprzestrzenia
        int liczbaPlonacych = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ElementTerenu element = testLas.getPole(i, j);
                if (element instanceof Drzewo && ((Drzewo) element).getStan() == Drzewo.StanDrzewa.PLONACE) {
                    liczbaPlonacych++;
                }
            }
        }

        // Po kilku krokach ogień powinien się rozszerzyć poza środkowy element
        assertTrue("Ogień powinien się rozprzestrzeniać na sąsiednie drzewa", liczbaPlonacych > 1);
    }

    /**
     * Test sprawdzający wpływ wiatru na rozprzestrzenianie ognia
     */
    @Test
    public void testWplywWiatruNaOgien() {
        // Ręczne ustawienie lasu
        Las testLas = new Las(5, 5);

        // Wypełnienie lasu sosnami
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                testLas.setPole(i, j, new Sosna());
            }
        }

        // Podpalenie środkowego drzewa
        Sosna plonacaSosna = new Sosna();
        plonacaSosna.setStan(Drzewo.StanDrzewa.PLONACE);
        testLas.setPole(2, 2, plonacaSosna);

        // Ustawienie silnego wiatru w jednym kierunku (na wschód)
        testLas.getWiatr().ustawSile(5);
        testLas.getWiatr().ustawKierunek(0, 1);

        // Wykonanie kilku kroków symulacji
        int krokiSymulacji = 3;
        for (int i = 0; i < krokiSymulacji; i++) {
            testLas.symulujKrok();
        }

        // Policzenie płonących drzew na wschód i zachód od początkowego ognia
        int plonaceNaWschod = 0;
        int plonaceNaZachod = 0;

        for (int j = 3; j < 5; j++) { // Wschód
            if (testLas.getPole(2, j) instanceof Drzewo &&
                    ((Drzewo) testLas.getPole(2, j)).getStan() == Drzewo.StanDrzewa.PLONACE) {
                plonaceNaWschod++;
            }
        }

        for (int j = 0; j < 2; j++) { // Zachód
            if (testLas.getPole(2, j) instanceof Drzewo &&
                    ((Drzewo) testLas.getPole(2, j)).getStan() == Drzewo.StanDrzewa.PLONACE) {
                plonaceNaZachod++;
            }
        }

        // Przy wietrze wiejącym na wschód, powinno być więcej płonących drzew po wschodniej stronie
        assertTrue("Przy wietrze wiejącym na wschód, powinno być więcej płonących drzew na wschód od źródła ognia",
                plonaceNaWschod >= plonaceNaZachod);
    }

    /**
     * Test blokowania ognia przez wodę
     */
    @Test
    public void testWodaBlokujeOgien() {
        // Ręczne ustawienie lasu
        Las testLas = new Las(3, 3);

        // Ustawienie wody i sosen
        testLas.setPole(0, 0, new Sosna());
        testLas.setPole(0, 1, new Woda());
        testLas.setPole(0, 2, new Sosna());

        // Podpalenie pierwszej sosny
        Sosna plonacaSosna = new Sosna();
        plonacaSosna.setStan(Drzewo.StanDrzewa.PLONACE);
        testLas.setPole(0, 0, plonacaSosna);

        // Wykonanie kilku kroków symulacji
        for (int i = 0; i < 5; i++) {
            testLas.symulujKrok();
        }

        // Sprawdzenie, czy sosna za wodą pozostała nienaruszona
        ElementTerenu elementZaWoda = testLas.getPole(0, 2);
        assertTrue("Element za wodą powinien być sosnę", elementZaWoda instanceof Sosna);
        assertEquals("Sosna za wodą powinna pozostać zdrowa",
                Drzewo.StanDrzewa.ZDROWE, ((Sosna) elementZaWoda).getStan());
    }

    /**
     * Test działania klasy Ogien
     */
    @Test
    public void testOgien() {
        Ogien ogien = new Ogien(2);
        assertEquals("Czas trwania ognia powinien wynosić 2", 2, ogien.getCzasTrwania());

        // Testowanie zachowania ognia w lesie
        Las testLas = new Las(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                testLas.setPole(i, j, new Puste());
            }
        }

        // Ustawienie ognia w środku
        testLas.setPole(1, 1, ogien);

        // Pierwszy krok symulacji - ogień powinien trwać
        ogien.nextStep(testLas, 1, 1);
        assertTrue("Po pierwszym kroku ogień powinien wciąż istnieć",
                testLas.getPole(1, 1) instanceof Ogien);

        // Drugi krok symulacji - ogień powinien zgasnąć
        ogien = (Ogien) testLas.getPole(1, 1);
        ogien.nextStep(testLas, 1, 1);
        assertTrue("Po drugim kroku ogień powinien zgasnąć (stać się pustym polem)",
                testLas.getPole(1, 1) instanceof Puste);
    }

    /**
     * Test metody stworzKopie dla różnych elementów terenu
     */
    @Test
    public void testStworzKopie() {
        // Testy dla różnych typów elementów
        Sosna sosna = new Sosna();
        sosna.setStan(Drzewo.StanDrzewa.PLONACE);
        sosna.setAktualnyCzasPalenia(2);

        Dab dab = new Dab();
        dab.setStan(Drzewo.StanDrzewa.SPALONE);

        Trawa trawa = new Trawa();
        trawa.podpal();

        Woda woda = new Woda();
        Puste puste = new Puste();
        Ogien ogien = new Ogien(3);

        // Tworzenie kopii
        ElementTerenu kopiaSosny = sosna.stworzKopie();
        ElementTerenu kopiaDab = dab.stworzKopie();
        ElementTerenu kopiaTrawa = trawa.stworzKopie();
        ElementTerenu kopiaWoda = woda.stworzKopie();
        ElementTerenu kopiaPuste = puste.stworzKopie();
        ElementTerenu kopiaOgien = ogien.stworzKopie();

        // Testy dla sosny
        assertTrue("Kopia sosny powinna być instancją Sosna", kopiaSosny instanceof Sosna);
        assertEquals("Stan kopii sosny powinien być taki sam jak oryginału",
                ((Drzewo) sosna).getStan(), ((Drzewo) kopiaSosny).getStan());
        assertEquals("Czas palenia kopii sosny powinien być taki sam jak oryginału",
                sosna.getAktualnyCzasPalenia(), ((Drzewo) kopiaSosny).getAktualnyCzasPalenia());

        // Testy dla dębu
        assertTrue("Kopia dębu powinna być instancją Dab", kopiaDab instanceof Dab);
        assertEquals("Stan kopii dębu powinien być taki sam jak oryginału",
                ((Drzewo) dab).getStan(), ((Drzewo) kopiaDab).getStan());

        // Testy dla trawy
        assertTrue("Kopia trawy powinna być instancją Trawa", kopiaTrawa instanceof Trawa);
        assertEquals("Symbol kopii trawy powinien być taki sam jak oryginału",
                trawa.getSymbol(), kopiaTrawa.getSymbol());

        // Testy dla pozostałych elementów
        assertTrue("Kopia wody powinna być instancją Woda", kopiaWoda instanceof Woda);
        assertTrue("Kopia pustego pola powinna być instancją Puste", kopiaPuste instanceof Puste);
        assertTrue("Kopia ognia powinna być instancją Ogien", kopiaOgien instanceof Ogien);
    }
}
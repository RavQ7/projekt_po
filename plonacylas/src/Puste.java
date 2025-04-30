class Puste extends ElementTerenu {
    private double podatnoscNaOgien = 0.1; // Prawdopodobieństwo zapłonu od sąsiada

    public Puste() {
        super('.');
    }

    @Override
    public boolean isPalny() {
        return true;
    }

    @Override
    public boolean canBeIgnited() {
        return true;
    }

    public double getPodatnoscNaOgien() {
        return podatnoscNaOgien;
    }
}

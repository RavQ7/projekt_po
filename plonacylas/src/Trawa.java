class Trawa extends ElementTerenu {
    private int czasPalenia = 1; // Ogień przechodzi przez trawę w 1 krok

    public Trawa() {
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

    @Override
    public void nextStep(Las las, int row, int col) {
        if (this.symbol == '*') {
            czasPalenia--;
            if (czasPalenia <= 0) {
                las.setPole(row, col, new Trawa());
            }
        }
    }
}

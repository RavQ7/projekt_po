class Woda extends ElementTerenu {
    public Woda() {
        super('~');
    }

    @Override
    public boolean isPalny() {
        return false;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }
}
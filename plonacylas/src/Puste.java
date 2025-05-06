public class Puste extends ElementTerenu {
    public Puste() {
        super(' ');
    }

    @Override
    public boolean isPalny() { return false; }

    @Override
    public boolean canBeIgnited() { return false; }

    @Override
    public void nextStep(Las las, int row, int col) {}
}

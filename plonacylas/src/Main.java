import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Las las = new Las(20, 20);
            new LasFrame(las);
        });
    }
}
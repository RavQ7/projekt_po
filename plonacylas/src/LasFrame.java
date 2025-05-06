import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class LasFrame extends JFrame {
    private LasPanel lasPanel;
    private JButton startButton;
    private JButton krokButton;
    private JLabel statystykiLabel;
    private Timer timer;
    private Las las;

    public LasFrame(Las las) {
        this.las = las;
        this.lasPanel = new LasPanel(las);
        this.statystykiLabel = new JLabel("Epoka: 0, Zdrowe: 0, Płonące: 0, Spalone: 0"); // Dodano Epoka

        setTitle("Symulacja Pożaru Lasu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(lasPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        krokButton = new JButton("Krok");

        controlPanel.add(startButton);
        controlPanel.add(krokButton);
        controlPanel.add(statystykiLabel);

        add(controlPanel, BorderLayout.SOUTH);

        // Inicjalizacja timera
        timer = new Timer(500, e -> wykonajKrokSymulacji());

        // Obsługa przycisków
        startButton.addActionListener(e -> timer.start());
        krokButton.addActionListener(e -> wykonajKrokSymulacji());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void wykonajKrokSymulacji() {
        las.symulujKrok();
        lasPanel.updateLas(las);
        aktualizujStatystyki();
        if (!las.czyPożarAktywny()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Pożar ugaszony!");
        }
    }

    private void aktualizujStatystyki() {
        List<Integer> statystyki = las.zliczStany();
        statystykiLabel.setText(String.format("Epoka: %d, Zdrowe: %d, Płonące: %d, Spalone: %d",
                las.getEpoka(), statystyki.get(0), statystyki.get(1), statystyki.get(2))); // Dodano numer epoki
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Las las = new Las(20, 20);
            new LasFrame(las);
        });
    }
}
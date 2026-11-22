import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Top-Down Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


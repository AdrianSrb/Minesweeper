import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MineSewwper extends JFrame {

    private JPanel gamePanel;
    private JPanel hudPanel;

    private JButton resetButton;

    private JButton[][] matrix;
    private boolean[][] minesMatrix;

    private ImageIcon smileyIcon;
    private ImageIcon deadIcon;

    private JMenuBar mb;
    private JMenu m1;
    private JMenuItem mi1;
    private JMenuItem mi2;
    private JMenuItem mi3;
    private JMenuItem mi4;

    private static final int[] I = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static final int[] J = {0, 1, 1, 1, 0, -1, -1, -1};


    public MineSewwper() {

        setTitle("MineSweeper");

        initComponent();
        initMenu();

        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void initComponent() {
        gamePanel = new JPanel();

        initHudPanel();
        initGame(10, 10);

        add(hudPanel, BorderLayout.NORTH);
        add(gamePanel);

    }

    private void initMenu() {
        mi1 = new JMenuItem("10x10");
        mi2 = new JMenuItem("15x15");
        mi3 = new JMenuItem("20x20");
        mi4 = new JMenuItem("25x25");

        mi1.addActionListener(ev -> initGame(10, 10));
        mi2.addActionListener(ev -> initGame(15, 15));
        mi3.addActionListener(ev -> initGame(20, 20));
        mi4.addActionListener(ev -> initGame(25, 25));

        m1 = new JMenu("Grad de dificultate");
        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi4);

        mb = new JMenuBar();
        mb.add(m1);
        setJMenuBar(mb);

    }

    private void initHudPanel() {
        resetButton = new JButton();

        Image scaledImage = new ImageIcon("smiley.jpg")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        smileyIcon = new ImageIcon(scaledImage);

        scaledImage = new ImageIcon("./dead.png")
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        deadIcon = new ImageIcon(scaledImage);

        resetButton.setIcon(smileyIcon);
        resetButton.addActionListener(ev -> {
            int rows = matrix.length;
            int columns = matrix[0].length;

            initGame(rows, columns);
        });
//        resetButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                initGamePanel(10,10);
//            }
//        });
      

        hudPanel = new JPanel();

        LayoutManager manager = new FlowLayout();

        hudPanel.setLayout(manager);
        hudPanel.add(resetButton);
    }

    private void initGame(int rows, int columns) {
   
        matrix = new JButton[rows][columns];

        resetButton.setIcon(smileyIcon);

        gamePanel.removeAll();
        gamePanel.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new JButton();
                gamePanel.add(matrix[i][j]);

                int row = i;
                int column = j;
                matrix[i][j].addActionListener(ev -> clcikButton(row, column));
               
            }
        }

        gamePanel.revalidate(); 

        generateMines(rows, columns);
        //showMines(); // TODO: Deltete 

    }

    private void generateMines(int rows, int columns) {
        Random random = new Random();
        minesMatrix = new boolean[rows][columns]; 

        int mines = rows * columns / 6;
        int count = 0;

        int i;
        int j;
      
        while (count < mines) {
            i = random.nextInt(rows); 
            j = random.nextInt(columns);

            if (!minesMatrix[i][j]) {
                minesMatrix[i][j] = true; 
                count++; // "generam o mina"
            }
        }

    }

    private void showMines() {
        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (minesMatrix[i][j]) {
                    JButton button = matrix[i][j];

                    button.setBackground(Color.ORANGE);
                }
            }
        }
    }

    private void mouseFlag(){
        //TODO: FLAG
    }

    private void clcikButton(int i, int j) {
        if (minesMatrix[i][j]) {
            gameOver();
            return;
        }

        expose(i, j);

    }

    private void gameOver() {
        showMines();
        resetButton.setIcon(deadIcon);

        JOptionPane.showMessageDialog(this, "AI PIERDUT");

    }

    private void expose(int i, int j) {
       
        if (!matrix[i][j].isEnabled()) {
            return;
        }
        int minesCount = countMines(i, j);
        matrix[i][j].setEnabled(false);

        if (minesCount != 0) {
            matrix[i][j].setText(String.valueOf(minesCount));
        } else {
          
            int rows = matrix.length;
            int columns = matrix[0].length;

            for (int k = 0; k < I.length; k++) {
                int newI = i + I[k];
                int newJ = j + J[k];

                if (newI < 0 || newI >= rows) {
                    continue;
                }
                if (newJ < 0 || newJ >= columns) {
                    continue;
                }
                expose(newI, newJ);
            }
        }
    }

    private int countMines(int i, int j) {

        int rows = matrix.length;
        int columns = matrix[0].length;

        int count = 0;

        for (int k = 0; k < I.length; k++) {
            int newI = i + I[k];
            int newJ = j + J[k];

            if (newI < 0 || newI >= rows) {
                continue;
            }
            if (newJ < 0 || newJ >= columns) {
                continue;
            }
            if (minesMatrix[newI][newJ]) {
                count++;
            }
        }
        return count;

    }

    public static void main(String[] args) {
        new MineSewwper();

    }
}

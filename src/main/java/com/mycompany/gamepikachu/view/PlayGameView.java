package com.mycompany.gamepikachu.view;

import com.mycompany.gamepikachu.controller.MatrixHelper;
import com.mycompany.gamepikachu.utils.Utils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class PlayGameView extends JPanelBackground implements ActionListener {

    private JPanel topMenuPanel;
    private JPanel pikachuPanel;
    private BorderLayout mainLayout;
    private GroupLayout topMenuLayout;
    private JButton resumGame;
    private JProgressBar timerProgress;
    private JLabel score1;
    private JLabel score2;
    private JButton pauseGame;
    private JLabel currentPlayer;
    private PlayGameListener playGameListener;
    private GridLayout pikachuLayout;
    private Pikachu[][] pikachuIcon;
    private int row;
    private int col;
    private int countClicked = 0;
    private Pikachu one;
    private Pikachu two;
    private JLabel player1;
    private JLabel player2;
    private Timer pathTimer;
    private boolean isPathVisible = false;
    private Pikachu pathStart;
    private Pikachu pathEnd;
    private int matrix[][];
    private Color color = Color.BLUE;

    public PlayGameView() {
        this(10, 10);
    }

    public void drawPath(Pikachu p1, Pikachu p2) {
        pathStart = p1;
        pathEnd = p2;
        isPathVisible = true;
        pathTimer.start();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isPathVisible && pathStart != null && pathEnd != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(color);
            int sx = 115;
            int sy = 100;
            Point start = new Point(one.getXPoint(), one.getYPoint());
            Point end = new Point(two.getXPoint(), two.getYPoint());
            java.util.List<Point> shortestPath = MatrixHelper.findShortestPath(matrix, start, end);
            // Vẽ đường đi ngắn nhất
            if (shortestPath != null) {
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    Point point1 = shortestPath.get(i);
                    Point point2 = shortestPath.get(i + 1);
                    int x1 = point1.y * 44 + sx;
                    int y1 = point1.x * 44 + sy;
                    int x2 = point2.y * 44 + sx;
                    int y2 = point2.x * 44 + sy;
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }

    private void setupPathTimer() {
        int pathDuration = 500; // Duration in milliseconds
        pathTimer = new Timer(pathDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPathVisible = false;
                pathStart = null;
                pathEnd = null;
                repaint();
                pathTimer.stop();
            }
        });
        pathTimer.setRepeats(false); // Only fire once
    }

    public PlayGameView(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        setVisible(false);
        initUI();
        setupPathTimer();
    }

    private void initUI() {
        mainLayout = new BorderLayout();
        this.setLayout(mainLayout);
        this.setBackgroundImage("../resources/bg_1.png");
        topMenuPanel = new JPanel();
        topMenuLayout = new GroupLayout(topMenuPanel);
        topMenuPanel.setSize(720, 60);
        topMenuPanel.setOpaque(false);
        topMenuPanel.setBorder(new EmptyBorder(5, 20, 5, 20));
        resumGame = new JButton();
        resumGame.addActionListener(this);
        Image image = new ImageIcon(getClass().getResource("../resources/resum.png")).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(40, 40, image.SCALE_SMOOTH));
        resumGame.setIcon(icon);
        resumGame.setMargin(new Insets(0, 0, 0, 0));
        resumGame.setBorder(null);
        resumGame.setActionCommand(Utils.BT_RESUM);
        timerProgress = new JProgressBar(0, 100);
        timerProgress.setValue(100);
        score1 = new JLabel("Score: 0");
        score1.setForeground(Color.WHITE);
        score2 = new JLabel("Score: 0");
        score2.setForeground(Color.WHITE);
        currentPlayer = new JLabel("Player 1");
        currentPlayer.setForeground(Color.WHITE);
        pauseGame = new JButton();
        pauseGame.addActionListener(this);
        Image img = new ImageIcon(getClass().getResource("../resources/pause.png")).getImage();
        Icon ico = new ImageIcon(img.getScaledInstance(40, 40, img.SCALE_SMOOTH));
        pauseGame.setIcon(ico);
        pauseGame.setMargin(new Insets(0, 0, 0, 0));
        pauseGame.setBorder(null);
        pauseGame.setActionCommand(Utils.BT_PAUSE);
        topMenuPanel.setLayout(topMenuLayout);
        topMenuLayout.setHorizontalGroup(
                topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(topMenuLayout.createSequentialGroup()
                                .addComponent(resumGame)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(topMenuLayout.createSequentialGroup()
                                                .addComponent(score1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(currentPlayer)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(score2))
                                        .addComponent(timerProgress, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pauseGame)
                                .addContainerGap())
        );
        topMenuLayout.setVerticalGroup(
                topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(topMenuLayout.createSequentialGroup()
                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(score1)
                                        .addComponent(currentPlayer)
                                        .addComponent(score2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timerProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(topMenuLayout.createSequentialGroup()
                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(resumGame, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pauseGame, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pikachuPanel = new JPanel();
        pikachuLayout = new GridLayout(row - 2, col - 2, 0, 0);
        pikachuPanel.setLayout(pikachuLayout);
        pikachuPanel.setOpaque(false);
        setAlignmentY(JPanel.CENTER_ALIGNMENT);

        add(topMenuPanel, BorderLayout.PAGE_START);
        player1 = new JLabel("             Player 1");
        player2 = new JLabel("Player 2             ");
        player1.setForeground(Color.RED);
        player2.setForeground(Color.BLUE);
        add(player2, BorderLayout.EAST);
        add(player1, BorderLayout.WEST);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(pikachuPanel);
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof Pikachu)) {
            switch (e.getActionCommand()) {
                case Utils.BT_RESUM:
                    if (playGameListener != null) {
                        playGameListener.onReplayClicked();
                    }
                    break;
                case Utils.BT_PAUSE:
                    if (playGameListener != null) {
                        playGameListener.onPauseClicked();
                    }
                    break;
                default:
                    break;
            }
        } else {
            ++countClicked;
            switch (countClicked) {
                case 1:
                    one = (Pikachu) e.getSource();
                    if (playGameListener != null) {
                        playGameListener.onPikachuClicked(countClicked, one);
                    }
                    break;
                case 2:
                    if (!one.equals(e.getSource())) {
                        two = (Pikachu) e.getSource();
                        if (playGameListener != null) {
                            playGameListener.onPikachuClicked(countClicked, one, two);
                        }
                    } else {
                        Utils.debug(getClass(), "Remove border");
                        one.removeBorder();
                    }
                    countClicked = 0;
                    break;
                default:
                    break;
            }
        }
    }

    public void renderMap(int[][] matrix) {
        this.matrix = matrix;
        pikachuIcon = new Pikachu[row][col];
        pikachuPanel.removeAll();
        pikachuPanel.invalidate();
        for (int i = 1; i <= row - 2; i++) {
            for (int j = 1; j <= col - 2; j++) {
                pikachuIcon[i][j] = createButton(i, j);
                Icon icon = getIcon(matrix[i][j]);
                pikachuIcon[i][j].setIcon(icon);
                pikachuIcon[i][j].drawBorder(Color.white);
                pikachuPanel.add(pikachuIcon[i][j]);
            }
        }
        pikachuPanel.repaint();
    }

    public void updateMap(int[][] matrix) {
        this.matrix = matrix;
        for (int i = 1; i <= row - 2; i++) {
            for (int j = 1; j <= col - 2; j++) {
                pikachuIcon[i][j].setIcon(getIcon(matrix[i][j]));
                pikachuIcon[i][j].setVisible(true);
            }
        }
        pikachuPanel.invalidate();
        pikachuPanel.repaint();
    }

    private Icon getIcon(int index) {
        int width = 40, height = 40;
        Image image = new ImageIcon(getClass().getResource(
                "../resources/ic_" + index + ".png")).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(width, height,
                image.SCALE_SMOOTH));
        return icon;
    }

    private Pikachu createButton(int x, int y) {
        Pikachu btn = new Pikachu(x, y);
        btn.setBorder(null);
        btn.addActionListener(this);
        return btn;
    }

    public void setPlayGameListener(PlayGameListener playGameListener) {
        this.playGameListener = playGameListener;
    }

    public void updateScore(int score) {
        if ("Player 1".equals(this.currentPlayer.getText())) {
            this.score1.setText("Score: " + score);
            this.currentPlayer.setText("Player 2");
            this.color = Color.RED;
        } else {
            this.score2.setText("Score: " + score);
            this.currentPlayer.setText("Player 1");
            this.color = Color.BLUE;
        }
    }

    public void resetScore() {
        this.score1.setText("Score: 0");
        this.score2.setText("Score: 0");
        currentPlayer.setText("Player 1");
    }

    public void updateMapNum(String map) {
        //this.currentPlayer.setText(map);
    }

    public void setCountClicked(int value) {
        this.countClicked = value;
    }

 public void updateProgress(int progress) {
    if (progress % 10 == 0) {
        Color randomColor = getRandomColor();
        if ("Player 1".equals(this.currentPlayer.getText())) {
            this.player1.setForeground(randomColor); 
        } else {
            this.player2.setForeground(randomColor);
        }
    }
    
    timerProgress.setValue(progress);
    invalidate();
}

private Color getRandomColor() {
    Random random = new Random();
    int red = random.nextInt(256);
    int green = random.nextInt(256);
    int blue = random.nextInt(256);
    return new Color(red, green, blue);
}

    public void updateMaxProgress(int progess) {
        timerProgress.setMaximum(progess);
        timerProgress.setValue(progess);
    }

    public int getMaxCountDown() {
        return timerProgress.getMaximum();
    }

    public int getCountDownValue() {
        return timerProgress.getValue();
    }

    public interface PlayGameListener {

        /**
         *
         */
        void onReplayClicked();

        /**
         * Được gọi khi nhấn Pause
         */
        void onPauseClicked();

        /**
         *
         * @param clickCounter Trả về số lần click
         * @param pikachus Trả về array pikachu đã đuợc click @arraySize = 2
         */
        void onPikachuClicked(int clickCounter, Pikachu... pikachus);
    }
}

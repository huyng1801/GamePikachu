package com.mycompany.gamepikachu.controller;
import com.mycompany.gamepikachu.utils.Utils;
import com.mycompany.gamepikachu.view.*;
import com.mycompany.gamepikachu.view.PauseMenuView.PauseMenuListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class GameController extends JFrame {
    private PlayGameView playGameView;
    private Matrix matrix;
    private Timer timer;
    private int countDown = 200;
    private String player = "Player 1";
    private int score1;  // luu score cong them 100 moi lan chon dung
    private int score2;
    /**
     * luu score cua man choi truoc do, scoreHienTai = scoreSum + score;
     */
    private int scoreSum;

    /**
     *
     */
    private int mapNumber;

   private PauseMenuView pauseMenuView;
    private int coupleDone;
    private MenuView menuView;
    /**
     *
     */
    private ActionListener timeAction;

    /**
     *
     * @param title Hiển thị tên cửa sổ game mới
     * @throws HeadlessException Không báo lỗi
     */
     private void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
          public void CanGiua(){
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        setLocation(x-350, y-300);
    }
    public GameController(String title) throws HeadlessException {
        super(title);
        Image icon = (new ImageIcon(getClass().getResource("../resources/pika_icon.png"))).getImage();
        setIconImage(icon);
        //Application.getApplication().setDockIconImage(icon);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        CanGiua();
    }

    @Override
    protected void frameInit() {
        super.frameInit();

       

        //Khởi tạo màn chơi
        this.playGameView = new PlayGameView(Utils.MAP_ROW, Utils.MAP_COL);
        this.playGameView.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
        
        this.menuView = new MenuView("../resources/bg.png");
        this.menuView.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
                
        this.pauseMenuView = new PauseMenuView("../resources/menu_bg.png");
        this.pauseMenuView.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
                //
        menuView.setOnClickMenuListener(new MenuView.OnClickMenuListener() {
            @Override
            public void onNewGameClicked(int type) {
                menuView.setVisible(false);

                //Khởi tạo màn chơi mới
                playGameView.renderMap(matrix.renderMatrix());

                int i = (new Random()).nextInt(5);
                playGameView.setBackgroundImage("../resources/bg_"+i+".png");

               // score = 0;
                scoreSum = 0;
                mapNumber = 1;
                coupleDone = 0;

                switch(type){
                    case MenuView.TYPE_EASY:
                        countDown = 200;
                        break;
                    case MenuView.TYPE_MEDIUM:
                        countDown = 180;
                        break;
                    case MenuView.TYPE_HARD:
                        countDown = 160;
                        break;
                    default:
                        break;
                }
                playGameView.updateMaxProgress(countDown);
               // playGameView.updateScore("Score: "+score);
                playGameView.updateMapNum("Map: "+mapNumber);
                playGameView.setVisible(true);
                timer.start();
            }

            @Override
            public void onSettingClicked() {
                Utils.debug(getClass(), "setting");
            }

            @Override
            public void onQuitClicked() {
                dispose();
            }
        });
        menuView.setVisible(true);
        //Khởi tạo ma trận thuật toán
        this.matrix = new Matrix(Utils.MAP_ROW, Utils.MAP_COL);
        playGameView.renderMap(matrix.getMatrix());
        
        this.timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countDown -= 1;
                playGameView.updateProgress(countDown);
                if (countDown == 0) {
                    timer.stop();
                    if("Player 1".equals(player))
                    JOptionPane.showMessageDialog(null, "Người chơi 1 hết giờ, người chơi 2 chiến thắng!");
                    else
                        JOptionPane.showMessageDialog(null, "Người chơi 2 hết giờ, người chơi 1 chiến thắng!");
                }
            }
        };

        this.timer = new Timer(100, timeAction);

        this.playGameView.setPlayGameListener(new PlayGameView.PlayGameListener() {
            @Override
            public void onReplayClicked() {
                playGameView.updateMap(matrix.renderMatrix());
                score1 = 0;
                score2 = 0;
                coupleDone = 0;
                countDown = playGameView.getMaxCountDown();
                playGameView.updateMaxProgress(countDown);
                playGameView.resetScore();
                player = "Player 1";
                playGameView.updateMapNum("Map: "+mapNumber);
                timer.start();
            }
            @Override
            public void onPauseClicked() {
                timer.stop();
                playGameView.setVisible(false);
                pauseMenuView.setVisible(true);
            }

            @Override
            public void onPikachuClicked(int clickCounter, Pikachu... pikachus) {
                if (clickCounter == 1) {
                    pikachus[0].drawBorder(Color.red);
                } else if (clickCounter == 2) {
                    pikachus[1].drawBorder(Color.red);
                    if (matrix.algorithm(pikachus[0], pikachus[1])) {

                        //Ẩn pikachu nếu chọn đúng
                        
                        matrix.setXY(pikachus[0], 0);
                        matrix.setXY(pikachus[1], 0);

                        pikachus[0].removeBorder();
                        pikachus[1].removeBorder();

                        pikachus[0].setVisible(false);
                        pikachus[1].setVisible(false);

                        //Tăng số cặp chọn đúng lên 1
                        coupleDone++;
                        playSound(getClass().getResource("../resources/sound.wav").toString().replace("file:/", ""));
                        playGameView.drawPath(pikachus[0], pikachus[1]);
                        if("Player 1".equals(player))
                        {
                            score1 += 100;
                            playGameView.updateScore(score1);
                            player = "Player 2";
                        }
                        else 
                        {
                            score2 += 100;
                            player = "Player 1";
                            playGameView.updateScore(score2);
                    
                        }
                        countDown = 200;
                        playGameView.updateProgress(countDown);
                        if (!matrix.canPlay() && coupleDone < (matrix.getRow()-2) * (matrix.getCol()-2) / 2){
                            timer.stop();
                            JOptionPane.showMessageDialog(null, "Không thể chơi tiếp!");
                            playGameView.setVisible(false);
                        }

                        if (coupleDone == (matrix.getRow()-2) * (matrix.getCol()-2) / 2) {
                            ++mapNumber;
                            if (mapNumber <= 3) {
                                score1 = 0;
                                score2 = 0;
                                countDown = playGameView.getMaxCountDown() - 10 * mapNumber;
                                coupleDone = 0;

                                playGameView.updateMaxProgress(countDown);
                                playGameView.updateMap(matrix.renderMatrix());
                                playGameView.updateMapNum("Map: "+mapNumber);
                                playGameView.resetScore();
                                player = "Player 1";
                            }else{
                                // TODO : chuc mung chien thang!
                                timer.stop();
                                if("Player 1".equals(player))
                                JOptionPane.showMessageDialog(null, "Chúc mừng người chơi 1");
                                else
                                    JOptionPane.showMessageDialog(null, "Chúc mừng người chơi 2");
                            }
                        }
                    } else {
                        pikachus[0].removeBorder();
                        pikachus[1].removeBorder();
                        playGameView.setCountClicked(0);
                    }
                }
            }
        });
         this.pauseMenuView.setPauseMenuListener(new PauseMenuListener(){
            @Override
            public void onContinueCliked() {
                pauseMenuView.setVisible(false);
                playGameView.setVisible(true);
                timer.start();
            }

            @Override
            public void onBackMenuClicked() {
                pauseMenuView.setVisible(false);
                menuView.setVisible(true);
            }

            @Override
            public void onQuitClicked() {
                dispose();
            }
            
        });
        this.add(menuView, BorderLayout.CENTER);
        this.add(playGameView, BorderLayout.CENTER);
        this.add(pauseMenuView, BorderLayout.CENTER);
        playGameView.setVisible(false);
    }

    /**
     *
     */
    public void start() {
        setVisible(true);
    }
}

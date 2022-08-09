package pbo.tubes;

import pbo.tubes.mediaplayer.SFX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH  = 720;
    private static final int SCREEN_HEIGHT = 720;

    private static final int UNIT_SIZE  = 20;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    private static int DELAY = 120;

    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];

    private final ArrayList<Integer> bombXs = new ArrayList<>(GAME_UNITS);
    private final ArrayList<Integer> bombYs = new ArrayList<>(GAME_UNITS);

    private int foodX;
    private int foodY;

    private int foodConsumed = 0;
    private int bombCount    = 1;
    private int snakeLength  = 6;

    private BufferedImage foodImage;
    private BufferedImage bombImage;

    private       Direction direction = Direction.RIGHT;
    private       boolean   running   = false;
    private       Timer     timer;
    private final Random    random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    private void startGame() {
        Snake.musicPlayer.play("music.wav");
        try {
            foodImage = ImageIO.read(new File("media/images/apple.png").toURI().toURL());
            bombImage = ImageIO.read(new File("media/images/bomb.png").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateFood();
        generateBomb();
        running = true;
        timer   = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            // GRID
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }

            // apel
            g.drawImage(foodImage.getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH), foodX, foodY, new Color(0, 0, 0, 0), null);
            // bom
            for (int i = 1; i <= bombCount; i++) {
                g.drawImage(bombImage.getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH), bombXs.get(i - 1), bombYs.get(i - 1), new Color(0, 0, 0, 0), null);
            }
            // SNAKE
            for (int i = 0; i < snakeLength; i++) {
                // Lidah
                if (i == 0) {
                    g.setColor(Color.RED);
                    if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                        g.fillRect(x[i], y[i] + UNIT_SIZE / 2, UNIT_SIZE / 2, UNIT_SIZE / 7);
                    } else {
                        g.fillRect(x[i] + UNIT_SIZE / 2, y[i], UNIT_SIZE / 7, UNIT_SIZE / 2);
                    }
                    // KEPALA
                } else if (i == 1) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    // TUBUH
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // SCORE
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String      score   = "Score: " + foodConsumed;
            g.drawString(score, SCREEN_WIDTH - metrics.stringWidth(score) - UNIT_SIZE, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    private void generateFood() {
        foodX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    private void generateBomb() {
        bombXs.add(random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE);
        bombYs.add(random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE);
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case UP -> y[0] = y[0] - UNIT_SIZE;
            case DOWN -> y[0] = y[0] + UNIT_SIZE;
            case LEFT -> x[0] = x[0] - UNIT_SIZE;
            case RIGHT -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    private void checkFoodAndBomb() {
        if (x[0] == foodX && y[0] == foodY) {
            foodConsumed++;
            snakeLength++;
            Snake.sfxPlayer.play(SFX.Eat);
            generateFood();
            if (DELAY > 50 && foodConsumed % 5 == 0) {
                generateBomb();
                bombCount++;
                timer.setDelay(DELAY = DELAY - 5);
            }
        }
    }

    private void checkCollision() {
        // Nabrak diri sendiri
        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        // Nabrak pinggiran
        if (x[0] < 0 || x[0] > SCREEN_WIDTH - UNIT_SIZE || y[0] < 0 || y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }

        // Nabrak bom
        for (int i = bombCount; i > 0; i--) {
            if (x[0] == bombXs.get(i - 1) && y[0] == bombYs.get(i - 1)) {
                running = false;
                break;
            }
        }

        if (!running) {
            Snake.sfxPlayer.play(SFX.Collided);
            Snake.musicPlayer.stop();
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {
        // SCORE
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String      score    = "Score: " + foodConsumed;
        g.drawString(score, (SCREEN_WIDTH - metrics1.stringWidth(score)) / 2, (SCREEN_HEIGHT / 2) + g.getFont().getSize());
        // GAME OVER
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 60));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        String      gameOver = "GAME OVER!";
        g.drawString(gameOver, (SCREEN_WIDTH - metrics2.stringWidth(gameOver)) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFoodAndBomb();
            checkCollision();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
            }
        }
    }
}
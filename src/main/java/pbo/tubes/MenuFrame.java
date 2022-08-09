package pbo.tubes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuFrame extends JFrame {

    MenuFrame() {
        this.setTitle("Snake");
        this.add(new MenuPanel());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private static class MenuPanel extends JPanel {

        private static final int SCREEN_WIDTH  = 720;
        private static final int SCREEN_HEIGHT = 720;
        BufferedImage bg;

        MenuPanel() {
            this.setBackground("bg.png");
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.WHITE);
            this.setFocusable(true);
            this.addKeyListener(new KeyListener());
        }

        public void setBackground(String filename) {
            try {
                bg = ImageIO.read(new File("media/images/" + filename).toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private static class KeyListener extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:

                        break;
                    case KeyEvent.VK_DOWN:

                        break;
                }
            }
        }

    }

}

package com.ponggame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Thread gameThread;
    private boolean running = false;

    private Paddle p1, p2;
    private Ball ball;

    private boolean wPressed, sPressed, upPressed, downPressed;

    private int p1Score = 0;
    private int p2Score = 0;

    private boolean inMenu = true;
    private int selectedMode = 0;  // 0 = 1P Easy, 1 = 1P Hard, 2 = 2P

    private MusicPlayer music;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        music = new MusicPlayer();
        music.playLoop();

        resetGame();
        start();
    }

    public void resetGame() {
        p1 = new Paddle(50, HEIGHT / 2 - 40, 10, 80, true);
        p2 = new Paddle(WIDTH - 60, HEIGHT / 2 - 40, 10, 80, false);
        ball = new Ball(20);

        p1Score = 0;
        p2Score = 0;
    }

    public void start() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(17); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (inMenu) return;

        // Player 1 movement
        if (wPressed) p1.moveUp();
        if (sPressed) p1.moveDown();

        // Player 2 movement or AI
        if (selectedMode == 2) { // 2P mode
            if (upPressed) p2.moveUp();
            if (downPressed) p2.moveDown();
        } else { // AI
            aiMove();
        }

        ball.move();

        // Ball collision with top/bottom
        if (ball.getY() <= 0 || ball.getY() + ball.getDiameter() >= HEIGHT) {
            ball.reverseY();
        }

        // Ball collision with paddles
        if (ball.getBounds().intersects(p1.getBounds())) {
            ball.setX(p1.getX() + p1.getWidth());
            ball.reverseX();
            ball.adjustY(p1.getY(), p1.getHeight());
        } else if (ball.getBounds().intersects(p2.getBounds())) {
            ball.setX(p2.getX() - ball.getDiameter());
            ball.reverseX();
            ball.adjustY(p2.getY(), p2.getHeight());
        }

        // Score update
        if (ball.getX() < 0) {
            p2Score++;
            ball.resetPosition();
        } else if (ball.getX() > WIDTH) {
            p1Score++;
            ball.resetPosition();
        }
    }

    private void aiMove() {
        // Easy AI: just follow the ball's y position with some tolerance
        int paddleCenter = p2.getY() + p2.getHeight() / 2;
        if (ball.getY() < paddleCenter - 10) {
            p2.moveUp();
        } else if (ball.getY() > paddleCenter + 10) {
            p2.moveDown();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inMenu) {
            drawMenu(g);
        } else {
            drawGame(g);
        }
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("PONG GAME", WIDTH / 2 - 120, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 25));
        String[] options = {"1 Player Easy", "1 Player Hard", "2 Player"};
        for (int i = 0; i < options.length; i++) {
            if (i == selectedMode) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options[i], WIDTH / 2 - 100, 200 + i * 40);
        }

        g.setFont(new Font("Arial", Font.ITALIC, 20));
        g.setColor(Color.GRAY);
        g.drawString("Use Up/Down arrows to select mode, Enter to start", WIDTH / 2 - 220, 350);
    }

    private void drawGame(Graphics g) {
        p1.draw(g);
        p2.draw(g);
        ball.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("" + p1Score, WIDTH / 2 - 50, 50);
        g.drawString("" + p2Score, WIDTH / 2 + 30, 50);
    }

    // KeyListener methods

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (inMenu) {
            if (key == KeyEvent.VK_UP) {
                selectedMode = (selectedMode - 1 + 3) % 3;
            } else if (key == KeyEvent.VK_DOWN) {
                selectedMode = (selectedMode + 1) % 3;
            } else if (key == KeyEvent.VK_ENTER) {
                inMenu = false;
                resetGame();
            }
            repaint();
        } else {
            switch (key) {
                case KeyEvent.VK_W -> wPressed = true;
                case KeyEvent.VK_S -> sPressed = true;
                case KeyEvent.VK_UP -> upPressed = true;
                case KeyEvent.VK_DOWN -> downPressed = true;
                case KeyEvent.VK_ESCAPE -> {
                    inMenu = true;
                    repaint();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W -> wPressed = false;
            case KeyEvent.VK_S -> sPressed = false;
            case KeyEvent.VK_UP -> upPressed = false;
            case KeyEvent.VK_DOWN -> downPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

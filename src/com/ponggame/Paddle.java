package com.ponggame;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Paddle {

    private int x, y;
    private int width, height;
    private int speed = 5;
    private boolean isPlayer;

    public Paddle(int x, int y, int width, int height, boolean isPlayer) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isPlayer = isPlayer;
    }

    public void moveUp() {
        if (y - speed > 0) {
            y -= speed;
        }
    }

    public void moveDown() {
        if (y + height + speed < GamePanel.HEIGHT) {
            y += speed;
        }
    }

    public void draw(Graphics g) {
        g.setColor(isPlayer ? Color.BLUE : Color.RED);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

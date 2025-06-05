package com.ponggame;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Ball {

    private int x, y;
    private int diameter;
    private int xSpeed = 5;
    private int ySpeed = 3;

    public Ball(int diameter) {
        this.diameter = diameter;
        resetPosition();
    }

    public void resetPosition() {
        x = GamePanel.WIDTH / 2 - diameter / 2;
        y = GamePanel.HEIGHT / 2 - diameter / 2;
        xSpeed = (Math.random() < 0.5) ? 5 : -5;
        ySpeed = (Math.random() < 0.5) ? 3 : -3;
    }

    public void move() {
        x += xSpeed;
        y += ySpeed;
    }

    public void reverseX() {
        xSpeed = -xSpeed;
    }

    public void reverseY() {
        ySpeed = -ySpeed;
    }

    public void adjustY(int paddleY, int paddleHeight) {
        int paddleCenter = paddleY + paddleHeight / 2;
        if (y < paddleCenter - 10) {
            ySpeed = -Math.abs(ySpeed);
        } else if (y > paddleCenter + 10) {
            ySpeed = Math.abs(ySpeed);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public int getX() {
        return x;
    }

    public void setX(int newX) {
        x = newX;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }
}

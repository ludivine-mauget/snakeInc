package org.snakeinc.snake.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import lombok.AllArgsConstructor;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.food.Apple;
import org.snakeinc.snake.model.food.Broccoli;
import org.snakeinc.snake.model.snakes.Anaconda;
import org.snakeinc.snake.model.snakes.BoaConstrictor;
import org.snakeinc.snake.model.snakes.Python;

@AllArgsConstructor
public class CellUI {

    private Cell cell;
    private int upperPixelX;
    private int upperPixelY;

    public void drawRectangle(Graphics g) {
        g.fillRect(upperPixelX, upperPixelY, GamePanel.TILE_PIXEL_SIZE, GamePanel.TILE_PIXEL_SIZE);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getSnakeColor(g2) .darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(upperPixelX, upperPixelY, GamePanel.TILE_PIXEL_SIZE, GamePanel.TILE_PIXEL_SIZE);
    }

    public void drawOval(Graphics g) {
        g.fillOval(upperPixelX, upperPixelY, GamePanel.TILE_PIXEL_SIZE, GamePanel.TILE_PIXEL_SIZE);
    }

    public void draw(Graphics g) {
        if (cell.containsFood()) {
            g.setColor(getFoodColor(g));
            drawOval(g);
        }
        if (cell.containsASnake()) {
            g.setColor(getSnakeColor(g));
            drawRectangle(g);
        }
    }

    private Color getSnakeColor(Graphics g) {
        switch (cell.getSnake()) {
            case Anaconda anaconda -> {
                return Color.GRAY;
            }
            case Python python   -> {
                return Color.GREEN;
            }
            case BoaConstrictor boaConstrictor -> {
                return Color.BLUE;
            }
            default -> {
                return Color.MAGENTA;
            }
        }
    }

    private Color getFoodColor(Graphics g) {
        switch (cell.getFood()) {
            case Apple apple -> {
                if (apple.isPoisonous())
                {
                    return Color.MAGENTA;
                }
                return Color.RED;
            }
            case Broccoli broccoli -> {
                if (broccoli.isSteamed())
                {
                    return Color.YELLOW;
                }
                return Color.GREEN;
            }
            default -> {
                return Color.MAGENTA;
            }
        }
    }

}

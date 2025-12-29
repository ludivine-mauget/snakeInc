package org.snakeinc.snake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.api.ApiClient;
import org.snakeinc.snake.api.PlayerResponse;
import org.snakeinc.snake.api.ScoreListResponse;
import org.snakeinc.snake.api.ScoreResponse;
import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.Direction;
import org.snakeinc.snake.model.Game;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int TILE_PIXEL_SIZE = 20;
    public static final int GAME_PIXEL_WIDTH = TILE_PIXEL_SIZE * GameParams.TILES_X;
    public static final int GAME_PIXEL_HEIGHT = TILE_PIXEL_SIZE * GameParams.TILES_Y;

    private Timer timer;
    private Game game;
    private boolean running = false;
    private Direction direction = Direction.RIGHT;
    private final ApiClient apiClient;
    private final PlayerResponse currentPlayer;
    private Integer bestScore = null;
    private boolean scoreSubmitted = false;

    public GamePanel(ApiClient apiClient, PlayerResponse player) {
        this.apiClient = apiClient;
        this.currentPlayer = player;
        this.setPreferredSize(new Dimension(GAME_PIXEL_WIDTH, GAME_PIXEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    private void startGame() {
        game = new Game();
        timer = new Timer(100, this);
        timer.start();
        running = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            UIUtils.draw(g, game);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        // Soumettre le score si ce n'est pas déjà fait
        if (!scoreSubmitted) {
            submitScore();
            scoreSubmitted = true;
        }

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());

        int yOffset = GAME_PIXEL_HEIGHT / 2 - 60;

        g.drawString("Game Over", (GAME_PIXEL_WIDTH - metrics.stringWidth("Game Over")) / 2, yOffset);
        yOffset += 30;

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        metrics = getFontMetrics(g.getFont());

        g.drawString("Joueur: " + currentPlayer.name(), (GAME_PIXEL_WIDTH - metrics.stringWidth("Joueur: " + currentPlayer.name())) / 2, yOffset);
        yOffset += 25;

        g.drawString("Score: " + game.getScore().getPoints(), (GAME_PIXEL_WIDTH - metrics.stringWidth("Score: " + game.getScore().getPoints())) / 2, yOffset);
        yOffset += 25;

        g.drawString("Moves Made: " + game.getScore().getMovesMade(), (GAME_PIXEL_WIDTH - metrics.stringWidth("Moves Made: " + game.getScore().getMovesMade())) / 2, yOffset);
        yOffset += 25;

        g.drawString("Food Eaten: " + game.getScore().getFoodsEaten(), (GAME_PIXEL_WIDTH - metrics.stringWidth("Food Eaten: " + game.getScore().getFoodsEaten())) / 2, yOffset);
        yOffset += 25;

        // Afficher le meilleur score
        if (bestScore != null) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            metrics = getFontMetrics(g.getFont());
            String bestScoreText = "Meilleur score: " + bestScore;
            g.drawString(bestScoreText, (GAME_PIXEL_WIDTH - metrics.stringWidth(bestScoreText)) / 2, yOffset + 15);
        }
    }

    private void submitScore() {
        try {
            // Envoyer le score à l'API
            apiClient.createScore(game.getSnakeType(), game.getScore().getPoints(), currentPlayer.id());

            // Récupérer le meilleur score du joueur pour ce type de serpent
            ScoreListResponse scores = apiClient.getScores(game.getSnakeType(), currentPlayer.id());
            if (scores != null && scores.scores() != null && !scores.scores().isEmpty()) {
                bestScore = scores.scores().stream()
                        .mapToInt(ScoreResponse::score)
                        .max()
                        .orElse(0);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la soumission du score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            try {
                game.iterate(direction);
            } catch (OutOfPlayException | SelfCollisionException | MalnutritionException exception) {
                timer.stop();
                running = false;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Direction newDirection = switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            default -> null;
        };

        if (newDirection != null) {
            direction = newDirection;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

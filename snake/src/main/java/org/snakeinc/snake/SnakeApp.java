package org.snakeinc.snake;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.snakeinc.snake.api.ApiClient;
import org.snakeinc.snake.api.PlayerResponse;
import org.snakeinc.snake.ui.GamePanel;
import org.snakeinc.snake.ui.PlayerSelectionDialog;

public class SnakeApp {

    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient();

        // Afficher la boîte de dialogue de sélection du joueur
        PlayerResponse player = PlayerSelectionDialog.showDialog(null, apiClient);

        if (player == null) {
            JOptionPane.showMessageDialog(null,
                    "Aucun joueur sélectionné. Le jeu va se fermer.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            try {
                apiClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        JFrame frame = new JFrame("Snake Inc - Joueur: " + player.name());
        GamePanel gamePanel = new GamePanel(apiClient, player);
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        // Fermer l'API client quand la fenêtre se ferme
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    apiClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
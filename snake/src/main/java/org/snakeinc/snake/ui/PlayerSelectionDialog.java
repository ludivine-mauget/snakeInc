package org.snakeinc.snake.ui;

import lombok.Getter;
import org.snakeinc.snake.api.ApiClient;
import org.snakeinc.snake.api.PlayerListResponse;
import org.snakeinc.snake.api.PlayerResponse;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSelectionDialog extends JDialog {
    @Getter
    private PlayerResponse selectedPlayer;
    private final ApiClient apiClient;

    public PlayerSelectionDialog(Frame parent, ApiClient apiClient) {
        super(parent, "Sélection du joueur", true);
        this.apiClient = apiClient;

        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        initComponents();
    }

    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Label d'instruction
        JLabel instructionLabel = new JLabel("Sélectionnez un joueur ou créez-en un nouveau :");
        mainPanel.add(instructionLabel, BorderLayout.NORTH);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JButton selectExistingButton = new JButton("Sélectionner un joueur existant");
        selectExistingButton.addActionListener(e -> selectExistingPlayer());

        JButton createNewButton = new JButton("Créer un nouveau joueur");
        createNewButton.addActionListener(e -> createNewPlayer());

        buttonPanel.add(selectExistingButton);
        buttonPanel.add(createNewButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void selectExistingPlayer() {
        try {
            List<PlayerResponse> players = loadPlayers();

            if (players.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucun joueur trouvé. Veuillez créer un nouveau joueur.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                createNewPlayer();
                return;
            }

            String[] playerNames = players.stream()
                    .map(p -> p.name() + " (ID: " + p.id() + ", Age: " + p.age() + ")")
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Choisissez un joueur:",
                    "Sélection du joueur",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    playerNames,
                    playerNames[0]);

            if (selected != null) {
                int index = java.util.Arrays.asList(playerNames).indexOf(selected);
                selectedPlayer = players.get(index);
                dispose();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des joueurs: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewPlayer() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();

        panel.add(new JLabel("Nom:"));
        panel.add(nameField);
        panel.add(new JLabel("Âge:"));
        panel.add(ageField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Créer un nouveau joueur", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();

            if (name.isEmpty() || ageText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                if (age < 0 || age > 150) {
                    JOptionPane.showMessageDialog(this,
                            "L'âge doit être entre 0 et 150.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                selectedPlayer = apiClient.createPlayer(name, age);
                JOptionPane.showMessageDialog(this,
                        "Joueur créé avec succès: " + selectedPlayer.name(),
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "L'âge doit être un nombre valide.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la création du joueur: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<PlayerResponse> loadPlayers() throws IOException {
        PlayerListResponse response = apiClient.getPlayers();
        if (response != null && response.players() != null) {
            return response.players();
        }
        return new ArrayList<>();
    }

    public static PlayerResponse showDialog(Frame parent, ApiClient apiClient) {
        PlayerSelectionDialog dialog = new PlayerSelectionDialog(parent, apiClient);
        dialog.setVisible(true);
        return dialog.getSelectedPlayer();
    }
}


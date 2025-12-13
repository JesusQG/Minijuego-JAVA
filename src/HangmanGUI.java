import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class HangmanGUI extends JFrame {
    private Player player;
    private HangmanGame game;
    private WordBank wordBank;

    // Componentes de la interfaz
    private JLabel playerLabel;
    private JLabel scoreLabel;
    private JLabel wordLabel;
    private JLabel attemptsLabel;
    private JLabel statusLabel;
    private JPanel hangmanPanel;
    private JPanel lettersPanel;
    private JButton[] letterButtons;
    private JButton hintButton;
    private JButton newGameButton;
    private JButton exitButton;
    private JTextArea statsArea;

    public HangmanGUI() {
        wordBank = new WordBank();

        // Pedir nombre del jugador
        String playerName = JOptionPane.showInputDialog(this,
                "¡Bienvenido al Juego del Ahorcado!\nIngresa tu nombre:",
                "Inicio", JOptionPane.PLAIN_MESSAGE);

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Jugador";
        }

        player = new Player(playerName);

        // Configurar la ventana
        setTitle("Juego del Ahorcado - " + player.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 245));

        // Panel superior - Información del jugador
        JPanel topPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(70, 130, 180));

        playerLabel = new JLabel("Jugador: " + player.getName());
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        scoreLabel = new JLabel("Puntuación: " + player.getScore());
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        attemptsLabel = new JLabel("Intentos: -/-");
        attemptsLabel.setForeground(Color.WHITE);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        statusLabel = new JLabel("Estado: Preparado");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        topPanel.add(playerLabel);
        topPanel.add(scoreLabel);
        topPanel.add(attemptsLabel);
        topPanel.add(statusLabel);

        // Panel central - Juego
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(240, 240, 245));

        // Panel del dibujo del ahorcado
        hangmanPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHangman(g);
            }
        };
        hangmanPanel.setPreferredSize(new Dimension(300, 300));
        hangmanPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        hangmanPanel.setBackground(Color.WHITE);

        // Panel de la palabra
        wordLabel = new JLabel("", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        wordLabel.setForeground(new Color(0, 100, 0));
        wordLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(hangmanPanel, BorderLayout.CENTER);
        centerPanel.add(wordLabel, BorderLayout.SOUTH);

        // Panel de letras (teclado)
        lettersPanel = new JPanel(new GridLayout(4, 7, 5, 5));
        lettersPanel.setBorder(BorderFactory.createTitledBorder("Selecciona una letra"));
        lettersPanel.setBackground(new Color(240, 240, 245));

        letterButtons = new JButton[28]; // A-Z + Ñ
        String[] letters = {
                "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N",
                "Ñ", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"
        };

        for (int i = 0; i < letters.length; i++) {
            letterButtons[i] = new JButton(letters[i]);
            letterButtons[i].setFont(new Font("Arial", Font.BOLD, 14));
            letterButtons[i].setBackground(new Color(100, 149, 237));
            letterButtons[i].setForeground(Color.WHITE);
            letterButtons[i].setFocusPainted(false);
            letterButtons[i].setEnabled(false);

            final String letter = letters[i];
            letterButtons[i].addActionListener(e -> guessLetter(letter.charAt(0)));

            lettersPanel.add(letterButtons[i]);
        }

        // Panel de botones de control
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlPanel.setBackground(new Color(240, 240, 245));

        hintButton = new JButton("💡 Pista (2 disponibles)");
        hintButton.setFont(new Font("Arial", Font.BOLD, 12));
        hintButton.setBackground(new Color(255, 215, 0));
        hintButton.setEnabled(false);
        hintButton.addActionListener(e -> useHint());

        newGameButton = new JButton("🎮 Nuevo Juego");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 12));
        newGameButton.setBackground(new Color(50, 205, 50));
        newGameButton.addActionListener(e -> startNewGame());

        exitButton = new JButton("❌ Salir");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(hintButton);
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);

        // Panel de estadísticas
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setBackground(new Color(255, 250, 240));
        statsArea.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        JScrollPane statsScroll = new JScrollPane(statsArea);
        statsScroll.setPreferredSize(new Dimension(250, 0));

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 245));

        bottomPanel.add(lettersPanel, BorderLayout.CENTER);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        // Agregar todos los paneles a la ventana
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(statsScroll, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Configurar ventana
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Iniciar primer juego
        startNewGame();
    }

    private void drawHangman(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (game == null) return;

        int state = game.getHangmanState();
        int centerX = getWidth() / 2;

        // Dibujar la base
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(centerX - 100, 250, 200, 20);
        g2d.fillRect(centerX - 20, 50, 40, 200);
        g2d.fillRect(centerX - 100, 50, 20, 20);

        // Dibujar la cuerda
        g2d.setColor(Color.BLACK);
        g2d.drawLine(centerX - 90, 70, centerX - 90, 90);

        // Dibujar estados según intentos
        switch (state) {
            case 0: // 100-75% intentos - Cabeza
                g2d.setColor(Color.PINK);
                g2d.fillOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(centerX - 110, 90, 40, 40);
                break;

            case 1: // 75-50% intentos - Cuerpo
                g2d.setColor(Color.PINK);
                g2d.fillOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLUE);
                g2d.fillRect(centerX - 100, 130, 20, 50);
                break;

            case 2: // 50-25% intentos - Brazos
                g2d.setColor(Color.PINK);
                g2d.fillOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLUE);
                g2d.fillRect(centerX - 100, 130, 20, 50);
                // Brazos
                g2d.drawLine(centerX - 100, 140, centerX - 130, 160);
                g2d.drawLine(centerX - 80, 140, centerX - 50, 160);
                break;

            case 3: // 25-0% intentos - Piernas
                g2d.setColor(Color.PINK);
                g2d.fillOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(centerX - 110, 90, 40, 40);
                // Cara triste
                g2d.drawArc(centerX - 100, 100, 20, 15, 0, 180);
                g2d.fillOval(centerX - 100, 105, 5, 5);
                g2d.fillOval(centerX - 85, 105, 5, 5);

                g2d.setColor(Color.BLUE);
                g2d.fillRect(centerX - 100, 130, 20, 50);
                g2d.drawLine(centerX - 100, 140, centerX - 130, 160);
                g2d.drawLine(centerX - 80, 140, centerX - 50, 160);
                // Piernas
                g2d.drawLine(centerX - 100, 180, centerX - 120, 220);
                g2d.drawLine(centerX - 80, 180, centerX - 60, 220);
                break;

            case 4: // 0% intentos - Ahorcado completo con cara X_X
                g2d.setColor(Color.PINK);
                g2d.fillOval(centerX - 110, 90, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(centerX - 110, 90, 40, 40);
                // Cara muerta X_X
                g2d.drawString("X", centerX - 100, 110);
                g2d.drawString("X", centerX - 85, 110);
                g2d.drawArc(centerX - 100, 115, 20, 10, 0, -180);

                g2d.setColor(Color.BLUE);
                g2d.fillRect(centerX - 100, 130, 20, 50);
                g2d.drawLine(centerX - 100, 140, centerX - 130, 160);
                g2d.drawLine(centerX - 80, 140, centerX - 50, 160);
                g2d.drawLine(centerX - 100, 180, centerX - 120, 220);
                g2d.drawLine(centerX - 80, 180, centerX - 60, 220);

                // Lengua fuera
                g2d.setColor(Color.RED);
                g2d.fillOval(centerX - 95, 120, 10, 5);
                break;
        }

        // Dibujar título
        g2d.setColor(new Color(70, 130, 180));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("ESTADO DEL AHORCADO", centerX - 80, 30);
    }

    private void startNewGame() {
        String[] options = {"Fácil (3-5 letras)", "Medio (6-8 letras)", "Difícil (9+ letras)"};
        int choice = JOptionPane.showOptionDialog(this,
                "Selecciona la dificultad:",
                "Nuevo Juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        String word;
        switch (choice) {
            case 0: word = wordBank.getRandomWord(3, 5); break;
            case 1: word = wordBank.getRandomWord(6, 8); break;
            case 2: word = wordBank.getRandomWord(9, 15); break;
            default: word = wordBank.getRandomWord(6, 8);
        }

        game = new HangmanGame(word);
        updateGameDisplay();
        enableAllLetterButtons();

        // Actualizar estadísticas
        updateStats();
    }

    private void guessLetter(char letter) {
        if (game.isGameOver()) return;

        boolean correct = game.guessLetter(letter);
        JButton button = findButton(letter);

        if (button != null) {
            if (correct) {
                button.setBackground(new Color(50, 205, 50));
                button.setEnabled(false);
            } else {
                button.setBackground(new Color(220, 20, 60));
                button.setEnabled(false);
            }
        }

        updateGameDisplay();

        if (game.isGameOver()) {
            gameOver();
        }
    }

    private void useHint() {
        if (game == null || game.isGameOver()) return;

        String hint = game.getHint();
        if (hint != null) {
            JOptionPane.showMessageDialog(this, hint, "Pista", JOptionPane.INFORMATION_MESSAGE);
            updateGameDisplay();
            hintButton.setText("💡 Pista (" + (2 - game.getHintsUsed()) + " disponible)");

            if (game.getHintsUsed() >= 2) {
                hintButton.setEnabled(false);
            }

            if (game.isGameOver()) {
                gameOver();
            }
        }
    }

    private void updateGameDisplay() {
        if (game == null) return;

        // Actualizar palabra
        wordLabel.setText(game.getGuessedWord());

        // Actualizar intentos
        attemptsLabel.setText(String.format("Intentos: %d/%d",
                game.getRemainingAttempts(), game.getMaxAttempts()));

        // Actualizar estado
        if (game.isGameOver()) {
            statusLabel.setText(game.hasWon() ? "Estado: ¡GANASTE!" : "Estado: ¡PERDISTE!");
        } else {
            statusLabel.setText(String.format("Estado: Jugando (%d%%)",
                    (int)game.getProgressPercentage()));
        }

        // Actualizar puntuación
        scoreLabel.setText("Puntuación: " + player.getScore());

        // Actualizar botón de pista
        int hintsLeft = 2 - game.getHintsUsed();
        hintButton.setText("💡 Pista (" + hintsLeft + " disponible)");
        hintButton.setEnabled(!game.isGameOver() && hintsLeft > 0);

        // Redibujar el ahorcado
        hangmanPanel.repaint();
    }

    private void gameOver() {
        disableAllLetterButtons();

        if (game.hasWon()) {
            int score = game.calculateScore();
            player.incrementScore(score);
            player.addGamePlayed(true);

            JOptionPane.showMessageDialog(this,
                    String.format("¡FELICIDADES %s! ¡GANASTE!\n\nPalabra: %s\nPuntos ganados: %d\nPuntuación total: %d",
                            player.getName(), game.getSecretWord(), score, player.getScore()),
                    "¡VICTORIA!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            player.addGamePlayed(false);

            JOptionPane.showMessageDialog(this,
                    String.format("¡JUEGO TERMINADO!\n\nLa palabra era: %s\n\n¡Mejor suerte la próxima vez!",
                            game.getSecretWord()),
                    "Fin del Juego",
                    JOptionPane.WARNING_MESSAGE);
        }

        updateStats();
    }

    private void updateStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("ESTADÍSTICAS\n");
        stats.append("══════════════\n");
        stats.append("Jugador: ").append(player.getName()).append("\n");
        stats.append("Puntuación: ").append(player.getScore()).append("\n");
        stats.append("Partidas: ").append(player.getGamesPlayed()).append("\n");
        stats.append("Victorias: ").append(player.getGamesWon()).append("\n");
        stats.append("Porcentaje: ").append(String.format("%.1f%%", player.getWinRate())).append("\n\n");

        if (game != null) {
            stats.append("ÚLTIMA PARTIDA\n");
            stats.append("══════════════\n");
            stats.append("Palabra: ").append(game.getSecretWord()).append("\n");
            stats.append("Longitud: ").append(game.getWordLength()).append(" letras\n");
            stats.append("Intentos usados: ").append(game.getMaxAttempts() - game.getRemainingAttempts()).append("\n");
            stats.append("Pistas usadas: ").append(game.getHintsUsed()).append("\n");
            stats.append("Resultado: ").append(game.hasWon() ? "GANÓ" : "PERDIÓ").append("\n");
        }

        statsArea.setText(stats.toString());
    }

    private JButton findButton(char letter) {
        for (JButton button : letterButtons) {
            if (button != null && button.getText().charAt(0) == letter) {
                return button;
            }
        }
        return null;
    }

    private void enableAllLetterButtons() {
        for (JButton button : letterButtons) {
            if (button != null) {
                button.setEnabled(true);
                button.setBackground(new Color(100, 149, 237));
            }
        }
    }

    private void disableAllLetterButtons() {
        for (JButton button : letterButtons) {
            if (button != null) {
                button.setEnabled(false);
            }
        }
    }
}
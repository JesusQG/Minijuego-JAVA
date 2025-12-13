import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Usar SwingUtilities para crear la interfaz en el hilo correcto
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer Look and Feel (opcional)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Crear y mostrar la ventana
                HangmanGUI game = new HangmanGUI();
                game.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                // Si falla el Look and Feel, crear la GUI igual
                HangmanGUI game = new HangmanGUI();
                game.setVisible(true);
            }
        });
    }
}
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== BIENVENIDO AL JUEGO DEL AHORCADO ===");
        System.out.print("Ingresa tu nombre: ");
        String playerName = scanner.nextLine();

        Player player = new Player(playerName);
        WordBank wordBank = new WordBank();

        System.out.println("\n¡Hola, " + player.getName() + "!");
        System.out.println("Vamos a comenzar el juego...");

        // Crear un juego con una palabra aleatoria
        String word = wordBank.getRandomWord();
        HangmanGame game = new HangmanGame(word);

        System.out.println("\nPalabra a adivinar: " + game.getGuessedWord());
        System.out.println("Longitud de la palabra: " + word.length() + " letras");
        System.out.println("Intentos disponibles: " + game.getMaxAttempts());

        // Juego básico
        while (!game.isGameOver()) {
            System.out.print("\nIngresa una letra: ");
            char letter = scanner.nextLine().toUpperCase().charAt(0);

            boolean correct = game.guessLetter(letter);

            if (correct) {
                System.out.println("¡Correcto! La letra '" + letter + "' está en la palabra.");
            } else {
                System.out.println("Incorrecto. La letra '" + letter + "' no está en la palabra.");
                System.out.println("Te quedan " + game.getRemainingAttempts() + " intentos.");
            }

            System.out.println("Palabra actual: " + game.getGuessedWord());
        }

        // Resultado final
        if (game.hasWon()) {
            System.out.println("\n¡FELICIDADES! ¡GANASTE!");
            System.out.println("La palabra era: " + game.getSecretWord());
            player.incrementScore(100);
        } else {
            System.out.println("\n¡JUEGO TERMINADO!");
            System.out.println("La palabra era: " + game.getSecretWord());
        }

        System.out.println("\n" + player);
        scanner.close();
    }
}
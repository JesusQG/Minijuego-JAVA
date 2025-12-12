public class HangmanGame {
    private String secretWord;
    private char[] guessedLetters;
    private int maxAttempts;
    private int remainingAttempts;
    private int correctGuesses;
    private boolean gameOver;

    public HangmanGame(String word) {
        this.secretWord = word.toUpperCase();
        this.guessedLetters = new char[secretWord.length()];
        this.maxAttempts = calculateMaxAttempts(secretWord.length());
        this.remainingAttempts = maxAttempts;
        this.correctGuesses = 0;
        this.gameOver = false;

        // Inicializar con guiones bajos
        for (int i = 0; i < guessedLetters.length; i++) {
            guessedLetters[i] = '_';
        }
    }

    private int calculateMaxAttempts(int wordLength) {
        // La mitad de las letras, redondeado hacia arriba
        return (int) Math.ceil(wordLength / 2.0);
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGuessedWord() {
        return new String(guessedLetters);
    }

    public String getSecretWord() {
        return secretWord;
    }

    public boolean guessLetter(char letter) {
        letter = Character.toUpperCase(letter);
        boolean found = false;

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter && guessedLetters[i] == '_') {
                guessedLetters[i] = letter;
                correctGuesses++;
                found = true;
            }
        }

        if (!found) {
            remainingAttempts--;
        }

        checkGameStatus();
        return found;
    }

    private void checkGameStatus() {
        if (correctGuesses == secretWord.length()) {
            gameOver = true;
        } else if (remainingAttempts <= 0) {
            gameOver = true;
        }
    }

    public boolean hasWon() {
        return correctGuesses == secretWord.length();
    }
}
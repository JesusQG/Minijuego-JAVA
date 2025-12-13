import java.util.HashSet;
import java.util.Set;

public class HangmanGame {
    private String secretWord;
    private char[] guessedLetters;
    private int maxAttempts;
    private int remainingAttempts;
    private int correctGuesses;
    private boolean gameOver;
    private Set<Character> incorrectGuesses;
    private int hintsUsed;

    public HangmanGame(String word) {
        this.secretWord = word.toUpperCase();
        this.guessedLetters = new char[secretWord.length()];
        this.maxAttempts = calculateMaxAttempts(secretWord.length());
        this.remainingAttempts = maxAttempts;
        this.correctGuesses = 0;
        this.gameOver = false;
        this.incorrectGuesses = new HashSet<>();
        this.hintsUsed = 0;

        for (int i = 0; i < guessedLetters.length; i++) {
            guessedLetters[i] = '_';
        }
    }

    private int calculateMaxAttempts(int wordLength) {
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
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < guessedLetters.length; i++) {
            result.append(guessedLetters[i]);
            if (i < guessedLetters.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public String getSecretWord() {
        return secretWord;
    }

    public Set<Character> getIncorrectGuesses() {
        return new HashSet<>(incorrectGuesses);
    }

    public boolean guessLetter(char letter) {
        if (gameOver) return false;

        letter = Character.toUpperCase(letter);
        boolean found = false;

        for (int i = 0; i < secretWord.length(); i++) {
            if (guessedLetters[i] == letter) {
                return true;
            }
        }

        if (incorrectGuesses.contains(letter)) {
            return false;
        }

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter && guessedLetters[i] == '_') {
                guessedLetters[i] = letter;
                correctGuesses++;
                found = true;
            }
        }

        if (!found) {
            remainingAttempts--;
            incorrectGuesses.add(letter);
        }

        checkGameStatus();
        return found;
    }

    public String getHint() {
        if (hintsUsed >= 2) {
            return null;
        }

        for (int i = 0; i < secretWord.length(); i++) {
            if (guessedLetters[i] == '_') {
                guessedLetters[i] = secretWord.charAt(i);
                correctGuesses++;
                hintsUsed++;
                checkGameStatus();
                return "Pista usada: Letra '" + secretWord.charAt(i) + "' revelada";
            }
        }

        return null;
    }

    public int getHintsUsed() {
        return hintsUsed;
    }

    public int getWordLength() {
        return secretWord.length();
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

    public double getProgressPercentage() {
        return (correctGuesses * 100.0) / secretWord.length();
    }

    public int getHangmanState() {
        double progress = (remainingAttempts * 100.0) / maxAttempts;
        if (progress <= 0) return 4;
        if (progress <= 25) return 3;
        if (progress <= 50) return 2;
        if (progress <= 75) return 1;
        return 0;
    }

    public int calculateScore() {
        if (!hasWon()) return 0;

        int baseScore = 100 * getDifficultyLevel();
        int attemptsBonus = remainingAttempts * 20;
        int hintsPenalty = hintsUsed * 30;
        int lengthBonus = secretWord.length() * 10;

        return baseScore + attemptsBonus + lengthBonus - hintsPenalty;
    }

    public int getDifficultyLevel() {
        int length = secretWord.length();
        if (length <= 5) return 1;
        if (length <= 8) return 2;
        return 3;
    }

    public boolean[] getGuessedStatus() {
        boolean[] status = new boolean[secretWord.length()];
        for (int i = 0; i < secretWord.length(); i++) {
            status[i] = guessedLetters[i] != '_';
        }
        return status;
    }
}
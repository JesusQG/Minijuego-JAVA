public class Player {
    private String name;
    private int score;
    private int gamesPlayed;
    private int gamesWon;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void incrementScore(int points) {
        this.score += points;
    }

    public void addGamePlayed(boolean won) {
        this.gamesPlayed++;
        if (won) {
            this.gamesWon++;
        }
    }

    public void resetScore() {
        this.score = 0;
    }

    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (gamesWon * 100.0) / gamesPlayed;
    }

    @Override
    public String toString() {
        return String.format("Jugador: %s | Puntuación: %d | Partidas: %d | Victorias: %d (%.1f%%)",
                name, score, gamesPlayed, gamesWon, getWinRate());
    }
}
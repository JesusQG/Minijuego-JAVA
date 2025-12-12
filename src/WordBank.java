import java.util.Random;

public class WordBank {
    private String[] words = {
            "ELEFANTE", "COMPUTADORA", "HELICOPTERO", "CHOCOLATE", "AVENTURA",
            "MARIPOSA", "UNIVERSIDAD", "TELEFONO", "JIRAFA", "MURCIELAGO",
            "PARAGUAS", "ZAPATO", "VENTANA", "ESPEJO", "LIBERTAD"
    };

    private Random random;

    public WordBank() {
        this.random = new Random();
    }

    public String getRandomWord() {
        int index = random.nextInt(words.length);
        return words[index];
    }

    public int getWordCount() {
        return words.length;
    }
}
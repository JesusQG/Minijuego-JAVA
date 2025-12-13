import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class WordBank {
    private String[] words = {
            "ELEFANTE", "COMPUTADORA", "HELICOPTERO", "CHOCOLATE", "AVENTURA",
            "MARIPOSA", "UNIVERSIDAD", "TELEFONO", "JIRAFA", "MURCIELAGO",
            "PARAGUAS", "ZAPATO", "VENTANA", "ESPEJO", "LIBERTAD",
            "PROGRAMACION", "AHORCADO", "BICICLETA", "TELEVISOR", "ELEVADOR",
            "GUITARRA", "OCEANO", "MONTANA", "ESPACIO", "GALAXIA",
            "PYTHON", "JAVASCRIPT", "HAMBURGUESA", "PIZZA", "HELADO",
            "DINOSAURIO", "EXPLORADOR", "MISTERIO", "TESORO", "DRAGON"
    };

    private Random random;

    public WordBank() {
        this.random = new Random();
    }

    public String getRandomWord() {
        int index = random.nextInt(words.length);
        return words[index];
    }

    public String getRandomWord(int minLength, int maxLength) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() >= minLength && word.length() <= maxLength) {
                filteredWords.add(word);
            }
        }

        if (filteredWords.isEmpty()) {
            return getRandomWord();
        }

        int index = random.nextInt(filteredWords.size());
        return filteredWords.get(index);
    }

    public int getWordCount() {
        return words.length;
    }
}
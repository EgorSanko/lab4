import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordFrequencyAnalysis {
    private static List<String> getWords(String filename) throws IOException {
        Pattern splitter = Pattern.compile("[\\p{Punct}\\d\\s«…»–]+");
        return Files.lines(Path.of(filename))
                .flatMap(splitter::splitAsStream)
                .map(String::toLowerCase)
                .filter(w -> !w.isEmpty())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
        List<String> words = getWords("C:/Users/egor3.DESKTOP-LHVEJ07/OneDrive/Рабочий стол/ucheba/отчеты по джаве/lenin.txt");

        int totalChars = words.stream().mapToInt(String::length).sum();
        System.out.println("Всего символов, использованных в словах: " + totalChars);

        // Фильтрация слов от 4 до 7 букв
        words = words.stream().filter(w -> w.length() >= 4 && w.length() <= 7).collect(Collectors.toList());

        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        // Сортировка частоты слов в лексикографическом порядке
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordFrequency.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());


        writeResultToFile("слово_частоты.txt", sortedEntries);
    }

    private static void writeResultToFile(String filename, List<Map.Entry<String, Integer>> sortedEntries) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            lines.add(entry.getKey() + ": " + entry.getValue());
        }
        Files.write(Path.of(filename), lines);
    }
}

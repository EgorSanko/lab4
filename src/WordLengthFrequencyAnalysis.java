import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordLengthFrequencyAnalysis {
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

        Map<Integer, Integer> lengthFrequency = new HashMap<>();
        for (String word : words) {
            int length = word.length();
            lengthFrequency.put(length, lengthFrequency.getOrDefault(length, 0) + 1);
        }

        writeResultToFile("length_frequencies.txt", lengthFrequency);
    }

    private static void writeResultToFile(String filename, Map<Integer, Integer> lengthFrequencies) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : lengthFrequencies.entrySet()) {
            lines.add("Length " + entry.getKey() + ": " + entry.getValue() + " times");
        }
        Files.write(Path.of(filename), lines);
    }
}

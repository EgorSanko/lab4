import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.function.Function;

public class TextAnalysis {
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

        Map<Character, Long> charFrequency = analyzeCharFrequency(words);
        writeMapToFile("частоты символов.txt", charFrequency);

        Map<String, Long> wordFrequency = analyzeWordFrequency(words);
        writeMapToFile("слово_частоты.txt", wordFrequency);

        Map<Integer, Long> wordLengths = analyzeWordLengths(words);
        writeMapToFile("длина слова.txt", wordLengths);

        Map<Integer, List<String>> topWords = topWordsByLength(words, 15);
        writeMapToFile("верхние_слова_ по_длинности.txt", topWords);
    }

    private static Map<Character, Long> analyzeCharFrequency(List<String> words) {
        return words.stream()
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static Map<String, Long> analyzeWordFrequency(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static Map<Integer, Long> analyzeWordLengths(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
    }

    private static Map<Integer, List<String>> topWordsByLength(List<String> words, int topN) {
        return words.stream()
                .collect(Collectors.groupingBy(String::length))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                .entrySet().stream()
                                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                                .limit(topN)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList())
                ));
    }

    private static void writeMapToFile(String filename, Map<?, ?> map) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            lines.add(entry.getKey() + ": " + entry.getValue());
        }
        Files.write(Path.of(filename), lines);
    }
}

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.function.Function;

public class LeninTextAnalysis {
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


        // Анализ частоты слов
        Map<String, Long> wordFrequency = words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        writeMapToFile("word_frequencies.txt", wordFrequency);

        // Анализ частоты символов
        Map<Character, Long> charFrequency = words.stream()
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        writeMapToFile("char_frequencies.txt", charFrequency);

// Сортировка и преобразование результатов слов для записи в файл
        List<String> sortedWordFrequencies = wordFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.toList());

// Сортировка и преобразование результатов символов для записи в файл
        List<String> sortedCharFrequencies = charFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.toList());
    }

    // Дополнительные методы для анализа и записи результатов
    private static Map<Integer, Long> analyzeWordLengths(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
    }

    //Метод для Поиска Самых Частых Слов Определенной Длины:
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

    //Метод для Записи Результатов:
    private static void writeMapToFile(String filename, Map<?, ?> map) throws IOException {
        List<String> lines = map.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.toList());
        Files.write(Path.of(filename), lines);


    }
}


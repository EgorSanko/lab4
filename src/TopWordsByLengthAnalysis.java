import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TopWordsByLengthAnalysis {
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

        Map<Integer, Map<String, Integer>> lengthToWords = new HashMap<>();
        for (String word : words) {
            int length = word.length();
            lengthToWords.putIfAbsent(length, new HashMap<>());
            Map<String, Integer> wordMap = lengthToWords.get(length);
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }

        Map<Integer, List<String>> top15WordsByLength = new HashMap<>();
        for (Integer length : lengthToWords.keySet()) {
            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(lengthToWords.get(length).entrySet());
            sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            List<String> top15Words = sortedList.stream()
                    .limit(15)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            top15WordsByLength.put(length, top15Words);
        }

        writeResultToFile("верхние_слова_ по_длинности.txt", top15WordsByLength);
    }

    private static void writeResultToFile(String filename, Map<Integer, List<String>> data) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
            lines.add("Length " + entry.getKey() + ": " + String.join(", ", entry.getValue()));
        }
        Files.write(Path.of(filename), lines);
    }
}

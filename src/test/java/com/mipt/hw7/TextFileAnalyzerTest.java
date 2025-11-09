package com.mipt.hw7;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.mipt.hw7.TextFileAnalyzer.AnalysisResult;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextFileAnalyzerTest {

  @Test
  void testAnalyzeFile(@TempDir Path tempDir) throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    // Создаем временный тестовый файл
    Path testFile = tempDir.resolve("input.txt");
    Files.write(testFile, List.of("Hello world!", "This is test."));

    AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount(), "Должно быть 2 строки");
    assertEquals(5, result.getWordCount(), "Должно быть 5 слов: Hello, world!, This, is, test.");
    assertEquals(25, result.getCharCount(), "Длина строк: 12 + 13 = 25");

    Map<Character, Long> freq = result.getCharFrequency();
    assertEquals(3L, freq.get('l'), "Буква 'l' должна встречаться 3 раза");
    assertEquals(1L, freq.get('H'), "Буква 'H' должна встречаться 1 раз");
    assertTrue(freq.containsKey(' '), "Пробел должен быть в частотах");
  }

  @Test
  void testSaveAnalysisResult(@TempDir Path tempDir) throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    // Создаем тестовый результат
    Map<Character, Long> freq = new HashMap<>();
    freq.put('a', 3L);
    freq.put('b', 1L);
    AnalysisResult result = new AnalysisResult(2, 5, 20, freq);

    // Сохраняем в файл
    Path outputFile = tempDir.resolve("analysis_result.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    // Проверяем, что файл создан и его размер > 0
    assertTrue(Files.exists(outputFile), "Файл должен существовать");
    assertTrue(Files.size(outputFile) > 0, "Размер файла должен быть больше 0");

    // Читаем файл и проверяем содержимое
    String content = Files.readString(outputFile);
    assertTrue(content.contains("Lines: 2"), "Должно содержать количество строк 2");
    assertTrue(content.contains("Words: 5"), "Должно содержать количество слов 5");
    assertTrue(content.contains("Chars: 20"), "Должно содержать количество символов 20");
    assertTrue(content.contains("'a': 3"), "Должно содержать частоту символа 'a': 3");
  }
}

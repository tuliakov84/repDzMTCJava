package com.mipt.hw7;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Long> charFrequency;

    public AnalysisResult(long lineCount, long wordCount, long charCount, Map<Character, Long> charFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
      this.charFrequency = new HashMap<>(charFrequency);
    }

    public long getLineCount() {
      return lineCount;
    }

    public long getWordCount() {
      return wordCount;
    }

    public long getCharCount() {
      return charCount;
    }

    public Map<Character, Long> getCharFrequency() {
      return new HashMap<>(charFrequency);
    }

    @Override
    public String toString() {
      String result = "Lines: " + lineCount + "\n";
      result += "Words: " + wordCount + "\n";
      result += "Chars: " + charCount + "\n";
      result += "Character Frequency:\n";
      for (Map.Entry<Character, Long> entry : charFrequency.entrySet()) {
        char ch = entry.getKey();
        long count = entry.getValue();
        result += "'" + ch + "': " + count + "\n";
      }

      return result;
    }
  }

  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Long> charFrequency = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length();

        String[] words = line.split("\\s+");
        for (String word : words) {
          if (!word.isEmpty()) {
            wordCount++;
          }
        }

        for (char c : line.toCharArray()) {
          charFrequency.merge(c, 1L, Long::sum);
        }
      }
    } catch (IOException e) {
      System.err.println("Ошибка чтения файла: " + e.getMessage());
    }

    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }

  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
      writer.write(result.toString());
    } catch (IOException e) {
      System.err.println("Ошибка записи файла: " + e.getMessage());
    }
  }
}

package com.mipt.hw7;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

  /**
   * Разбивает файл на части указанного размера.
   *
   * @param sourcePath путь к исходному файлу
   * @param outputDir  директория для сохранения частей
   * @param partSize   размер каждой части в байтах
   * @return список путей к созданным частям
   */
  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    Path source = Paths.get(sourcePath);
    Path outputDirectory = Paths.get(outputDir);

    // Создаём директорию, если её нет
    Files.createDirectories(outputDirectory);

    // Получаем имя файла без расширения
    String fileName = source.getFileName().toString();
    String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

    List<Path> partPaths = new ArrayList<>();

    try (FileChannel sourceChannel = FileChannel.open(source)) {
      long fileSize = sourceChannel.size();
      long position = 0;
      int partNumber = 1;

      ByteBuffer buffer = ByteBuffer.allocate(partSize);

      while (position < fileSize) {
        // Определяем размер текущей части
        int bytesToRead = (int) Math.min(partSize, fileSize - position);

        // Создаём файл для части
        String partFileName = baseName + ".part" + partNumber;
        Path partPath = outputDirectory.resolve(partFileName);
        partPaths.add(partPath);

        try (FileChannel partChannel = FileChannel.open(
            partPath,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING)) {

          // Читаем данные из исходного файла
          sourceChannel.position(position);
          int bytesRead = sourceChannel.read(buffer);
          if (bytesRead == -1) break;

          // Переводим буфер в режим чтения
          buffer.flip();

          // Записываем в часть
          partChannel.write(buffer);

          buffer.clear();
        }

        position += bytesToRead;
        partNumber++;
      }
    }

    return partPaths;
  }

  /**
   * Объединяет части файла обратно в один файл.
   *
   * @param partPaths  список путей к частям файла (в правильном порядке)
   * @param outputPath путь для результирующего файла
   */
  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    // Проверяем, что все части существуют
    for (Path part : partPaths) {
      if (!Files.exists(part)) {
        throw new IOException("Часть файла не существует: " + part);
      }
    }

    // Создаём выходной файл
    Path output = Paths.get(outputPath);
    Files.createDirectories(output.getParent()); // создаём родительскую папку

    try (FileChannel outputChannel = FileChannel.open(
        output,
        StandardOpenOption.CREATE,
        StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING)) {

      for (Path partPath : partPaths) {
        try (FileChannel partChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
          // Копируем данные из части в выходной файл
          partChannel.transferTo(0, partChannel.size(), outputChannel);
        }
      }
    }
  }
}

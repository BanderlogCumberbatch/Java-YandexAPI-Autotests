package org.helpers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Random;

/**
 * Класс для работы с файлами на устройстве.
 */
public class FilesHelper {
    /**
     * Длинна ключа в байтах.
     */
    private static final int KEY_LENGTH = 32;

    /**
     * Экземпляр Random, для генерации ключа.
     */
    private static final Random random = new Random();

    /**
     * Генерация случайного ключа фиксированной длины в формате Base64.
     * @return Сгенерированный ключ
     */
    private static String generateSecretKey() {
        byte[] bytes = new byte[KEY_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Генерация файла с данными.
     * @param filename Имя файла
     */
    public static void createDataFile(String filename) {
        String secretKey = generateSecretKey();

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("username=SDET\n");
            writer.write("password=" + secretKey + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Скачивание файла на устройство, используя URL.
     * @param fileUrl URL для скачивания
     * @param outputFileName название/путь скачанного файла
     */
    public static void downloadFile(String fileUrl, String outputFileName) {
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, Paths.get(outputFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

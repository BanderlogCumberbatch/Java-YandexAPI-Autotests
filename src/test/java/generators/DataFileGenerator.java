package generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * Класс для генерации тестового файла с данными
 */
public class DataFileGenerator {
    private static final int KEY_LENGTH = 32;
    private static final Random random = new Random();

    /**
     * Генерация случайного ключа фиксированной длины в формате Base64
     * @return Сгенерированный ключ
     */
    private static String generateSecretKey() {
        byte[] bytes = new byte[KEY_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Генерация файла с данными
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

}
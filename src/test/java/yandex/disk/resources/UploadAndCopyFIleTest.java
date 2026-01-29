package yandex.disk.resources;

import generators.DataFileGenerator;
import org.helpers.BaseRequests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import yandex.BaseTest;

import java.io.File;
import java.util.List;

/**
 * Класс тестов загрузки и копирования файлов Yandex Disc API.
 */
public final class UploadAndCopyFIleTest extends BaseTest {

    /**
     * Папка для загрузки файла.
     */
    private final String INPUT_FOLDER = "input_data/";

    /**
     * Папка для копирования файла.
     */
    private final String OUTPUT_FOLDER = "output_data/";

    /**
     * Имя генерируемого файла.
     */
    private final String FILE_NAME = "data.txt";

    /**
     * Создание папок для тестов.
     */
    @BeforeClass
    public void createFolders() {
        BaseRequests.createFolder(INPUT_FOLDER);
        BaseRequests.createFolder(OUTPUT_FOLDER);
    }

    /**
     * Тест загрузки и копирования файла.
     */
    @Test(description = "Upload and copy file")
    public void uploadAndCopyFileTest() {
        String diskPath = INPUT_FOLDER + FILE_NAME;
        DataFileGenerator.createDataFile(FILE_NAME);
        BaseRequests.uploadFile(FILE_NAME, diskPath, 201);
    }

    /**
     * Уборка после класса тестов.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(List.of(INPUT_FOLDER, OUTPUT_FOLDER));
        new File(FILE_NAME).delete();
    }
}

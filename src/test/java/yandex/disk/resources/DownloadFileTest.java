package yandex.disk.resources;

import org.helpers.FilesHelper;
import org.helpers.BaseRequests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import yandex.BaseTest;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Класс теста скачивания файлов на устройство Yandex Disc API.
 */
public final class DownloadFileTest extends BaseTest {

    /**
     * Имя создаваемой для теста папки
     */
    String folderName;

    /**
     * Имя создаваемого для теста файла на диске
     */
    String fileName;

    /**
     * Путь до создаваемого для теста файла на диске
     */
    String diskPath;

    /**
     * Имя загружаемого с диска тестового файла на устройстве
     */
    String downloadedFileName;

    /**
     * Предусловия для теста.
     */
    @BeforeClass
    public void preconditions() {
        folderName = "sdet_data";
        fileName = "data.txt";
        diskPath = folderName + "/" + fileName;
        BaseRequests.createFolder(folderName);

        FilesHelper.createDataFile(fileName);

        BaseRequests.uploadFile(fileName, diskPath);
    }

    /**
     * Тест скачивания файла с диска и его сравнения с оригинальным файлом, загруженным на диск.
     */
    @Test(description = "Download file")
    public void downloadFileTest() {
        String downloadUrl = BaseRequests.getDownloadUrl(diskPath, 200)
                .getHref();

        downloadedFileName = "downloaded_data.txt";
        FilesHelper.downloadFile(downloadUrl, downloadedFileName);

        long l;
        try {
            l = Files.mismatch(Path.of(fileName), Path.of(downloadedFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(l, -1L, "Содержание скачанного и оригинального файла не совпадает");
    }

    /**
     * Уборка после класса тестов.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(List.of(folderName));
        new File(fileName).delete();
        new File(downloadedFileName).delete();
    }
}

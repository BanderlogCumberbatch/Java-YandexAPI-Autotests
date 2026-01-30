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
     * Предусловия для теста.
     */
    @BeforeClass
    public void preconditions() {
        String folderName = "sdet_data/";
        String fileName = "data.txt";
        String diskPath = folderName + fileName;
        BaseRequests.createFolder(folderName);

        FilesHelper.createDataFile(fileName);

        BaseRequests.uploadFile(fileName, diskPath, 201);
    }

    /**
     * Тест скачивания файла с диска и его сравнения с оригинальным файлом, загруженным на диск.
     */
    @Test(description = "Download file test")
    public void downloadFileTest() {
        String fileName = "data.txt";
        String diskPath = "sdet_data/" + fileName;
        String downloadUrl = BaseRequests.getDownloadUrl(diskPath, 200)
                .getHref();

        String downloadedFileName = "downloaded_data.txt";
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
        BaseRequests.clearFolders(List.of("sdet_data/"));
        new File("data.txt").delete();
        new File("downloaded_data.txt").delete();
    }
}

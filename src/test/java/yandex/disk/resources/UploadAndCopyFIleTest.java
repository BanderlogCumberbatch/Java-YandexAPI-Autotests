package yandex.disk.resources;

import org.helpers.FilesHelper;
import org.helpers.BaseRequests;
import org.pojo.CopyRequest;
import org.pojo.ErrorResponse;
import org.pojo.SuccessResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import yandex.BaseTest;

import java.io.File;
import java.util.List;

/**
 * Класс теста загрузки и копирования файлов Yandex Disc API.
 */
public final class UploadAndCopyFIleTest extends BaseTest {

    /**
     * Создание папок для тестов.
     */
    @BeforeClass
    public void createFolders() {
        BaseRequests.createFolder("input_data/");
        BaseRequests.createFolder("output_data/");
    }

    /**
     * Тест загрузки и копирования файла.
     */
    @Test(description = "Upload and copy file")
    public void uploadAndCopyFileTest() {
        String fileName = "data.txt";
        String diskPath = "input_data/" + fileName;
        FilesHelper.createDataFile(fileName);

        BaseRequests.uploadFile(fileName, diskPath, 201);

        String copyPath = "output_data/" + fileName;
        CopyRequest copyRequest = CopyRequest.builder()
                .from(diskPath)
                .path(copyPath)
                .build();
        SuccessResponse successResponse = BaseRequests
                .copyFile(copyRequest, 201)
                .as(SuccessResponse.class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(successResponse.getMethod(), "Тело ответа не содержит method");
        softAssert.assertNotNull(successResponse.getHref(), "Тело ответа не содержит href");
        softAssert.assertNotNull(successResponse.getTemplated(), "Тело ответа не содержит templated");
        softAssert.assertAll();

        ErrorResponse errorResponse = BaseRequests
                .copyFile(copyRequest, 409)
                .as(ErrorResponse.class);
        softAssert.assertNotNull(errorResponse.getError(), "Тело ответа не содержит error");
        softAssert.assertNotNull(errorResponse.getDescription(), "Тело ответа не содержит description");
        softAssert.assertNotNull(errorResponse.getMessage(), "Тело ответа не содержит message");
        softAssert.assertAll();

    }

    /**
     * Уборка после класса тестов.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(List.of("input_data/", "output_data/"));
        new File("data.txt").delete();
    }
}

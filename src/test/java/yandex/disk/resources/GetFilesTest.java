package yandex.disk.resources;

import io.restassured.response.ValidatableResponse;
import org.helpers.BaseRequests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Класс теста получения списка файлов
 */
public class GetFilesTest extends yandex.BaseTest {

    /**
     * Имя создаваемой для теста папки
     */
    String folderName;

    /**
     * Предусловия для теста.
     */
    @BeforeClass
    public void preconditions() {
        folderName = "files";
        String diskPath = folderName + "/file.txt";
        BaseRequests.createFolder(folderName);
        BaseRequests.uploadFile(diskPath);
    }

    /**
     * Тест получения списка файлов и проверки его JSON схемы.
     */
    @Test(description = "Get files list")
    public void getFilesTest() {
        ValidatableResponse validResp = BaseRequests.getFiles(200);
        validResp
                .body(matchesJsonSchemaInClasspath("get_files_schema.json"));
    }

    /**
     * Уборка после класса тестов.
     */
    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(List.of(folderName));
    }
}

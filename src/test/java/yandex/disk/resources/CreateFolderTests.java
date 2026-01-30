package yandex.disk.resources;

import io.restassured.response.ValidatableResponse;
import org.helpers.BaseRequests;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterClass;
import yandex.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;

/**
 * Класс тестов создания папки Yandex Disc API.
 */
public final class CreateFolderTests extends BaseTest {

    @DataProvider(name = "Folder names")
    public Object[][] dpMethod() {
        return new Object[][]{
                {".folder"},
                {"Папка"},
                {123},
                {"\"456\""},
                {"'789'"},
                {"ё.txt"},
                {"*?<>|\\"},
                {"~!@#$%^-+*;№)("},
                {"\uD83D\uDD35"},
        };
    }

    /**
     * Список путей папок для уборки.
     */
    List<String> foldersOnDelete = new ArrayList<>();

    /**
     * Тест создания папки.
     */
    @Test(description = "Create folder", dataProvider = "Folder names")
    public void createFolderTest(Object folderName) {
        foldersOnDelete.add(folderName.toString());

        ValidatableResponse validResp = BaseRequests.createFolder(folderName, 201);
        validResp
                .body("method", instanceOf(String.class),
                        "href", instanceOf(String.class),
                        "templated", instanceOf(Boolean.class));
    }

    /**
     * Тест создания папки без Oauth токена.
     */
    @Test(description = "Create folder without Oauth token", priority = 1)
    public void createFolderWithoutAuthTest() {
        BaseRequests.createFolderWithoutAuth();
    }

    /**
     * Тест создания уже существующей папки.
     */
    @Test(description = "Create existing folder", priority = 2)
    public void createExistingFolderTest() {
        String folderName = "folder";
        foldersOnDelete.add(folderName);
        BaseRequests.createFolder(folderName, 201);

        ValidatableResponse validResp = BaseRequests.createFolder(folderName, 409);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Создание папки с несуществующим path в запросе.
     */
    @Test(description = "Create non existing folder", priority = 3)
    public void createNotFolderTest() {
        String folderName = "/";
        ValidatableResponse validResp = BaseRequests.createFolder(folderName, 409);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Создание папки с некорректным path в запросе.
     */
    @Test(description = "Create incorrect folder", priority = 4)
    public void createIncorrectFolderTest() {
        String folderName = "/:";
        ValidatableResponse validResp = BaseRequests.createFolder(folderName, 400);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Создание папки без path в запросе.
     */
    @Test(description = "Create folder without path", priority = 5)
    public void createFolderWithoutPathTest() {
        ValidatableResponse validResp = BaseRequests.createFolder(400);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Уборка после класса тестов.
     */
    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(foldersOnDelete);
    }
}

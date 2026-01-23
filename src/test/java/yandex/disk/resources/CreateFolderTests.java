package yandex.disk.resources;

import org.helpers.BaseRequests;
import org.testng.annotations.*;
import yandex.BaseTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс негативных тестов создания папки YandexAPI
 */
public final class CreateFolderTests extends BaseTest {

    private final String DEFAULT_FOLDER = "folder";

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

    List<String> foldersOnDelete = new ArrayList<>(List.of(DEFAULT_FOLDER));

    /**
     * Тест создания папки.
     */
    @Test(description = "Create folder", dataProvider = "Folder names")
    public void createFolderTest(Object folderName) {
        foldersOnDelete.add(folderName.toString());
        BaseRequests.createFolder(folderName, 201);
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
        BaseRequests.createFolder(DEFAULT_FOLDER, 201);
        BaseRequests.createFolder(DEFAULT_FOLDER, 409);
    }

    /**
     * Создание папки с несуществующим path в запросе.
     */
    @Test(description = "Create non existing folder", priority = 3)
    public void createNotFolderTest() {
        String folderName = "/";
        BaseRequests.createFolder(folderName, 409);
    }

    /**
     * Создание папки с некорректным path в запросе.
     */
    @Test(description = "Create incorrect folder", priority = 4)
    public void createIncorrectFolderTest() {
        String folderName = "/:";
        BaseRequests.createFolder(folderName, 400);
    }

    /**
     * Создание папки без path в запросе.
     */
    @Test(description = "Create folder without path", priority = 5)
    public void createFolderWithoutPathTest() {
        BaseRequests.createFolder();
    }

    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(foldersOnDelete);
        BaseRequests.clearTrash();
    }
}

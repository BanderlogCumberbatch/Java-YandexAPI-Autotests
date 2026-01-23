package yandex.disk.resources;

import org.helpers.BaseRequests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import yandex.BaseTest;

import java.util.List;

/**
 * Класс негативных тестов удаления папки
 */
public final class DeleteFolderTests extends BaseTest {

    private final String DEFAULT_FOLDER = "folder";

    /**
     * Тест удаления папки без Oauth токена.
     */
    @Test(description = "Delete folder")
    public void deleteFolder() {
        BaseRequests.createFolder(DEFAULT_FOLDER, 201);
        BaseRequests.deleteFolderWithoutAuth();
    }

    /**
     * Тест удаления папки без Oauth токена.
     */
    @Test(description = "Delete folder without Oauth token", priority = 1)
    public void deleteFolderWithoutAuthTest() {
        BaseRequests.deleteFolderWithoutAuth();
    }

    /**
     * Удаление несуществующей папки.
     */
    @Test(description = "Delete non existing folder", priority = 2)
    public void deleteNonExistingFolderTest() {
        String folderName = "folder0";
        BaseRequests.deleteFolder(folderName, 404);
    }

    /**
     * Удаление папки без path в запросе.
     */
    @Test(description = "Delete folder without path", priority = 3)
    public void deleteFolderWithoutPathTest() {
        BaseRequests.deleteFolder();
    }

    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(List.of(DEFAULT_FOLDER));
        BaseRequests.clearTrash();
    }
}

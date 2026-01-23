package yandex.disk.trash;

import org.helpers.BaseRequests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import yandex.BaseTest;

import java.util.ArrayList;
import java.util.List;

public final class RestoreFolderTests extends BaseTest {

    List<String> foldersOnDelete = new ArrayList<>();

    /**
     * Тест восстановления папки.
     */
    @Test(description = "Restore folder")
    public void restoreFolder() {
        String folderName = "folder0";
        foldersOnDelete.add(folderName);
        BaseRequests.createFolder(folderName, 201);
        BaseRequests.deleteFolder(folderName, 204, false);
        BaseRequests.restoreFolder(folderName, 201);
    }

    /**
     * Тест восстановления уже существующей папки.
     */
    @Test(description = "Restore existing folder", priority = 1)
    public void restoreExistingFolderTest() {
        String folderName = "folder1", restoredFolderName = "folder1 (1)";
        foldersOnDelete.add(folderName);
        foldersOnDelete.add(restoredFolderName);
        BaseRequests.createFolder(folderName, 201);
        BaseRequests.deleteFolder(folderName, 204, false);
        BaseRequests.createFolder(folderName, 201);
        BaseRequests.restoreFolder(folderName, 201);
    }

    /**
     * Тест восстановления папки без Oauth токена.
     */
    @Test(description = "Restore folder without Oauth token", priority = 2)
    public void restoreFolderWithoutAuthTest() {
        BaseRequests.restoreFolderWithoutAuth();
    }

    /**
     * Восстановление несуществующей в корзине папки.
     */
    @Test(description = "Restore non existing folder", priority = 3)
    public void restoreNonExistingFolderTest() {
        String folderName = "folder";
        BaseRequests.restoreFolderFromTrash(folderName, 404);
    }

    /**
     * Восстановление папки без path в запросе.
     */
    @Test(description = "Restore folder without path", priority = 4)
    public void restoreFolderWithoutPathTest() {
        BaseRequests.restoreFolderFromTrash();
    }

    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(foldersOnDelete);
        BaseRequests.clearTrash();
    }

}

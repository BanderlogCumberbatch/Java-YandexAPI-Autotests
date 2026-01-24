package yandex.disk.trash;

import io.restassured.response.ValidatableResponse;
import org.helpers.BaseRequests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import yandex.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;

/**
 * Класс тестов восстановления из корзины Yandex Disc API.
 */
public final class RestoreFolderTests extends BaseTest {

    /**
     * Список путей папок для уборки.
     */
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

        ValidatableResponse validResp = BaseRequests.restoreFolder(folderName, 201);
        validResp
                .body("method", instanceOf(String.class),
                        "href", instanceOf(String.class),
                        "templated", instanceOf(Boolean.class));

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

        ValidatableResponse validResp = BaseRequests.restoreFolder(folderName, 201);
        validResp
                .body("method", instanceOf(String.class),
                        "href", instanceOf(String.class),
                        "templated", instanceOf(Boolean.class));
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
        ValidatableResponse validResp = BaseRequests.restoreFolderFromTrash(folderName, 404);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Восстановление папки без path в запросе.
     */
    @Test(description = "Restore folder without path", priority = 4)
    public void restoreFolderWithoutPathTest() {
        ValidatableResponse validResp = BaseRequests.restoreFolderFromTrash(400);
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
        BaseRequests.clearTrash();
    }

}

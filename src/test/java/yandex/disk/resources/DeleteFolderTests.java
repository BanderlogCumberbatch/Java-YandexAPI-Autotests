package yandex.disk.resources;

import io.restassured.response.ValidatableResponse;
import org.helpers.BaseRequests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import yandex.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Класс тестов удаления папки Yandex Disc API.
 */
public final class DeleteFolderTests extends BaseTest {

    /**
     * Список путей папок для уборки.
     */
    List<String> foldersOnDelete = new ArrayList<>();

    /**
     * Тест удаления папки без Oauth токена.
     */
    @Test(description = "Delete folder")
    public void deleteFolder() {
        String folderName = "folder";
        foldersOnDelete.add(folderName);
        BaseRequests.createFolder(folderName, 201);

        ValidatableResponse validResp = BaseRequests.deleteFolder(folderName, 204);
        validResp.body(emptyOrNullString());
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

        ValidatableResponse validResp = BaseRequests
                .deleteFolder(folderName, 404);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Удаление папки без path в запросе.
     */
    @Test(description = "Delete folder without path", priority = 3)
    public void deleteFolderWithoutPathTest() {
        ValidatableResponse validResp = BaseRequests.deleteFolder(400);
        validResp
                .body("error", instanceOf(String.class),
                        "description", instanceOf(String.class),
                        "message", instanceOf(String.class));
    }

    /**
     * Уборка после тестов класса.
     */
    @AfterClass
    public void cleaning() {
        BaseRequests.clearFolders(foldersOnDelete);
    }
}

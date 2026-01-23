package yandex.disk;

import org.helpers.BaseRequests;
import org.testng.annotations.Test;
import yandex.BaseTest;

/**
 * Класс для тестов авторизации Yandex Disc API.
 */
public final class AuthTests extends BaseTest {

    /**
     * Тест авторизации YandexAPI с валидным токеном.
     */
    @Test(description = "YandexAPI correct auth test", priority = 1)
    public void authCorrectTest() {
        BaseRequests.authYD(
                secretsProvider.getProperty("yd.login"),
                secretsProvider.getProperty("yd.display.name"));
    }

    /**
     * Тест авторизации YandexAPI без передачи токена.
     */
    @Test(description = "YandexAPI incorrect auth test", priority = 2)
    public void authIncorrectTest() {
        BaseRequests.authYD();
    }
}

import org.helpers.BaseRequests;
import org.helpers.PropertyProvider;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Класс для тестов авторизации YandexAPI
 */
public class AuthTests {

    /**
     * Экземпляр PropertyProvider с загруженными локальными параметрами
     */
    private final PropertyProvider envLocalProvider = new PropertyProvider();

    /**
     * Экземпляр PropertyProvider с загруженными секретами
     */
    private final PropertyProvider secretsProvider = new PropertyProvider("secrets.properties");

    @BeforeTest
    public void initSpec() {
        BaseRequests.initRequestSpecification(
                envLocalProvider.getProperty("base.url"));
    }

    /**
     * Тест авторизации YandexAPI с валидным токеном
     */
    @Test(description = "YandexAPI correct auth test", priority = 1)
    public void authCorrectTest() {
        BaseRequests.authYD(
                secretsProvider.getProperty("oauth.token"),
                secretsProvider.getProperty("yd.login"),
                secretsProvider.getProperty("yd.display.name"));
    }

    /**
     * Тест авторизации YandexAPI без передачи токена
     */
    @Test(description = "YandexAPI incorrect auth test", priority = 2)
    public void authIncorrectTest() {
        BaseRequests.authYD();
    }
}

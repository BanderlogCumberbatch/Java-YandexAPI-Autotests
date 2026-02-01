package yandex;

import org.helpers.BaseRequests;
import org.helpers.PropertyProvider;
import org.testng.annotations.BeforeTest;

/**
 * Базовый класс для тестов Yandex Disc API.
 */
public class BaseTest {

    /**
     * Экземпляр PropertyProvider с загруженными локальными параметрами.
     */
    protected final PropertyProvider envLocalProvider = new PropertyProvider();

    /**
     * Экземпляр PropertyProvider с загруженными секретами.
     */
    protected final PropertyProvider secretsProvider = new PropertyProvider("secrets.properties");

    /**
     * Инициализация спецификации запросов перед тестами.
     */
    @BeforeTest
    public void initSpec() {
        BaseRequests.initRequestSpecification(
                secretsProvider.getProperty("oauth.token"),
                envLocalProvider.getProperty("base.url"));
    }

}

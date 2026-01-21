package org.helpers;

import lombok.Getter;
import java.io.InputStream;
import java.util.Properties;

@Getter
public final class PropertyProvider {

    /**
     * Класс для загрузки properties.
     */
    private final Properties properties = new Properties();

    /**
     * Загрузка .properties-файла.
     * @param propertiesFile имя .properties-файла в папке resources
     */
    public PropertyProvider(final String propertiesFile) {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    /**
     * Загрузка .properties-файла по умолчанию (env_local.properties).
     */
    public PropertyProvider() {
        String propertiesFile = "env_local.properties";
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    /**
     * Загрузить параметр из .properties-файла.
     * @param key ключ, для нахождения параметра
     * @return параметр, найденный по ключу
     */
    public String getProperty(final String key) {
        return properties.getProperty(key);
    }
}

package org.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;
import org.pojo.CopyRequest;
import org.pojo.SuccessResponse;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Класс запросов Yandex Disc API.
 */
public final class BaseRequests {

    private BaseRequests() { }

    /**
     * API для авторизации.
     */
    private static final String YD_AUTH = "v1/disk";

    /**
     * API для взаимодействия с ресурсами.
     */
    private static final String YD_RESOURCES = "v1/disk/resources";

    /**
     * API для загрузки файлов на диск.
     */
    private static final String YD_UPLOAD = "v1/disk/resources/upload";

    /**
     * API для копирования файлов.
     */
    private static final String YD_COPY = "v1/disk/resources/copy";

    /**
     * API для загрузки файлов с диска.
     */
    private static final String YD_DOWNLOAD = "v1/disk/resources/download";

    /**
     * API для взаимодействия с корзиной.
     */
    private static final String YD_TRASH = "v1/disk/trash/resources";

    /**
     * API для восстановления из корзины.
     */
    private static final String YD_RESTORE = "v1/disk/trash/resources/restore";

    /**
     * Oauth токен для авторизации.
     */
    private static String authToken;

    /**
     * Подготовка спецификации запроса.
     * @param authToken Oauth токен
     * @param baseUrl базовый url WordPress
     */
    public static void initRequestSpecification(final String authToken,
                                                final String baseUrl) {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .setAccept(ContentType.JSON);
        requestSpecification = requestSpecBuilder.build();
        BaseRequests.authToken = authToken;
    }

    /**
     * Авторизация с передачей Oauth токена.
     * @param userLogin ожидаемый логин аккаунта Яндекса
     * @param userDisplayName ожидаемое отображаемое имя аккаунта Яндекса
     */
    public static void authYD(final String userLogin,
                              final String userDisplayName) {
        given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .get(YD_AUTH)
                .then()
                    .statusCode(200)
                    .body("user.login", equalTo(userLogin),
                            "user.display_name", equalTo(userDisplayName));
    }

    /**
     * Авторизация без передачи Oauth токена.
     */
    public static void authYD() {
        given()
                .spec(requestSpecification)
                .when()
                    .get(YD_AUTH)
                .then()
                    .statusCode(401)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
    }

    /**
     * Создать папку с проверяемым ответом.
     * @param folderPath путь папки
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse createFolder(final Object folderPath,
                                    final int statusCode) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", folderPath)
                    .put(YD_RESOURCES)
                .then()
                    .statusCode(statusCode);
    }

    /**
     * Создать папку без указания пути.
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse createFolder(final int statusCode) {
        return createFolder("", statusCode);
    }

    /**
     * Создать папку.
     * @param folderPath путь папки
     */
    public static void createFolder(final String folderPath) {
        createFolder(folderPath, 201);
    }

    /**
     * Создать папку без передачи Oauth токена.
     */
    public static void createFolderWithoutAuth() {
        given()
                .spec(requestSpecification)
                .when()
                    .put(YD_RESOURCES)
                .then()
                    .statusCode(401)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
    }

    /**
     * Удалить папку.
     * @param folderPath путь папки
     * @param statusCode ожидаемый статус-код ответа
     * @param permanently true - удалить навсегда, false - переместить в корзину
     * @return проверяемый ответ
     */
    public static ValidatableResponse deleteFolder(final String folderPath,
                                    final int statusCode,
                                    final boolean permanently) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", folderPath)
                    .param("permanently", permanently)
                    .delete(YD_RESOURCES)
                .then()
                    .statusCode(statusCode);
    }

    /**
     * Удалить папку без перемещения в корзину.
     * @param folderPath путь папки
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse deleteFolder(final String folderPath,
                                    final int statusCode) {
        return deleteFolder(folderPath, statusCode, true);
    }

    /**
     * Удалить папку без указания пути.
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse deleteFolder(final int statusCode) {
        return deleteFolder("", statusCode);
    }

    /**
     * Удалить папку без передачи Oauth токена.
     */
    public static void deleteFolderWithoutAuth() {
        given()
                .spec(requestSpecification)
                .when()
                    .put(YD_RESOURCES)
                .then()
                    .statusCode(401)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
    }

    /**
     * Удалить список папок.
     * @param folderList список из путей папок
     */
    public static void clearFolders(final List<String> folderList) {
        for (String i : folderList) {
            given()
                    .spec(requestSpecification)
                    .header(new Header("Authorization", authToken))
                    .when()
                        .param("path", i)
                        .param("permanently", true)
                        .delete(YD_RESOURCES)
                    .then()
                        .statusCode(anyOf(equalTo(204), equalTo(202), equalTo(404)));
        }
    }

    /**
     * Очистить корзину.
     */
    public static void clearTrash() {
        given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .delete(YD_TRASH)
                .then()
                    .statusCode(anyOf(equalTo(204), equalTo(404)));
    }

    /**
     * Восстановить папку из корзины, используя путь в корзине.
     * @param trashPath путь папки в корзине
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse restoreFolderFromTrash(final String trashPath,
                                     final int statusCode) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                .param("path", trashPath)
                .put(YD_RESTORE)
                .then()
                .statusCode(statusCode);
    }

    /**
     * Получить путь к ресурсу в корзине.
     * @param originPath путь к ресурсу до помещения в корзину
     * @return путь к ресурсу в корзине
     */
    public static String getTrashHash(final String originPath) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .get(YD_TRASH)
                .then()
                    .statusCode(200)
                .extract()
                    .jsonPath()
                    .getString("_embedded.items.find{it.origin_path=='disk:/"
                            + originPath + "' }.path");
    }

    /**
     * Восстановить папку из корзины.
     * @param folderPath путь папки до помещения в корзину
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse restoreFolder(final String folderPath,
                                     final int statusCode) {
        String trashHashPath = getTrashHash(folderPath);

        return restoreFolderFromTrash(trashHashPath, statusCode);
    }

    /**
     * Восстановить папку из корзины без указания пути.
     * @param statusCode ожидаемый статус-код ответа
     * @return проверяемый ответ
     */
    public static ValidatableResponse restoreFolderFromTrash(final int statusCode) {
        return restoreFolder("", statusCode);
    }

    /**
     * Восстановить папку из корзины без передачи Oauth токена.
     */
    public static void restoreFolderWithoutAuth() {
        given()
                .spec(requestSpecification)
                .when()
                    .put(YD_RESTORE)
                .then()
                    .statusCode(401)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
    }

    /**
     * Получить тело ответа с PUT запросом для загрузки файла на диск.
     * @param path путь к создаваемому файлу и его имя
     * @return Data-класс с href URL на PUT запрос загрузки файла
     */
    public static SuccessResponse getUploadUrl(String path) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", path)
                    .get(YD_UPLOAD)
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                    .as(SuccessResponse.class);
    }

    /**
     * Загрузить файл на диск.
     * @param filePath путь к загружаемому файлу
     * @param diskPath желаемый путь к файлу в облаке
     * @param statusCode ожидаемый статус-код ответа
     */
    public static void uploadFile(String filePath, String diskPath, int statusCode) {
        String uploadUrl = getUploadUrl(diskPath)
                .getHref();

        given()
                .spec(requestSpecification)
                .contentType(ContentType.BINARY)
                .body(new File(filePath))
                .header(new Header("Authorization", authToken))
                .when()
                    .put(uploadUrl)
                .then()
                    .statusCode(statusCode);
    }

    /**
     * Копировать файл.
     * @param copyRequest Data-класс запроса на копирование
     * @param statusCode ожидаемый статус-код ответа
     * @return опции для сериализации тела ответа
     */
    public static ResponseBodyExtractionOptions copyFile(CopyRequest copyRequest, int statusCode) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .queryParam("from", copyRequest.getFrom())
                .queryParam("path", copyRequest.getPath())
                .queryParam("overwrite", copyRequest.getOverwrite())
                .when()
                    .post(YD_COPY)
                .then()
                    .statusCode(statusCode)
                .extract()
                .body();
    }

    /**
     * Получить телом ответа с URL для загрузки файла с диска.
     * @param path путь к создаваемому файлу и его имя
     * @return Data-класс с href URL для загрузки файла
     */
    public static SuccessResponse getDownloadUrl(String path) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", path)
                    .get(YD_DOWNLOAD)
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                    .as(SuccessResponse.class);
    }
}

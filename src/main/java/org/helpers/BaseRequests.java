package org.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Класс запроса YandexAPI.
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
     * API для взаимодействия с корзиной.
     */
    private static final String YD_TRASH = "v1/disk/trash/resources";

    /**
     * API для восстановления из корзины.
     */
    private static final String YD_RESTORE = "v1/disk/trash/resources/restore";

    /**
     * Oauth токен для авторизации
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
     * Авторизация YandexAPI с передачей Oauth токена.
     * @param userLogin ожидаемый логин аккаунта Яндекса
     * @param userDisplayName ожидаемое отображаемое имя аккаунта Яндекса
     */
    public static void authYD(
                              final String userLogin,
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
     * Авторизация YandexAPI без передачи Oauth токена.
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
     * Создать папку.
     * @param folderPath путь папки
     * @param statusCode ожидаемый статус код
     */
    public static void createFolder(final Object folderPath,
                                    final int statusCode) {
        ValidatableResponse validResp = given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", folderPath)
                    .put(YD_RESOURCES)
                .then()
                    .statusCode(statusCode);

        if (statusCode == 201) {
            validResp
                    .body("method", instanceOf(String.class),
                            "href", instanceOf(String.class),
                            "templated", instanceOf(Boolean.class));
        }
        else {
            validResp
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
        }
    }

    /**
     * Создать папку без указания пути.
     */
    public static void createFolder() {
        createFolder("", 400);
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
     * @param statusCode ожидаемый статус код
     * @param permanently true - удалить навсегда, false - переместить в корзину
     */
    public static void deleteFolder(final String folderPath,
                                    final int statusCode,
                                    final boolean permanently) {
        ValidatableResponse validResp = given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", folderPath)
                    .param("permanently", permanently)
                    .delete(YD_RESOURCES)
                .then()
                    .statusCode(statusCode);

        if (statusCode == 204) {
            validResp
                    .body(emptyOrNullString());
        }
        else if (statusCode == 202) {
            validResp
                    .body("method", instanceOf(String.class),
                            "href", instanceOf(String.class),
                            "templated", instanceOf(Boolean.class));
        }
        else {
            validResp
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
        }
    }

    /**
     * Удалить папку без перемещения в корзину.
     * @param folderPath путь папки
     * @param statusCode ожидаемый статус код
     */
    public static void deleteFolder(final String folderPath,
                                    final int statusCode) {
        deleteFolder(folderPath, statusCode, true);
    }

    /**
     * Удалить папку без указания пути.
     */
    public static void deleteFolder() {
        deleteFolder("", 400);
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
                        .statusCode(anyOf(equalTo(204), equalTo(404)));
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
     * @param statusCode ожидаемый статус код
     */
    public static void restoreFolderFromTrash(final String trashPath,
                                     final int statusCode) {

        ValidatableResponse validResp = given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                .param("path", trashPath)
                .put(YD_RESTORE)
                .then()
                .statusCode(statusCode);

        if (statusCode == 201 || statusCode == 202) {
            validResp
                    .body("method", instanceOf(String.class),
                            "href", instanceOf(String.class),
                            "templated", instanceOf(Boolean.class));
        }
        else {
            validResp
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
        }
    }

    /**
     * Получить путь к ресурсу в корзине.
     * @param originPath путь к ресурсу до помещения в корзину
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
                .getString("_embedded.items.find{it.origin_path=='disk:/" + originPath + "' }.path");
    }

    /**
     * Восстановить папку из корзины.
     * @param folderPath путь папки до помещения в корзину
     * @param statusCode ожидаемый статус код
     */
    public static void restoreFolder(final String folderPath,
                                     final int statusCode) {
        String trashHashPath = getTrashHash(folderPath);

        restoreFolderFromTrash(trashHashPath, statusCode);
    }

    /**
     * Восстановить папку из корзины без указания пути
     */
    public static void restoreFolderFromTrash() {
        restoreFolder("", 400);
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

}

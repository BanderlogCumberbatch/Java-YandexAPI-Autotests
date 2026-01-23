package org.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.instanceOf;
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
    private static final String YD_TRASH = "v1/disk/trash/resources/restore";

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
    public static void createFolder(final String folderPath,
                                    final int statusCode) {
        ValidatableResponse validResp = given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", folderPath)
                    .put(YD_RESOURCES)
                .then();


        if (statusCode == 201) {
            validResp
                    .statusCode(statusCode)
                    .body("method", instanceOf(String.class),
                            "href", instanceOf(String.class),
                            "templated", instanceOf(Boolean.class));
        }
        else {
            validResp
                    .statusCode(statusCode)
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
     * Поместить папку в корзину.
     * @param folderPath путь папки
     * @param statusCode ожидаемый статус код
     */
    public static void deleteFolder(final String folderPath,
                                    final int statusCode) {
        ValidatableResponse validResp = given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", folderPath)
                    .delete(YD_RESOURCES)
                .then();


        if (statusCode == 204 || statusCode == 202) {
            validResp
                    .statusCode(statusCode)
                    .body("method", instanceOf(String.class),
                            "href", instanceOf(String.class),
                            "templated", instanceOf(Boolean.class));
        }
        else {
            validResp
                    .statusCode(statusCode)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
        }
    }

    /**
     * Поместить папку в корзину без указания пути.
     */
    public static void deleteFolder() {
        deleteFolder("", 400);
    }

    /**
     * Поместить папку в корзину без передачи Oauth токена.
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
                    .getString("_embedded.items.find {it.origin_path == disk:/" + originPath + " }.path");
    }

    /**
     * Восстановить папку из корзины.
     * @param folderPath путь папки до помещения в корзину
     * @param statusCode ожидаемый статус код
     */
    public static void restoreFolder(final String folderPath,
                                    final int statusCode) {
        String trashHashPath = getTrashHash(folderPath);

        ValidatableResponse validResp = given()
                .spec(requestSpecification)
                .header(new Header("Authorization", authToken))
                .when()
                    .param("path", trashHashPath)
                    .put(YD_TRASH)
                .then();


        if (statusCode == 201 || statusCode == 202) {
            validResp
                    .statusCode(statusCode)
                    .body("method", instanceOf(String.class),
                            "href", instanceOf(String.class),
                            "templated", instanceOf(Boolean.class));
        }
        else {
            validResp
                    .statusCode(statusCode)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
        }
    }

    /**
     * Восстановить папку из корзины без указания пути
     */
    public static void restoreFolder() {
        restoreFolder("", 400);
    }

    /**
     * Восстановить папку из корзины без передачи Oauth токена.
     */
    public static void restoreFolderWithoutAuth() {
        given()
                .spec(requestSpecification)
                .when()
                    .put(YD_TRASH)
                .then()
                    .statusCode(401)
                    .body("error", instanceOf(String.class),
                            "description", instanceOf(String.class),
                            "message", instanceOf(String.class));
    }

}

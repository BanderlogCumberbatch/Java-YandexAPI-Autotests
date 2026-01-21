package org.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.notNullValue;
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
     * Подготовка спецификации запроса.
     * @param baseUrl базовый url WordPress
     */
    public static void initRequestSpecification(final String baseUrl) {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .setAccept(ContentType.JSON);
        requestSpecification = requestSpecBuilder.build();
    }

    /**
     * Авторизация YandexAPI с передачей Oauth токена.
     * @param authToken Oauth токен
     * @param userLogin ожидаемый логин аккаунта Яндекса
     * @param userDisplayName ожидаемое отображаемое имя аккаунта Яндекса
     */
    public static void authYD(final String authToken,
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
     * Авторизация YandexAPI без передачи токена.
     */
    public static void authYD() {
        given()
                .spec(requestSpecification)
                .when()
                    .get(YD_AUTH)
                .then()
                    .statusCode(401)
                    .body("error", notNullValue(),
                            "description", notNullValue(),
                            "message", notNullValue());
    }
}

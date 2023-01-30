import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(UserInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when() // "когда"
                .post("/api/system/users")
                .then() // "тогда ожидаем"
                .statusCode(200);
    }

    private DataGenerator() {
    }

    private static Faker faker = new Faker(new Locale("en"));

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }


    public static UserInfo getUser(String status) {
        var user = new UserInfo(generateLogin(), generatePassword(), status);
        return user;
    }

    public static UserInfo getRegistrationUser(String status) {
        var registeredUser = getUser(status);
        sendRequest(registeredUser);
        return registeredUser;
    }


    @Value
    public static class UserInfo {
        private final String login;
        private final String password;
        private final String status;
    }
}
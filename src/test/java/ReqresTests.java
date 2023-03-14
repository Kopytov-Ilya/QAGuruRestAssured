import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ReqresTests {

    @Test
    @DisplayName("User created successfully")
    void createUserTest() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    @DisplayName("Resource not found")
    void resourceNotFoundTest() {

        given()
                .log().all()
                .when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("Successful login and get token")
    void successfulLoginAndGetTokenTest() {

        String credentials = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .log().all()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", not(empty()));
    }

    @Test
    @DisplayName("Delete user")
    void deletingUser() {

        given()
                .log().all()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .statusCode(204);
    }


    @Test
    @DisplayName("Unsuccessful login")
    void loginUnsuccessful() {

        String data = "{ \"email\": \"peter@klaven\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));

    }
}


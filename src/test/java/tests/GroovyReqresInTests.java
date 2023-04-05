package tests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static specs.CreateUser.createUserRequestSpec;
import static specs.CreateUser.createUserResponseSpec;
import static specs.DeleteUser.deletedUserRequestSpec;
import static specs.DeleteUser.deletedUserResponseSpec;
import static specs.ListUser.listUserRequestSpec;
import static specs.ListUser.listUserResponseSpec;
import static specs.Login.loginRequestSpec;
import static specs.Login.loginResponseSpec;
import static specs.UpdateUser.updatedUserRequestSpec;
import static specs.UpdateUser.updatedUserResponseSpec;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.CreateUserResponse;
import model.LoginBody;
import model.LoginErrorResponse;
import model.UpdateUserResponse;
import model.UserBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("GroovyTests")
public class GroovyReqresInTests {
    @DisplayName("Checking the name and position when creating a user")
    @Test
    void createUser() {
        UserBody data = new UserBody();
        data.setName("morpheus");
        data.setJob("leader");

        CreateUserResponse response = given(createUserRequestSpec)
                .body(data)
                .when()
                .post("/users")
                .then()
                .spec(createUserResponseSpec)
                .extract().as(CreateUserResponse.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("leader");
    }

    @DisplayName("Checking the number of all users to groovy")
    @Test
    void getUsers() {
        given(listUserRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(listUserResponseSpec)
                .body("total", is(12))
                .body("data.find{it.id == 7}.email", is("michael.lawson@reqres.in"))
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("byron.fields@reqres.in"));
    }

    @DisplayName("Editing the user work place")
    @Test
    void editUser() {
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        UserBody data = new UserBody();
        data.setName("morpheus");
        data.setJob("zion resident");

        UpdateUserResponse response = given(updatedUserRequestSpec)
                .when()
                .patch("/users/2")
                .then()
                .spec(updatedUserResponseSpec)
                .extract().as(UpdateUserResponse.class);

        assertThat(response.getUpdatedAt()).contains(dateTime);
    }

    @DisplayName("Deleting a user")
    @Test
    void deleteUser() {
        given(deletedUserRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(deletedUserResponseSpec);
    }

    @DisplayName("Unsuccessful login")
    @Test
    void loginUnsuccessful() {
        LoginBody data = new LoginBody();
        data.setEmail("peter@klaven");

        LoginErrorResponse response = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginErrorResponse.class);

        assertThat(response.getError()).isEqualTo("Missing password");
    }
}
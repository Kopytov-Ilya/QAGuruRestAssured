package tests;

import static io.qameta.allure.Allure.step;
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
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.CreateUserResponse;
import model.ListUser;
import model.LoginBody;
import model.LoginErrorResponse;
import model.UpdateUserResponse;
import model.UserBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

    public class LombokReqresInTest {

        @DisplayName("Checking the name and position when creating a user")
        @Test
        void createUser() {

            UserBody data = new UserBody();
            data.setName("morpheus");
            data.setJob("leader");

            CreateUserResponse response = step("Data entry", () ->
                    given(createUserRequestSpec)
                            .body(data)
                            .when()
                            .post("/users")
                            .then()
                            .spec(createUserResponseSpec)
                            .extract().as(CreateUserResponse.class));

            step("Checking the input data", () -> {
                assertThat(response.getName()).isEqualTo("morpheus");
                assertThat(response.getJob()).isEqualTo("leader");
            });
        }

        @DisplayName("Checking the number of all users")
        @Test
        void getUsers() {
            ListUser response = step("Viewing all users", () ->
                    given(listUserRequestSpec)
                            .when()
                            .get("/users?page=2")
                            .then()
                            .spec(listUserResponseSpec)
                            .extract().as(ListUser.class));

            step("Checking the number of all users", () -> {
                assertThat(response.getTotal()).isEqualTo(12);
            });
        }

        @DisplayName("Editing the user's place of work")
        @Test
        void editUser() {
            String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
            UserBody data = new UserBody();
            data.setName("morpheus");
            data.setJob("zion resident");

            UpdateUserResponse response = step("Data entry", () ->
                    given(updatedUserRequestSpec)
                            .when()
                            .patch("/users/2")
                            .then()
                            .spec(updatedUserResponseSpec)
                            .extract().as(UpdateUserResponse.class));

            step("Checking that the place of work has been edited", () -> {
                assertThat(response.getUpdatedAt()).contains(dateTime);
            });
        }

        @DisplayName("Deleting a user")
        @Test
        void deleteUser() {
            step("Deleting a user", () -> {
                given(deletedUserRequestSpec)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(deletedUserResponseSpec);
            });

        }

        @DisplayName("Login-Unsuccessful")
        @Test
        void loginUnsuccessful() {
            LoginBody data = new LoginBody();
            data.setEmail("peter@klaven");

            LoginErrorResponse response = step("Data entry", () ->
                    given(loginRequestSpec)
                            .body(data)
                            .when()
                            .post("/login")
                            .then()
                            .spec(loginResponseSpec)
                            .extract().as(LoginErrorResponse.class));

            step("Error checking", () -> {
                assertThat(response.getError()).isEqualTo("Missing password");
            });
        }
}

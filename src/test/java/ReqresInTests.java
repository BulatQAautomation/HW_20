import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    public void getListUsersTest() {
        given()
                .log().uri()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data.id[0]", equalTo(7));
    }

    @Test
    public void createNewUserTest() {
        String newUserData = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(newUserData)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheus"), "job", equalTo("leader"));
    }

    @Test
    public void registrationSuccessfulTest() {
        String registrationData = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";
        given()
                .log().uri()
                .contentType(JSON)
                .body(registrationData)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(4), "token", is(notNullValue()));
    }

    @Test
    public void parsingListOfResourcesTest() {

        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get("/unknown")
                .then()
                .log().status()
                .log().body()
                .body("data.name[0]", equalTo("cerulean"))
                .assertThat().contentType(ContentType.JSON);
    }

    @Test
    public void authenticationErrorTest() {

        String email = "{\n" +
                "    \"email\": \"peter@klaven\"\n" +
                "}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(email)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
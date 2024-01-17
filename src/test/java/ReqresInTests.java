import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {
    String url = "https://reqres.in/api";
    @Test
    public void getListUsers(){
        given()
                .log().uri()
        .when()
                .get(url + "/users?page=2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data.id[0]", equalTo(7));
    }

    @Test
    public void createNewUser(){
        String newUserData = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(newUserData)
        .when()
                .post(url + "/users")
        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheus"), "job", equalTo("leader"));
    }

    @Test
    public void registrationSuccessful(){
        String registrationData = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";
        given()
                .log().uri()
                .contentType(JSON)
                .body(registrationData)
        .when()
                .post(url + "/register")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(4), "token", is(notNullValue()));
    }

    @Test
    public void parsingListOfResources(){
        String data1 = "<{id=2, name=fuchsia rose, year=2001, color=#C74375, pantone_value=17-2031}>";

        given()
                .log().uri()
                .contentType(JSON)
        .when()
                .get(url + "/unknown")
        .then()
                .log().status()
                .log().body()
                .body("data.name[0]", equalTo("cerulean"))
                .assertThat().contentType(ContentType.JSON);
    }

    @Test
    public void authenticationError(){

        String email = "{\n" +
                "    \"email\": \"peter@klaven\"\n" +
                "}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(email)
        .when()
                .post(url + "/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
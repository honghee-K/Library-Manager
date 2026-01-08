package thws.librarymanager.adapters.in.rest;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import thws.librarymanager.application.domain.models.Librarian;
import thws.librarymanager.application.ports.out.repository.LibrarianPort;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
@TestHTTPEndpoint(AuthController.class)
public class AuthControllerTest {

    @Inject
    private LibrarianPort librarianPort;

    private String jwtToken;

    public AuthControllerTest() {}

    @BeforeAll
    public void initTestData() {
        Librarian librarian = new Librarian(null, "hong");
        Librarian saved = librarianPort.save(librarian);
        System.out.println("Generated ID: " + saved.getId());
    }

    @Test
    @Order(1)
    public void login() {

        String token = RestAssured.given()
                .when()
                .queryParam("name", "hong")
                .get("login")
                .then()
                .statusCode(200)
                .body(Matchers.not(Matchers.emptyString()))
                .extract()
                .asString();

        Assertions.assertNotNull(token);

        jwtToken = token;
    }

    @Test
    @Order(2)
    public void test() {
        String message = given().auth()
                .oauth2(jwtToken)
                .when()
                .get("/test")
                .then()
                .statusCode(200)
                .body(Matchers.not(Matchers.emptyString()))
                .extract()
                .asString();

        Assertions.assertNotNull(message);
    }
}

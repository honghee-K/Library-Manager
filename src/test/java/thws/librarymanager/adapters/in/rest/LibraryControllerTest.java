package thws.librarymanager.adapters.in.rest;

import java.util.List;

import jakarta.inject.Inject;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;

import thws.librarymanager.adapters.in.rest.models.LibraryDTO;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@QuarkusTest
@TestHTTPEndpoint(LibraryController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryControllerTest {

    @Inject
    LibraryPort libraryPort;

    @BeforeAll
    void setup() {
        libraryPort.save(new Library(null, "Central Library", "Berlin", null));
        libraryPort.save(new Library(null, "City Library", "Hamburg", null));
    }



    @Test
    @Order(1)
    void getLibraryById() {

        LibraryDTO dto = RestAssured.given()
                .when()
                .pathParam("id", 1L)
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(LibraryDTO.class);

        Assertions.assertEquals("Central Library", dto.getName());

        RestAssured.given()
                .when()
                .pathParam("id", 999L)
                .get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(2)
    void getAllLibraries_withoutFilter() {

        List<LibraryDTO> libraries = RestAssured.given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {});

        Assertions.assertEquals(2, libraries.size());
    }

    @Test
    @Order(3)
    void getAllLibraries_withLocationFilter() {

        List<LibraryDTO> libraries = RestAssured.given()
                .when()
                .queryParam("location", "Berlin")
                .get("/")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {});

        Assertions.assertEquals(1, libraries.size());
        Assertions.assertEquals("Berlin", libraries.get(0).getLocation());
    }
}

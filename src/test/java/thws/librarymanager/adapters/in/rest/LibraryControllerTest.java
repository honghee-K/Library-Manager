package thws.librarymanager.adapters.in.rest;

import java.util.List;

import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
    EntityManager em;
    @Inject
    LibraryPort libraryPort;

    private Long lib1Id;


    @BeforeAll
    @Transactional
    void setup() {
        em.createQuery("DELETE FROM BookEntity").executeUpdate();
        em.createQuery("DELETE FROM LibraryEntity").executeUpdate();

        Library lib1 = libraryPort.save(new Library(null, "Central Library", "Berlin", null));
        libraryPort.save(new Library(null, "City Library", "Hamburg", null));
        this.lib1Id = lib1.getId();
    }



    @Test
    @Order(1)
    void getLibraryById() {

        LibraryDTO dto = RestAssured.given()
                .when()
                .pathParam("id", lib1Id)
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

    @Test
    @Order(4)
    void addLibrary() {

        LibraryDTO request = new LibraryDTO();
        request.setName("New Library");
        request.setLocation("Munich");

        LibraryDTO response =
                RestAssured.given()
                        .contentType("application/json")
                        .body(request)
                        .when()
                        .post("/")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(LibraryDTO.class);

        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("New Library", response.getName());
        Assertions.assertEquals("Munich", response.getLocation());
    }

}

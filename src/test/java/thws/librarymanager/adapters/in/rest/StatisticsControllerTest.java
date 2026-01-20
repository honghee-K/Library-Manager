package thws.librarymanager.adapters.in.rest;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@QuarkusTest
@TestHTTPEndpoint(StatisticsController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatisticsControllerTest {

    @Inject
    EntityManager em;

    @Inject
    LibraryPort libraryPort;

    private Long libraryId;

    @BeforeAll
    @Transactional
    void initTestData() {

        em.createQuery("DELETE FROM LoanEntity").executeUpdate();
        em.createQuery("DELETE FROM BookEntity").executeUpdate();
        em.createQuery("DELETE FROM UserEntity").executeUpdate();
        em.createQuery("DELETE FROM LibraryEntity").executeUpdate();

        Library library = libraryPort.save(new Library(null, "Statistics Library", "Würzburg", null));

        libraryId = library.getId();
    }

    @Test
    void getStatistics_withoutLibraryId_shouldReturn400() {

        given().when().get().then().statusCode(400);
    }

    @Test
    void getStatistics_withLibraryId_shouldReturnStatistics() {

        given().queryParam("libraryId", libraryId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("books", Matchers.notNullValue())
                .body("users", Matchers.notNullValue())
                .body("loans", Matchers.notNullValue());
    }

    @Test
    void statistics_shouldContainExpectedFields() {

        given().queryParam("libraryId", libraryId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("books.total", Matchers.notNullValue())
                .body("users.registered", Matchers.notNullValue())
                .body("loans.active", Matchers.notNullValue());
    }

    @Test
    void statistics_shouldContainCacheControlHeader() {

        given().queryParam("libraryId", libraryId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .header("Cache-Control", Matchers.containsString("max-age"));
    }
}

package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import thws.librarymanager.adapters.in.rest.models.LoanDTO;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LibraryPort;
import thws.librarymanager.application.ports.out.repository.UserPort;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
@TestHTTPEndpoint(LoanController.class)
public class LoanControllerTest {
    /*

    @Inject
    EntityManager em;

    @Inject
    LibraryPort libraryPort;

    @Inject
    BookPort bookPort;

    @Inject
    UserPort userPort;

    private Long loanId;
    private Long userId;
    private Long isbn;

    @BeforeAll
    @Transactional
    void initTestData() {

        em.createQuery("DELETE FROM LoanEntity").executeUpdate();
        em.createQuery("DELETE FROM BookEntity").executeUpdate();
        em.createQuery("DELETE FROM UserEntity").executeUpdate();
        em.createQuery("DELETE FROM LibraryEntity").executeUpdate();

        Library library = libraryPort.save(
                new Library(null, "Main Library", "Würzburg", null)
        );

        Book book = bookPort.save(
                new Book(null, 5555L, "Test Book", "Test Author", "Genre", library, null)
        );
        isbn = book.getIsbn();

        User user = userPort.save(
                new User(null, "Max Mustermann", "max@test.de")
        );
        userId = user.getId();
    }

    // -------------------------------------------------------
    // 1️⃣ CREATE LOAN
    // -------------------------------------------------------
    @Test
    @Order(1)
    void createLoan() {

        LoanDTO dto = new LoanDTO();
        dto.setUserId(userId);
        dto.setIsbn(isbn);

        LoanDTO created =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post("/")
                        .then()
                        .statusCode(201)
                        .body("status", Matchers.equalTo("ACTIVE"))
                        .extract()
                        .as(LoanDTO.class);

        Assertions.assertNotNull(created.getId());
        loanId = created.getId();
    }

    // -------------------------------------------------------
    // 2️⃣ GET LOAN (200 + ETag)
    // -------------------------------------------------------
    @Test
    @Order(2)
    void getLoanById() {

        given()
                .pathParam("id", loanId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .header("ETag", Matchers.notNullValue())
                .body("status", Matchers.equalTo("ACTIVE"));
    }

    // -------------------------------------------------------
    // 3️⃣ CONDITIONAL GET → 304
    // -------------------------------------------------------
    @Test
    @Order(3)
    void getLoan_NotModified_WithETag() {

        String etag =
                given()
                        .pathParam("id", loanId)
                        .when()
                        .get("/{id}")
                        .then()
                        .extract()
                        .header("ETag");

        given()
                .pathParam("id", loanId)
                .header("If-None-Match", etag)
                .when()
                .get("/{id}")
                .then()
                .statusCode(304);
    }

    // -------------------------------------------------------
    // 4️⃣ RETURN LOAN (Conditional PUT)
    // -------------------------------------------------------
    @Test
    @Order(4)
    void returnLoan() {

        String etag =
                given()
                        .pathParam("id", loanId)
                        .when()
                        .get("/{id}")
                        .then()
                        .extract()
                        .header("ETag");

        given()
                .pathParam("id", loanId)
                .header("If-Match", etag)
                .when()
                .put("/{id}/return")
                .then()
                .statusCode(204);
    }

    // -------------------------------------------------------
    // 5️⃣ CHECK RETURNED STATUS
    // -------------------------------------------------------
    @Test
    @Order(5)
    void getReturnedLoan() {

        given()
                .pathParam("id", loanId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("RETURNED"))
                .body("returnDate", Matchers.notNullValue());
    }*/
}

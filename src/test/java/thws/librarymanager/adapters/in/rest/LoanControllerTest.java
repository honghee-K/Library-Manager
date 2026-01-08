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
import thws.librarymanager.application.ports.out.repository.LoanPort;
import thws.librarymanager.application.ports.out.repository.UserPort;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
@TestHTTPEndpoint(LoanController.class)
public class LoanControllerTest {

    @Inject
    EntityManager em;

    @Inject
    LoanPort loanPort;

    @Inject
    UserPort userPort;

    @Inject
    BookPort bookPort;

    @Inject
    LibraryPort libraryPort;

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
                new Book(null, 5555L, "Test Book", "Author", "Genre", library, null)
        );
        isbn = book.getIsbn();

        User user = userPort.save(
                new User(null, "Max", "Mustermann")
        );
        userId = user.getId();
    }


    @Test
    @Order(1)
    void createLoan() {

        LoanDTO dto = new LoanDTO();
        dto.setUserId(userId);
        dto.setIsbn(isbn);

        LoanDTO created =
                RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .post("/")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(LoanDTO.class);

        loanId = created.getId();
        Assertions.assertNotNull(loanId);
    }



    @Test
    @Order(2)
    void getLoan() {
        RestAssured.given()
                .pathParam("id", loanId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("ACTIVE"));
    }



    @Test
    @Order(3)
    void returnLoan() {

        RestAssured.given()
                .pathParam("id", loanId)
                .post("/{id}/return")
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("RETURNED"));
    }

}


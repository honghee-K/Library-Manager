package thws.librarymanager.adapters.in.rest;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import thws.librarymanager.adapters.in.rest.models.BookDTO;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
@TestHTTPEndpoint(BookController.class)
public class BookControllerTest {

    @Inject
    private BookPort bookPort;

    @Inject
    private LibraryPort libraryPort;

    private Long testLibraryId;

    public BookControllerTest() {}

    @BeforeAll
    @Transactional
    public void initTestData() {

       /* Library library = new Library(null, "Main Library", "Würzburg", null);
        Library savedLibrary = libraryPort.save(library);
        this.testLibraryId = savedLibrary.getId();*/

        Book book1 = new Book(null, 1234L, "title1", "author1", "genre", null, null);
        bookPort.save(book1);

        Book book2 = new Book(null, 1235L, "title2", "author2", "genre", null, null);
        bookPort.save(book2);
    }

    @Test
    @Order(1)
    public void getBookByIsbn() {

        BookDTO bookDTO = RestAssured.given()
                .when()
                .pathParam("isbn", 1234L)
                .get("/{isbn}")
                .then()
                .statusCode(200)
                .body(Matchers.not(Matchers.emptyString()))
                .extract()
                .as(BookDTO.class);

        Assertions.assertNotNull(bookDTO);
        Assertions.assertEquals("author1", bookDTO.getAuthor());

        RestAssured.given()
                .when()
                .pathParam("isbn", 9999L)
                .get("/{isbn}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(2)
    public void getAllBooks() {

        List<BookDTO> bookDTOs = RestAssured.given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(Matchers.not(Matchers.emptyString()))
                .extract()
                .as(new TypeRef<>() {});

        Assertions.assertNotNull(bookDTOs);
        Assertions.assertEquals(2, bookDTOs.size());

        List<BookDTO> bookDTOs2 = RestAssured.given()
                .when()
                .queryParam("author", "author2")
                .get("/")
                .then()
                .statusCode(200)
                .body(Matchers.not(Matchers.emptyString()))
                .extract()
                .as(new TypeRef<>() {});

        Assertions.assertNotNull(bookDTOs2);
        Assertions.assertEquals(1, bookDTOs2.size());
        Assertions.assertEquals(1235L, bookDTOs2.get(0).getIsbn());
    }

    /*    @Test //TODO: JpaLibraryRepository muss korrigiert werden.
    @Order(3)
    public void addBook() {
        BookDTO newBookDTO = new BookDTO();
        newBookDTO.setIsbn(9988L);
        newBookDTO.setTitle("New Book Title");
        newBookDTO.setAuthor("New Author");
        newBookDTO.setGenre("Tech");
        newBookDTO.setLibraryId(null);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newBookDTO)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .header("Location", Matchers.containsString("9988"))
                .body("isbn", Matchers.equalTo(9988))
                .body("author", Matchers.equalTo("New Author"));
    }*/
}

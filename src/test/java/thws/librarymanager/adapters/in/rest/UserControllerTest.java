package thws.librarymanager.adapters.in.rest;

import java.util.List;

import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import thws.librarymanager.adapters.in.rest.models.UserDTO;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.out.repository.UserPort;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
@TestHTTPEndpoint(UserController.class)
public class UserControllerTest {

    @Inject
    EntityManager em;

    @Inject
    private UserPort userPort;

    private Long testUserId;

    public UserControllerTest() {}

    @BeforeAll
    @Transactional
    public void initTestData() {
        em.createQuery("DELETE FROM LoanEntity").executeUpdate();
        em.createQuery("DELETE FROM UserEntity").executeUpdate();

        User user1 = new User(null, "user1", "user1@test.com");
        User savedUser = userPort.save(user1);
        this.testUserId = savedUser.getId();

        User user2 = new User(null, "user2", "user2@test.com");
        userPort.save(user2);
    }
    @Test
    @Order(1)
    public void createUser() {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setName("newuser");
        newUserDTO.setEmail("new@test.com");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newUserDTO)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .header("Location", Matchers.notNullValue())
                .header("Link", Matchers.containsString("rel=\"self\""))
                .body("name", Matchers.equalTo("newuser"))
                .body("email", Matchers.equalTo("new@test.com"));
    }
    @Test
    @Order(2)
    public void getUserById() {
        UserDTO userDTO = RestAssured.given()
                .when()
                .pathParam("id", testUserId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(UserDTO.class);

        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals("user1", userDTO.getName());

        RestAssured.given()
                .when()
                .pathParam("id", 9999L)
                .get("/{id}")
                .then()
                .statusCode(404);
    }



    @Test
    @Order(3)
    public void getUserById_HateoasHeaderValidation() {
        Response response = RestAssured.given()
                //.log().all()
                .when()
                .pathParam("id", testUserId)
                .get("/{id}");

        response.then().statusCode(200);

        List<String> links = response.headers().getValues("Link");

        Assertions.assertTrue(links.stream().anyMatch(l -> l.contains("rel=\"self\"")), "self missing");
        Assertions.assertTrue(links.stream().anyMatch(l -> l.contains("rel=\"update\"")), "update missing");
        Assertions.assertTrue(links.stream().anyMatch(l -> l.contains("rel=\"delete\"")), "delete missing");

        response.then().header("ETag", Matchers.notNullValue());
    }

    @Test
    @Order(4)
    public void getAllUsers() {
        List<UserDTO> userDTOs = RestAssured.given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .header("Link", Matchers.containsString("rel=\"self\""))
                .extract()
                .as(new TypeRef<>() {});

        Assertions.assertNotNull(userDTOs);
        Assertions.assertEquals(3, userDTOs.size());
    }

    @Test
    @Order(5)
    public void updateUser() {
        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("updatedUser");
        updateDTO.setEmail("updated@test.com");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateDTO)
                .when()
                .pathParam("id", testUserId)
                .put("/{id}")
                .then()
                .statusCode(204)
                .header("Link", Matchers.containsString("rel=\"self\""));

        RestAssured.given()
                .when()
                .pathParam("id", testUserId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("updatedUser"));
    }

    @Test
    @Order(6)
    public void deleteUser() {
        RestAssured.given()
                .when()
                .pathParam("id", testUserId)
                .delete("/{id}")
                .then()
                .statusCode(204)
                .header("Link", Matchers.containsString("rel=\"collection\""));

        RestAssured.given()
                .when()
                .pathParam("id", testUserId)
                .get("/{id}")
                .then()
                .statusCode(404);
    }
}
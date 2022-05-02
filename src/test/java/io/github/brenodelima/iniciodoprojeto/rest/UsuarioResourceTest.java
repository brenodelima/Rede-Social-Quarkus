package io.github.brenodelima.iniciodoprojeto.rest;

import io.github.brenodelima.iniciodoprojeto.rest.dto.CriarUsuarioRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioResourceTest {

    @Test
    @DisplayName("Deverá criar um usuario com sucesso!")
    @Order(1)
    public void criarUsuarioTest(){
        var usuario = new CriarUsuarioRequest();
        usuario.setNome("Breno");
        usuario.setIdade(24);

        var resposta =
            given()
                .contentType(ContentType.JSON)
                .body(usuario)
            .when()
                .post("/usuario")
                .then()
                .statusCode(201)
                .extract().response();
        assertNotNull(resposta.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Deverá retornar uma mensagem de erro ao passar o JSON incorreto!")
    @Order(2)
    public void erroAoCriarUsuarioTest(){
        var usuario = new CriarUsuarioRequest();
        usuario.setNome(null);
        usuario.setIdade(null);

        var resposta =
            given()
                .contentType(ContentType.JSON)
                .body(usuario)
            .when()
                .post("/usuario")
            .then()
                .statusCode(422)
                .extract().response();
        assertEquals("Validacao Erro", resposta.jsonPath().getString("menssagem"));
    }
    @Test
    @DisplayName("Deverá listar todos os usuario!!")
    @Order(3)
    public void listarTodosUsuariosTest() {

        var resposta =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/usuario")
                        .then()
                        .statusCode(200)
                        .body("size()", Matchers.is(1));
    }
}
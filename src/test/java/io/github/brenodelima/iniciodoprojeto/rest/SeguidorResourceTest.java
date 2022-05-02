package io.github.brenodelima.iniciodoprojeto.rest;

import io.github.brenodelima.iniciodoprojeto.rest.dto.SeguidorRequest;
import io.github.brenodelima.redesocial.domain.modelo.Seguidor;
import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.github.brenodelima.repository.SeguidorRepository;
import io.github.brenodelima.repository.UsuarioRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(SeguidorResource.class)
class SeguidorResourceTest {

    @Inject
    SeguidorRepository seguidorRepository;
    @Inject
    UsuarioRepository usuarioRepository;

    Long usuarioId;
    Long seguidorId;

    @BeforeEach
    @Transactional
    void configTestUsuario(){
          // USUARIO PADRÃO!!
            var usuario = new Usuario();
            usuario.setNome("Maria Marroá");
            usuario.setIdade(25);
            usuarioRepository.persist(usuario);
            usuarioId = usuario.getId();

        // SEGUIDOR UM!!
        var seguidorUm = new Usuario();
        seguidorUm.setNome("Juventino");
        seguidorUm.setIdade(26);
        usuarioRepository.persist(seguidorUm);
        seguidorId = seguidorUm.getId();

        // SEGUIDOR DOIS!!
        var seguidorDois = new Seguidor();
        seguidorDois.setSeguidor(seguidorUm);
        seguidorDois.setUsuario(usuario);
        seguidorRepository.persist(seguidorDois);

    }

    @Test
    @DisplayName("Deverá retornar 409 quando o Id do seguidor for igual o Id do usuario!")
     public void mesmoUsuarioComoSeguidorTest(){

        var seguidor = new SeguidorRequest();
        seguidor.setSeguidorId(usuarioId);

            given()
                .contentType(ContentType.JSON)
                .body(seguidor)
                .pathParam("usuarioId", usuarioId)
            .when().
                put()
            .then()
                .statusCode(409).body(Matchers.is("Você não pode seguira você mesmo. " +
                            "Busque um outro usuario para seguir!"));
    }

    @Test
    @DisplayName("Deverá retornar 404 quando ao tentar seguir um usuario o Id do usuario não existir!")
    public void usuarioNãoEncontradoAoTentarSeguirTest(){

        var seguidor = new SeguidorRequest();
        seguidor.setSeguidorId(usuarioId);

        var usuarioIdNaoExiste= 999;

        given()
                .contentType(ContentType.JSON)
                .body(seguidor)
                .pathParam("usuarioId", usuarioIdNaoExiste)
                .when().
                put()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Deverá seguir um usuário!")
    public void seguirUmUsuario(){

        var seguidor = new SeguidorRequest();
        seguidor.setSeguidorId(seguidorId);

        given()
            .contentType(ContentType.JSON)
            .body(seguidor)
            .pathParam("usuarioId", usuarioId)
        .when()
            .put()
        .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("Deverá retornar 404 ao tentar listar os seguidores de um usuario que não existir!")
    public void seguidoresNãoEncontradoPoisUsuarioNaoExisteTest(){

        var usuarioIdNaoExiste= 999;

        given()
            .contentType(ContentType.JSON)
            .pathParam("usuarioId", usuarioIdNaoExiste)
        .when()
            .get()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Deverá retornar uma lista de seguidores!")
    public void listaDeSeguidoresTest(){
        var response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("usuarioId", usuarioId)
        .when()
            .get()
        .then()
            .extract().response();

        var quantidadeDeSeguidores=  response.jsonPath().get("quantidadeDeSeguidores");
        var seguidoresContagem= response.jsonPath().getList("contagem");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, quantidadeDeSeguidores);
        assertEquals(1, seguidoresContagem.size());

}

    @Test
    @DisplayName("Deverá retornar 404 quando tentar deixar de seguir um usuario  e o Id do usuario não existir!")
    public void usuarioNãoEncontradoAoTentarDeixarSeguirTest(){

        var seguidor = new SeguidorRequest();
        seguidor.setSeguidorId(usuarioId);

        var usuarioIdNaoExiste= 999;

        given()
            .pathParam("usuarioId", usuarioIdNaoExiste)
            .queryParam("seguidorId", seguidorId)
        .when()
            .delete()
        .then()
            .statusCode(404);
}

    @Test
    @DisplayName("Deverá deixar de seguir um usario!")
    public void deixarDeSeguirUmUsuarioTest(){

        given()
            .pathParam("usuarioId", usuarioId)
            .queryParam("seguidorId", seguidorId)
        .when()
            .delete()
        .then()
            .statusCode(204);
    }

}
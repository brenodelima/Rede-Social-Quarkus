package io.github.brenodelima.iniciodoprojeto.rest;

import io.github.brenodelima.iniciodoprojeto.rest.dto.CriarPostagemRequest;
import io.github.brenodelima.redesocial.domain.modelo.Postagem;
import io.github.brenodelima.redesocial.domain.modelo.Seguidor;
import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.github.brenodelima.repository.PostagemRepository;
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

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UsuarioRepository usuarioRespository;
    SeguidorRepository seguidorRepository;
    PostagemRepository postagemRepository;
    Long usuarioId;
    Long usuarioNaoSeguidorId;
    Long usuarioSeguidorId;

    @BeforeEach
    @Transactional
    public void configTestUsuario(){
    // USUARIO PADRÃO!!
        var usuario = new Usuario();
        usuario.setNome("Fulano");
        usuario.setIdade(51);
        usuarioRespository.persist(usuario);
        usuarioId = usuario.getId();

        //CRIANDO A POSTAGEM DO USUARIO PADRÃO
        Postagem postagem = new Postagem();
        postagem.setTexto("Essa é a minha primeira postagem!");
        postagem.setUsuario(usuario);
        postagemRepository.persist(postagem);

        //NÃO SEGUE NINGUEM!!
        var usuarioNaoSeguidor = new Usuario();
        usuarioNaoSeguidor.setNome("Cicrano");
        usuarioNaoSeguidor.setIdade(20);
        usuarioRespository.persist(usuarioNaoSeguidor);
        usuarioNaoSeguidorId = usuarioNaoSeguidor.getId();

        //É SEGUIDOR!!
        var usuarioSeguidor = new Usuario();
        usuarioSeguidor.setNome("BELTRANO");
        usuarioSeguidor.setIdade(26);
        usuarioRespository.persist(usuarioSeguidor);
        usuarioSeguidorId = usuarioSeguidor.getId();

        Seguidor seguidor = new Seguidor();
        seguidor.setUsuario(usuario);
        seguidor.setSeguidor(usuarioSeguidor);
        seguidorRepository.persist(seguidor);


    }

    @Test
    @DisplayName("Deverá criar uma postagem  para o usuario #teste!")
    public void criarPostagemTest(){
        var postRequest = new CriarPostagemRequest();
        postRequest.setText("testanto, testando, testando");

            given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("usuarioId", usuarioId)
            .when()
                .post()
            .then()
                .statusCode(201);

    }
    @Test
    @DisplayName("Deverá retornar Erro 404, quando tentar postar para um usuario inexistente!")
    public void postParaUsuarioInexistente(){
        var postRequest = new CriarPostagemRequest();
        postRequest.setText("testanto, testando, testando");

        var usuarioIdInexistente = 999;

            given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("usuarioId", 999)
            .when()
                .post()
            .then()
                .statusCode(404);
        }
    @Test
    @DisplayName("Deverá retornar 404 quando usuário não existir!")
    public void  listPostUsuarioNaoExistirTest(){
        var inexistenteUsuarioId = 999;

        given()
            .pathParam("usuarioId", inexistenteUsuarioId)
        .when()
            .get()
        .then()
            .statusCode(404);

    }

//    @Test
//    @DisplayName("Deverá retornar 400 quando esquecer de informar o seguidor!")
//    public void  listPostUsuarioNaoInformaSeguidorTest(){
//        given()
//            .pathParam("usuarioId", usuarioId)
//        .when()
//            .get()
//        .then()
//            .statusCode(400)
//            .body(Matchers.is("Voce esqueceu de informar o seguidor!!"));
//    }

    @Test
    @DisplayName("Deverá retornar 400 quando o seguidor nao existir!")
    public void  listPostUsuarioSeguidorNaoExisteTest(){

        var inexistenteSeguidorId = 999;

         given()
            .pathParam("usuarioId", usuarioId)
            .header("seguidorId", inexistenteSeguidorId)
        .when()
            .get()
        .then()
            .statusCode(400)
            .body(Matchers.is("Seguidor não existe!!"));
   }

    @Test
    @DisplayName("Deverá retornar 403 quando o usuario nao seguir o outro!")
    public void  listPostUsuarioNaoESeguidorTest(){

        given()
                .pathParam("usuarioId", usuarioId)
                .header("seguidorId", usuarioNaoSeguidorId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("Voce não pode visualizar as postagens deste usuario, pois, voce não segue ele!"));
    }

    @Test
    @DisplayName("Deverá retornar uma lista de post!")
    public void  listPostTest(){

        given()
            .pathParam("usuarioId", usuarioId)
            .header("seguidorId", usuarioSeguidorId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));

    }
}
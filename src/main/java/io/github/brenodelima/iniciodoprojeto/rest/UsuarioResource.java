package io.github.brenodelima.iniciodoprojeto.rest;

import io.github.brenodelima.iniciodoprojeto.rest.dto.CriarUsuarioRequest;
import io.github.brenodelima.iniciodoprojeto.rest.dto.RespostaErro;
import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.github.brenodelima.repository.UsuarioRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.AllArgsConstructor;


import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    private Validator validator;
    @Inject
    private UsuarioRepository repository;

    @POST
    @Transactional
    public Response criarUsuario(CriarUsuarioRequest usuarioRequest){

        Set<ConstraintViolation<CriarUsuarioRequest>> validacoes = validator.validate(usuarioRequest);
                if(!validacoes.isEmpty()){
                return RespostaErro
                        .criarAPartirdaValidacao(validacoes)
                        .withStatusCode(RespostaErro.UNPROCESSABLE_ENTITY_STATUS);
                }

        Usuario usuario = new Usuario();
        usuario.setIdade(usuarioRequest.getIdade());
        usuario.setNome(usuarioRequest.getNome());

        repository.persist(usuario);

        return Response.status(Response.Status.CREATED.getStatusCode())
                .entity(usuario)
                .build();
    }

    @GET
    public Response listTodosUsuarios(){
        PanacheQuery<Usuario> listartodosusuarios = repository.findAll();
        return Response.ok(listartodosusuarios.list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletarUsuario(@PathParam("id") Long id){
        Usuario usuario = repository.findById(id);

        if (usuario != null){
            repository.delete(usuario);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizarUsuario(@PathParam("id") Long id, CriarUsuarioRequest dadosDoUsuario){
        Usuario usuario = repository.findById(id);

        if (usuario != null){
            usuario.setNome(dadosDoUsuario.getNome());
            usuario.setIdade(dadosDoUsuario.getIdade());
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

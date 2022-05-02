package io.github.brenodelima.iniciodoprojeto.rest;

import io.github.brenodelima.iniciodoprojeto.rest.dto.SeguidorPorUsuarioResponse;
import io.github.brenodelima.iniciodoprojeto.rest.dto.SeguidorRequest;
import io.github.brenodelima.iniciodoprojeto.rest.dto.SeguidorResponse;
import io.github.brenodelima.redesocial.domain.modelo.Seguidor;
import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.github.brenodelima.repository.SeguidorRepository;
import io.github.brenodelima.repository.UsuarioRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/usuario/{usuarioId}/seguidor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguidorResource {

    private SeguidorRepository seguidorRepository;
    private UsuarioRepository usuarioRepository;

    @Inject
    public SeguidorResource(SeguidorRepository seguidorRepository,
                            UsuarioRepository usuarioRepository){

        this.seguidorRepository = seguidorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PUT
    @Transactional
    public Response seguindoUsuario(@PathParam("usuarioId") Long usuarioId,
                                    SeguidorRequest seguidorRequest){

        if(usuarioId.equals(seguidorRequest.getSeguidorId())){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Você não pode seguira você mesmo. Busque um outro usuario para seguir!")
                    .build();
        }

        Usuario usuario = usuarioRepository.findById(usuarioId);
        if(usuario == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Usuario seguidor = usuarioRepository.findById(seguidorRequest.getSeguidorId());

        boolean seguidor1 = seguidorRepository.seguir(seguidor, usuario);

        if(!seguidor1){
            Seguidor entity = new Seguidor();
            entity.setUsuario(usuario);
            entity.setSeguidor(seguidor);

        seguidorRepository.persist(entity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listaDeSeguidores(@PathParam("usuarioId") Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId);
        if(usuario == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = seguidorRepository.findByUsuario(usuarioId);
        SeguidorPorUsuarioResponse responseObjeto = new SeguidorPorUsuarioResponse();
        responseObjeto.setQuantidadeDeSeguidores(list.size());

        var seguidoresList= list.stream()
                .map(SeguidorResponse:: new)
                .collect(Collectors.toList());

        responseObjeto.setContagem(seguidoresList);
        return Response.ok(responseObjeto).build();

    }

    @DELETE
    @Transactional
    public Response deixarDeSeguirUsuario(
            @PathParam("usuarioId") Long usuarioId,
            @QueryParam("seguidorId") Long seguidorId){

        Usuario usuario = usuarioRepository.findById(usuarioId);
        if(usuario == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    seguidorRepository.deleteSeguidorIdeUsuarioId(seguidorId, usuarioId);

    return Response.status(Response.Status.NO_CONTENT).build();
    }


}

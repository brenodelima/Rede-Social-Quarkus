package io.github.brenodelima.iniciodoprojeto.rest;

import io.github.brenodelima.iniciodoprojeto.rest.dto.CriarPostagemRequest;
import io.github.brenodelima.iniciodoprojeto.rest.dto.PostagemResposta;
import io.github.brenodelima.redesocial.domain.modelo.Postagem;
import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.github.brenodelima.repository.PostagemRepository;
import io.github.brenodelima.repository.SeguidorRepository;
import io.github.brenodelima.repository.UsuarioRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/usuario/{usuarioId}/post")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UsuarioRepository usuarioRepository;
    private PostagemRepository postagemRepository;
    private SeguidorRepository seguidorRespository;

    @Inject
    public PostResource(UsuarioRepository usuarioRepository,
                        PostagemRepository repository,
                        SeguidorRepository seguidorRespository) {
        this.usuarioRepository = usuarioRepository;
        this.postagemRepository = repository;
        this.seguidorRespository = seguidorRespository;
    }

    @POST
    @Transactional
     public Response salvePost(
             @PathParam("usuarioId") Long usuarioId, CriarPostagemRequest request){
        Usuario usuario = usuarioRepository.findById(usuarioId);
        if (usuario == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Postagem post = new Postagem();
        post.setTexto(request.getText());
        post.setUsuario(usuario);
        postagemRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();

    }

    @GET
    public Response listePost(@PathParam("usuarioId") Long usuarioId,
                            @HeaderParam("seguidorId") Long seguidorId){
        Usuario usuario = usuarioRepository.findById(usuarioId); //Aqui o método findbyId está
        //buscando o usuario pelo ID
        if (usuario == null){ //SE O USUÁRIO FOR NULO (NÃO EXISTIR) ELE RESPONDERÁ UM NOT FOUND
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (usuarioId == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Voce esqueceu de informar o seguidor!!")
                    .build();
        }

        Usuario seguidor = usuarioRepository.findById(seguidorId);

        if (seguidor == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Seguidor não existe!!")
                    .build();
        }
        boolean seguir = seguidorRespository.seguir(seguidor, usuario);
            if (!seguir){
                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Voce não pode visualizar as postagens deste usuario, pois, voce não segue ele!")
                        .build();
            }

        PanacheQuery<Postagem> query = postagemRepository
                .find("usuario", Sort.by("dateTime", Sort.Direction.Descending ),usuario);

        var list = query.list();

       var postagemRespostaList= list.stream()
                .map(PostagemResposta::paraEntidade)
                .collect(Collectors.toList());

        return Response.ok(postagemRespostaList).build();
    }
}

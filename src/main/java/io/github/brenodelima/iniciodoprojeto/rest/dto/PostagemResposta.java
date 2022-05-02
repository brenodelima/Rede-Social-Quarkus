package io.github.brenodelima.iniciodoprojeto.rest.dto;

import io.github.brenodelima.redesocial.domain.modelo.Postagem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostagemResposta {

    private String texto;
    private LocalDateTime dateHora;

    public static PostagemResposta paraEntidade(Postagem postagem) {
     PostagemResposta response = new PostagemResposta();
        response.setTexto(postagem.getTexto());
        response.setDateHora(postagem.getDataHora());
        return response;
    }
}

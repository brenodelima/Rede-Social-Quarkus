package io.github.brenodelima.iniciodoprojeto.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeguidorPorUsuarioResponse {
    private Integer quantidadeDeSeguidores;
    private List<SeguidorResponse> contagem;
}


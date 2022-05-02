package io.github.brenodelima.iniciodoprojeto.rest.dto;

import io.github.brenodelima.redesocial.domain.modelo.Seguidor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeguidorResponse {
    private Long id;
    private String nome;

    public SeguidorResponse(Seguidor seguidor) {
        this(seguidor.getId(), seguidor.getSeguidor().getNome());
    }
}

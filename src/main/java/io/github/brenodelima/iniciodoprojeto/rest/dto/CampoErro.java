package io.github.brenodelima.iniciodoprojeto.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CampoErro {
    private String campoDoErro;
    private String menssagemDeErro;

}

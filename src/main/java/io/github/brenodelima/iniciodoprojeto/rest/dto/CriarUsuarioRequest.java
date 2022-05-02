package io.github.brenodelima.iniciodoprojeto.rest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CriarUsuarioRequest {

    @NotBlank(message = "O preenchimento do nome do usuário é obrigatório!!")
    private String nome;

    @NotNull(message = "O preenchimento da idade do usuário é obrigatório!!")
    private Integer idade;



}

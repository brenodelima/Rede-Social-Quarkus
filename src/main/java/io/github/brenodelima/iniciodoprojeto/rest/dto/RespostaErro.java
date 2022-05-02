package io.github.brenodelima.iniciodoprojeto.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class RespostaErro {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

    private String menssagem;
    private Collection<CampoErro> erros;

    public static <T> RespostaErro criarAPartirdaValidacao(Set<ConstraintViolation<T>> violations){
        List<CampoErro> erros = violations
                .stream()
                .map(ConstraintViolat -> new CampoErro
                        (ConstraintViolat.getPropertyPath().toString(), ConstraintViolat.getMessage()))
                .collect(Collectors.toList());

        String menssagem = "Validacao Erro";

        var responseError = new RespostaErro(menssagem, erros);

        return new RespostaErro(menssagem, erros);
    }
    public Response withStatusCode(int code){
        return Response.status(code).entity(this).build();
    }
}

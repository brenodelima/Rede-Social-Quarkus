package io.github.brenodelima.repository;

import io.github.brenodelima.redesocial.domain.modelo.Seguidor;
import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SeguidorRepository implements PanacheRepository<Seguidor> {

    public boolean seguir(Usuario seguidor, Usuario usuario){
        var params = Parameters
                .with("seguidor", seguidor)
                .and("usuario", usuario)
                .map();

        PanacheQuery<Seguidor> query = find("seguidor = :seguidor and usuario = :usuario", params);
        Optional<Seguidor> resultado = query.firstResultOptional();

        return resultado.isPresent();
    }

    public List<Seguidor> findByUsuario(Long usuarioId){
        PanacheQuery<Seguidor> query = find("usuario.id", usuarioId);
        return query.list();

    }

    public void deleteSeguidorIdeUsuarioId(Long seguidorId, Long usuarioId) {
        var params = Parameters
                .with("usuarioId", usuarioId)
                .and("seguidorId", seguidorId)
                .map();
        delete("seguidor.id = :seguidorId and usuario.id =:usuarioId", params);

    }
}

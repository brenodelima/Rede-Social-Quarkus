package io.github.brenodelima.repository;

import io.github.brenodelima.redesocial.domain.modelo.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
}

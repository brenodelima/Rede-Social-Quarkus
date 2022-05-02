package io.github.brenodelima.repository;

import io.github.brenodelima.redesocial.domain.modelo.Postagem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostagemRepository implements PanacheRepository<Postagem> {
}

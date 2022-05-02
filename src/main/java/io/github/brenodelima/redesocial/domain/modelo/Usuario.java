package io.github.brenodelima.redesocial.domain.modelo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table (name = "usuarios") // mostrando para hiberneta que o nome da tabela é diferente do nome da classe!
@Setter
@Getter
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name")
    private String nome;

    @Column(name = "idade")
    private Integer idade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(nome, usuario.nome) && Objects.equals(idade, usuario.idade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, idade);
    }
}

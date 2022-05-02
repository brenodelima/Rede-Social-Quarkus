package io.github.brenodelima.redesocial.domain.modelo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "postagens")
@Data
@Setter
@Getter
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postagem_text")
    private String texto;

    @Column(name = "datetime")
    private LocalDateTime dataHora;

    @JoinColumn(name = "usuarios_id")
    @ManyToOne
    private Usuario usuario;

    @PrePersist
    private void prePersist(){
    setDataHora(LocalDateTime.now());
    }

}

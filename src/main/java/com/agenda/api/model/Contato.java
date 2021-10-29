package com.agenda.api.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Entity
@Data
public class Contato implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "${campo.nome.obrigatorio}")
    @Column(nullable = false, length = 120)
    private String nome;
    
    @NotEmpty(message = "${campo.email.obrigatorio}")
    @Email(message = "${campo.email.invalido}")
    @Column(nullable = false, length = 200, unique = true)
    private String email;
    
    @Column(nullable = false)
    private Boolean favorito;
    
    @Lob
    @Column
    private byte[] foto;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.nome = StringUtils.strip(this.nome);
        this.email = StringUtils.strip(this.email);
        if(this.favorito == null) this.favorito = false;
    }
    
}

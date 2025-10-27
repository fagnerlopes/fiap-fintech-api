package br.com.fintech.fintechapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entidade que representa uma Pessoa Jurídica no sistema
 * Relacionamento 1:1 com Usuario
 */
@Entity
@Table(name = "PESSOA_JURIDICA")
public class PessoaJuridica {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_PESSOA_JURIDICA"
    )
    @SequenceGenerator(
            name = "SEQ_PESSOA_JURIDICA",
            sequenceName = "SEQ_PESSOA_JURIDICA",
            allocationSize = 1
    )
    @Column(name = "id_pj")
    private Long idPj;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @JsonIgnore  // Evitar loop infinito ao serializar
    private Usuario usuario;

    @Column(unique = true, nullable = false, length = 18)
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    // Construtor padrão
    public PessoaJuridica() {
    }

    // Construtor com parâmetros
    public PessoaJuridica(Usuario usuario, String cnpj, String razaoSocial) {
        this.usuario = usuario;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    // Getters e Setters
    public Long getIdPj() {
        return idPj;
    }

    public void setIdPj(Long idPj) {
        this.idPj = idPj;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
}


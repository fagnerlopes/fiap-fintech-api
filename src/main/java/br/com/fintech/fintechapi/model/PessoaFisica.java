package br.com.fintech.fintechapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidade que representa uma Pessoa Física no sistema
 * Relacionamento 1:1 com Usuario
 */
@Entity
@Table(name = "PESSOA_FISICA")
public class PessoaFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pf")
    private Long idPf;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @JsonIgnore  // Evitar loop infinito ao serializar
    private Usuario usuario;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @Column(name = "data_nasc")
    private LocalDate dataNasc;

    // Construtor padrão
    public PessoaFisica() {
    }

    // Construtor com parâmetros
    public PessoaFisica(Usuario usuario, String nome, String cpf, LocalDate dataNasc) {
        this.usuario = usuario;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNasc = dataNasc;
    }

    // Getters e Setters
    public Long getIdPf() {
        return idPf;
    }

    public void setIdPf(Long idPf) {
        this.idPf = idPf;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }
}


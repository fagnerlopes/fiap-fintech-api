package br.com.fintech.fintechapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DESPESA")
public class Despesa {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_DESPESAS"
    )
    @SequenceGenerator(
            name = "SEQ_DESPESAS",
            sequenceName = "SEQ_DESPESAS",
            allocationSize = 1
    )
    @Column(name = "id_despesa")
    private Long idDespesa;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(length = 255)
    private String descricao;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(columnDefinition = "NUMBER(1) DEFAULT 0", nullable = false)
    private Integer recorrente = 0;

    @Column(columnDefinition = "NUMBER(1) DEFAULT 0", nullable = false)
    private Integer pendente = 0;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria")
    private Subcategoria subcategoria;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Despesa() {
        this.criadoEm = LocalDateTime.now();
        this.recorrente = 0;
        this.pendente = 0;
    }

    public Long getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(Long idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getRecorrente() {
        return recorrente;
    }

    public void setRecorrente(Integer recorrente) {
        this.recorrente = recorrente;
    }

    public Integer getPendente() {
        return pendente;
    }

    public void setPendente(Integer pendente) {
        this.pendente = pendente;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}


package br.com.fintech.fintechapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "CATEGORIA")
public class Categoria {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_CATEGORIAS"
    )
    @SequenceGenerator(
            name = "SEQ_CATEGORIAS",
            sequenceName = "SEQ_CATEGORIAS",
            allocationSize = 1
    )
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nome_categoria", nullable = false, length = 80)
    private String nomeCategoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_categoria", nullable = false, length = 20)
    private TipoCategoria tipoCategoria;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Subcategoria> subcategorias;

    public Categoria() {
    }

    public Categoria(String nomeCategoria, TipoCategoria tipoCategoria) {
        this.nomeCategoria = nomeCategoria;
        this.tipoCategoria = tipoCategoria;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public TipoCategoria getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(TipoCategoria tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public List<Subcategoria> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<Subcategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }
}


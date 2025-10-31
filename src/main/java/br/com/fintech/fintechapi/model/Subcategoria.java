package br.com.fintech.fintechapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SUBCATEGORIA")
public class Subcategoria {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_SUBCATEGORIAS"
    )
    @SequenceGenerator(
            name = "SEQ_SUBCATEGORIAS",
            sequenceName = "SEQ_SUBCATEGORIAS",
            allocationSize = 1
    )
    @Column(name = "id_subcategoria")
    private Long idSubcategoria;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "nome_subcat", nullable = false, length = 80)
    private String nomeSubcat;

    public Subcategoria() {
    }

    public Subcategoria(Categoria categoria, String nomeSubcat) {
        this.categoria = categoria;
        this.nomeSubcat = nomeSubcat;
    }

    public Long getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Long idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNomeSubcat() {
        return nomeSubcat;
    }

    public void setNomeSubcat(String nomeSubcat) {
        this.nomeSubcat = nomeSubcat;
    }
}


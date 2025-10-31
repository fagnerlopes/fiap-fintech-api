package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    List<Subcategoria> findByCategoriaIdCategoria(Long idCategoria);

    Optional<Subcategoria> findByCategoriaIdCategoriaAndNomeSubcat(Long idCategoria, String nomeSubcat);

    boolean existsByCategoriaIdCategoriaAndNomeSubcat(Long idCategoria, String nomeSubcat);
}


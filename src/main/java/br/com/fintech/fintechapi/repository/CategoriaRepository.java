package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTipoCategoria(TipoCategoria tipoCategoria);

    Optional<Categoria> findByNomeCategoriaAndTipoCategoria(String nomeCategoria, TipoCategoria tipoCategoria);

    boolean existsByNomeCategoriaAndTipoCategoria(String nomeCategoria, TipoCategoria tipoCategoria);
}


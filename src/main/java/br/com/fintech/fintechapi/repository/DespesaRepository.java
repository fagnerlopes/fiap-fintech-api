package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByUsuarioIdUsuario(Long idUsuario);

    List<Despesa> findByUsuarioIdUsuarioAndDataVencimentoBetween(
            Long idUsuario,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    List<Despesa> findByUsuarioIdUsuarioAndPendente(Long idUsuario, Integer pendente);

    /**
     * Busca despesas com filtros opcionais (JPQL dinâmica)
     * Todos os parâmetros são opcionais
     * @param idUsuario ID do usuário (obrigatório)
     * @param dataInicio Data inicial (opcional)
     * @param dataFim Data final (opcional)
     * @param idCategoria ID da categoria (opcional)
     * @param pendente Status pendente (opcional)
     * @return Lista de despesas filtradas
     */
    @Query("SELECT d FROM Despesa d WHERE d.usuario.idUsuario = :idUsuario " +
           "AND (:dataInicio IS NULL OR d.dataVencimento >= :dataInicio) " +
           "AND (:dataFim IS NULL OR d.dataVencimento <= :dataFim) " +
           "AND (:idCategoria IS NULL OR d.categoria.idCategoria = :idCategoria) " +
           "AND (:pendente IS NULL OR d.pendente = :pendente) " +
           "ORDER BY d.dataVencimento DESC")
    List<Despesa> findByFiltros(
            @Param("idUsuario") Long idUsuario,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("idCategoria") Long idCategoria,
            @Param("pendente") Integer pendente
    );
}


package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para operações de Receita no banco de dados
 */
@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    /**
     * Busca todas as receitas de um usuário específico
     * @param idUsuario ID do usuário
     * @return Lista de receitas do usuário
     */
    List<Receita> findByUsuarioIdUsuario(Long idUsuario);

    /**
     * Busca receitas de um usuário em um período específico
     * @param idUsuario ID do usuário
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de receitas no período
     */
    List<Receita> findByUsuarioIdUsuarioAndDataEntradaBetween(
            Long idUsuario, 
            LocalDate dataInicio, 
            LocalDate dataFim
    );

    /**
     * Busca receitas pendentes de um usuário
     * @param idUsuario ID do usuário
     * @param pendente Flag pendente (0 ou 1)
     * @return Lista de receitas pendentes
     */
    List<Receita> findByUsuarioIdUsuarioAndPendente(Long idUsuario, Integer pendente);

    /**
     * Busca receitas com filtros opcionais (JPQL dinâmica)
     * Todos os parâmetros são opcionais
     * @param idUsuario ID do usuário (obrigatório)
     * @param dataInicio Data inicial (opcional)
     * @param dataFim Data final (opcional)
     * @param idCategoria ID da categoria (opcional)
     * @param pendente Status pendente (opcional)
     * @return Lista de receitas filtradas
     */
    @Query("SELECT r FROM Receita r WHERE r.usuario.idUsuario = :idUsuario " +
           "AND (:dataInicio IS NULL OR r.dataEntrada >= :dataInicio) " +
           "AND (:dataFim IS NULL OR r.dataEntrada <= :dataFim) " +
           "AND (:idCategoria IS NULL OR r.categoria.idCategoria = :idCategoria) " +
           "AND (:pendente IS NULL OR r.pendente = :pendente) " +
           "ORDER BY r.dataEntrada DESC")
    List<Receita> findByFiltros(
            @Param("idUsuario") Long idUsuario,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("idCategoria") Long idCategoria,
            @Param("pendente") Integer pendente
    );
}


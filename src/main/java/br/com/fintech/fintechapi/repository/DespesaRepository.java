package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
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
}


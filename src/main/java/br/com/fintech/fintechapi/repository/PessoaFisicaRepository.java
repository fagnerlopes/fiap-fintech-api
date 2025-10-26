package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para a entidade PessoaFisica
 */
@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {
    
    /**
     * Busca uma pessoa física pelo CPF
     * @param cpf CPF da pessoa física
     * @return Optional contendo a pessoa física se encontrada
     */
    Optional<PessoaFisica> findByCpf(String cpf);
    
    /**
     * Verifica se existe uma pessoa física com o CPF informado
     * @param cpf CPF a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Busca uma pessoa física pelo ID do usuário
     * @param idUsuario ID do usuário
     * @return Optional contendo a pessoa física se encontrada
     */
    Optional<PessoaFisica> findByUsuarioIdUsuario(Long idUsuario);
}


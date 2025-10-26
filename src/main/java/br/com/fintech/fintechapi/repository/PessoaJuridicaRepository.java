package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para a entidade PessoaJuridica
 */
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {
    
    /**
     * Busca uma pessoa jurídica pelo CNPJ
     * @param cnpj CNPJ da pessoa jurídica
     * @return Optional contendo a pessoa jurídica se encontrada
     */
    Optional<PessoaJuridica> findByCnpj(String cnpj);
    
    /**
     * Verifica se existe uma pessoa jurídica com o CNPJ informado
     * @param cnpj CNPJ a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByCnpj(String cnpj);
    
    /**
     * Busca uma pessoa jurídica pelo ID do usuário
     * @param idUsuario ID do usuário
     * @return Optional contendo a pessoa jurídica se encontrada
     */
    Optional<PessoaJuridica> findByUsuarioIdUsuario(Long idUsuario);
}


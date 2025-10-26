package br.com.fintech.fintechapi.repository;

import br.com.fintech.fintechapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para a entidade Usuario
 * Fornece operações CRUD e consultas customizadas
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca um usuário pelo email
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica se existe um usuário com o email informado
     * @param email Email a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
}


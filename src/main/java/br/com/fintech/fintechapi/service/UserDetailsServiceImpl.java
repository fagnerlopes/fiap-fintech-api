package br.com.fintech.fintechapi.service;

import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação do UserDetailsService do Spring Security
 * Responsável por carregar os dados do usuário para autenticação
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carrega um usuário pelo username (email)
     * @param username Email do usuário
     * @return UserDetails contendo os dados do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + username));
        
        return usuario;
    }
}


package br.com.fintech.fintechapi.controller;

import br.com.fintech.fintechapi.model.PessoaFisica;
import br.com.fintech.fintechapi.model.PessoaJuridica;
import br.com.fintech.fintechapi.model.TipoUsuario;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller responsável pelos endpoints de gerenciamento de usuários
 * Endpoints protegidos (requerem autenticação)
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Busca o usuário autenticado
     * GET /api/usuarios/me
     * 
     * @return Dados do usuário autenticado
     */
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> buscarUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Usuario usuario = usuarioService.buscarPorEmail(email);
        
        return ResponseEntity.ok(montarResponseUsuario(usuario));
    }

    /**
     * Lista todos os usuários
     * GET /api/usuarios
     * 
     * @return Lista de usuários
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Map<String, Object>>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            response.add(montarResponseUsuario(usuario));
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um usuário por ID
     * GET /api/usuarios/{id}
     * 
     * @param id ID do usuário
     * @return Usuario encontrado
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(montarResponseUsuario(usuario));
    }

    /**
     * Atualiza um usuário
     * PUT /api/usuarios/{id}
     * 
     * @param id ID do usuário
     * @param usuarioAtualizado Dados atualizados
     * @return Usuario atualizado
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuarioAtualizado) {
        
        usuarioAtualizado.setIdUsuario(id);
        Usuario usuario = usuarioService.atualizar(usuarioAtualizado);
        
        return ResponseEntity.ok(montarResponseUsuario(usuario));
    }

    /**
     * Deleta um usuário
     * DELETE /api/usuarios/{id}
     * 
     * @param id ID do usuário
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método auxiliar para montar a resposta do usuário sem expor a senha
     * 
     * @param usuario Usuario a ser transformado
     * @return Map com os dados do usuário
     */
    private Map<String, Object> montarResponseUsuario(Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("email", usuario.getEmail());
        response.put("tipoUsuario", usuario.getTipoUsuario());
        response.put("criadoEm", usuario.getCriadoEm());

        // Adicionar dados de PF ou PJ
        if (usuario.getTipoUsuario() == TipoUsuario.PF) {
            try {
                PessoaFisica pf = usuarioService.buscarPessoaFisicaPorIdUsuario(usuario.getIdUsuario());
                Map<String, Object> pfResponse = new HashMap<>();
                pfResponse.put("idPf", pf.getIdPf());
                pfResponse.put("nome", pf.getNome());
                pfResponse.put("cpf", pf.getCpf());
                pfResponse.put("dataNasc", pf.getDataNasc());
                response.put("pessoaFisica", pfResponse);
            } catch (Exception e) {
                // PF não encontrada
            }
        } else if (usuario.getTipoUsuario() == TipoUsuario.PJ) {
            try {
                PessoaJuridica pj = usuarioService.buscarPessoaJuridicaPorIdUsuario(usuario.getIdUsuario());
                Map<String, Object> pjResponse = new HashMap<>();
                pjResponse.put("idPj", pj.getIdPj());
                pjResponse.put("cnpj", pj.getCnpj());
                pjResponse.put("razaoSocial", pj.getRazaoSocial());
                response.put("pessoaJuridica", pjResponse);
            } catch (Exception e) {
                // PJ não encontrada
            }
        }

        return response;
    }
}


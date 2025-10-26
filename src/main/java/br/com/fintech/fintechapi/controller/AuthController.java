package br.com.fintech.fintechapi.controller;

import br.com.fintech.fintechapi.model.PessoaFisica;
import br.com.fintech.fintechapi.model.PessoaJuridica;
import br.com.fintech.fintechapi.model.TipoUsuario;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller responsável pelos endpoints de autenticação
 * Endpoints públicos (sem necessidade de autenticação)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Endpoint para registro de novo usuário
     * POST /api/auth/registro
     * 
     * @param requestBody Map contendo os dados do usuário
     * @return Usuario criado (sem senha)
     */
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> registrar(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extrair dados do usuário
            String email = (String) requestBody.get("email");
            String senha = (String) requestBody.get("senha");
            String tipoUsuarioStr = (String) requestBody.get("tipoUsuario");
            TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipoUsuarioStr);

            // Criar usuário
            Usuario usuario = new Usuario(tipoUsuario, email, senha);

            // Criar Pessoa Física ou Jurídica conforme o tipo
            if (tipoUsuario == TipoUsuario.PF) {
                Map<String, Object> pfData = (Map<String, Object>) requestBody.get("pessoaFisica");
                if (pfData == null) {
                    throw new IllegalArgumentException("Dados de Pessoa Física são obrigatórios");
                }
                
                PessoaFisica pf = new PessoaFisica();
                pf.setUsuario(usuario);
                pf.setNome((String) pfData.get("nome"));
                pf.setCpf((String) pfData.get("cpf"));
                
                // Data de nascimento (opcional)
                if (pfData.get("dataNasc") != null) {
                    pf.setDataNasc(java.time.LocalDate.parse((String) pfData.get("dataNasc")));
                }
                
                usuario.setPessoaFisica(pf);
                
            } else if (tipoUsuario == TipoUsuario.PJ) {
                Map<String, Object> pjData = (Map<String, Object>) requestBody.get("pessoaJuridica");
                if (pjData == null) {
                    throw new IllegalArgumentException("Dados de Pessoa Jurídica são obrigatórios");
                }
                
                PessoaJuridica pj = new PessoaJuridica();
                pj.setUsuario(usuario);
                pj.setCnpj((String) pjData.get("cnpj"));
                pj.setRazaoSocial((String) pjData.get("razaoSocial"));
                
                usuario.setPessoaJuridica(pj);
            }

            // Registrar usuário
            Usuario usuarioSalvo = usuarioService.registrar(usuario);

            // Preparar resposta (sem senha)
            Map<String, Object> response = new HashMap<>();
            response.put("idUsuario", usuarioSalvo.getIdUsuario());
            response.put("email", usuarioSalvo.getEmail());
            response.put("tipoUsuario", usuarioSalvo.getTipoUsuario());
            response.put("criadoEm", usuarioSalvo.getCriadoEm());

            // Adicionar dados de PF ou PJ
            if (usuarioSalvo.getTipoUsuario() == TipoUsuario.PF && usuarioSalvo.getPessoaFisica() != null) {
                Map<String, Object> pfResponse = new HashMap<>();
                pfResponse.put("nome", usuarioSalvo.getPessoaFisica().getNome());
                pfResponse.put("cpf", usuarioSalvo.getPessoaFisica().getCpf());
                pfResponse.put("dataNasc", usuarioSalvo.getPessoaFisica().getDataNasc());
                response.put("pessoaFisica", pfResponse);
            } else if (usuarioSalvo.getTipoUsuario() == TipoUsuario.PJ && usuarioSalvo.getPessoaJuridica() != null) {
                Map<String, Object> pjResponse = new HashMap<>();
                pjResponse.put("cnpj", usuarioSalvo.getPessoaJuridica().getCnpj());
                pjResponse.put("razaoSocial", usuarioSalvo.getPessoaJuridica().getRazaoSocial());
                response.put("pessoaJuridica", pjResponse);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao registrar usuário: " + ex.getMessage(), ex);
        }
    }

    /**
     * Endpoint para login de usuário
     * POST /api/auth/login
     * 
     * @param loginRequest Map contendo email e senha
     * @return Informações do usuário autenticado
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String senha = loginRequest.get("senha");

        // Autenticar usuário
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha)
        );

        // Definir o contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Buscar usuário completo
        Usuario usuario = usuarioService.buscarPorEmail(email);

        // Preparar resposta (sem senha)
        Map<String, Object> response = new HashMap<>();
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("email", usuario.getEmail());
        response.put("tipoUsuario", usuario.getTipoUsuario());
        response.put("criadoEm", usuario.getCriadoEm());
        response.put("message", "Login realizado com sucesso");

        // Adicionar dados de PF ou PJ
        if (usuario.getTipoUsuario() == TipoUsuario.PF) {
            try {
                PessoaFisica pf = usuarioService.buscarPessoaFisicaPorIdUsuario(usuario.getIdUsuario());
                Map<String, Object> pfResponse = new HashMap<>();
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
                pjResponse.put("cnpj", pj.getCnpj());
                pjResponse.put("razaoSocial", pj.getRazaoSocial());
                response.put("pessoaJuridica", pjResponse);
            } catch (Exception e) {
                // PJ não encontrada
            }
        }

        return ResponseEntity.ok(response);
    }
}

